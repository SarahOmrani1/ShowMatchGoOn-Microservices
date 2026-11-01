package com.exemple.contentnotification.Service.impl;

import com.exemple.contentnotification.dto.*;
import com.exemple.contentnotification.Entity.*;
import com.exemple.contentnotification.exception.ResourceNotFoundException;
import com.exemple.contentnotification.Repository.ContentRepository;
import com.exemple.contentnotification.Service.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Content Service Implementation
 * WHY: Films, Series and Documentaries share the "contents" collection, so a single
 * ContentRepository handles all three (the concrete type is resolved with instanceof)
 * Users live in the user service: content only keeps the creator's username
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final JsonMapper jsonMapper;

    @Value("${app.ai.recommendation-base-url:http://localhost:5055}")
    private String recommendationServiceBaseUrl;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Override
    public FilmDTO createFilm(FilmDTO filmDTO, String username) {
        log.info("Creating film: {}", filmDTO.getTitle());

        Film film = new Film();
        mapCommonFieldsToEntity(filmDTO, film, username);
        film.setContentType("FILM");
        film.setDurationInMinutes(filmDTO.getDurationInMinutes());
        film.setDirector(filmDTO.getDirector());
        log.info("Film mapped with title: {}, director: {}", film.getTitle(), film.getDirector());

        Film savedFilm = contentRepository.save(film);
        log.info("Film saved successfully with ID: {}", savedFilm.getId());
        return mapToFilmDTO(savedFilm);
    }

    @Override
    public SeriesDTO createSeries(SeriesDTO seriesDTO, String username) {
        log.info("Creating series: {}", seriesDTO.getTitle());

        Series series = new Series();
        mapCommonFieldsToEntity(seriesDTO, series, username);
        series.setContentType("SERIES");
        series.setNumberOfSeasons(seriesDTO.getNumberOfSeasons());
        series.setNumberOfEpisodes(seriesDTO.getNumberOfEpisodes());
        series.setIsCompleted(seriesDTO.getIsCompleted());
        log.info("Series mapped with title: {}, seasons: {}", series.getTitle(), series.getNumberOfSeasons());

        Series savedSeries = contentRepository.save(series);
        log.info("Series saved successfully with ID: {}", savedSeries.getId());
        return mapToSeriesDTO(savedSeries);
    }

    @Override
    public DocumentaryDTO createDocumentary(DocumentaryDTO documentaryDTO, String username) {
        log.info("Creating documentary: {}", documentaryDTO.getTitle());

        Documentary documentary = new Documentary();
        mapCommonFieldsToEntity(documentaryDTO, documentary, username);
        documentary.setContentType("DOCUMENTARY");
        documentary.setTopic(documentaryDTO.getTopic());
        documentary.setNarrator(documentaryDTO.getNarrator());
        log.info("Documentary mapped with title: {}, topic: {}", documentary.getTitle(), documentary.getTopic());

        Documentary savedDoc = contentRepository.save(documentary);
        log.info("Documentary saved successfully with ID: {}", savedDoc.getId());
        return mapToDocumentaryDTO(savedDoc);
    }

    @Override
    public ContentDTO getContentById(String id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found: " + id));
        return mapToDTO(content);
    }

    @Override
    public List<ContentDTO> getAllContent() {
        try {
            log.info("Fetching all content");
            List<Content> allContent = contentRepository.findAll();
            log.info("Retrieved {} content items from database", allContent.size());

            List<ContentDTO> result = allContent.stream()
                    .map(this::mapToDTO)
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            log.info("Mapped to {} DTOs", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error fetching content: {}", e.getMessage());
            log.debug("Full error:", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ContentAnalyticsDTO> getContentAnalytics(String category, String genreKeyword, int limit) {
        return contentRepository.findContentAnalytics(category, genreKeyword, limit);
    }

    @Override
    public List<ContentAnalyticsDTO> getTop10Content(String category, String genreKeyword) {
        return contentRepository.findContentAnalytics(category, genreKeyword, 10);
    }

    @Override
    public List<ContentRecommendationDTO> getContentRecommendations(String userId, int limit) {
        List<Content> contents = contentRepository.findAll();
        if (contents.isEmpty()) {
            return List.of();
        }

        try {
            List<ContentRecommendationDTO> aiRecommendations = callAiRecommendations(contents, limit);
            if (!aiRecommendations.isEmpty()) {
                return aiRecommendations;
            }
        } catch (Exception e) {
            log.warn("AI recommendation service unavailable, using local fallback: {}", e.getMessage());
        }

        Map<String, ContentAnalyticsDTO> analyticsByContentId = contentRepository.findContentAnalytics(null, null, Math.max(contents.size(), 1))
                .stream()
                .collect(Collectors.toMap(ContentAnalyticsDTO::getContentId, item -> item, (left, right) -> left));

        Set<String> preferredGenres = resolvePreferredGenres(contents);
        LocalDateTime now = LocalDateTime.now();

        return contents.stream()
                .map(content -> toRecommendation(content, analyticsByContentId.get(content.getId()), preferredGenres, now))
                .sorted(Comparator.comparingDouble(ContentRecommendationDTO::getRecommendationScore).reversed()
                        .thenComparing(ContentRecommendationDTO::getTitle, String.CASE_INSENSITIVE_ORDER))
                .limit(Math.max(1, limit))
                .toList();
    }

    @Override
    public List<ContentSearchResultDTO> searchContentAdvanced(String keyword, String genreKeyword, String category, int limit) {
        return contentRepository.advancedKeywordSearch(keyword, genreKeyword, category, limit);
    }

    @Override
    public PageResponseDTO<ContentDTO> getAllContentPaginated(int page, int size, String search, String categoryId, String sortBy, String sortDirection) {
        try {
            List<ContentDTO> allContent = contentRepository.findAll().stream()
                    .map(this::mapToDTO)
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            // Filter by search query
            if (search != null && !search.isEmpty()) {
                String searchLower = search.toLowerCase();
                allContent = allContent.stream()
                        .filter(c -> c.getTitle().toLowerCase().contains(searchLower) ||
                                (c.getDescription() != null && c.getDescription().toLowerCase().contains(searchLower)))
                        .collect(Collectors.toList());
            }

            // Filter by category (unknown category -> empty page)
            if (categoryId != null && !categoryId.isEmpty()) {
                ContentCategory category = ContentCategory.fromString(categoryId);
                if (category == null) {
                    return emptyPage(page, size);
                }
                allContent = allContent.stream()
                        .filter(c -> c.getCategory() == category)
                        .collect(Collectors.toList());
            }

            // Sort results
            if (sortBy != null && !sortBy.isEmpty()) {
                boolean ascending = sortDirection == null || "ASC".equalsIgnoreCase(sortDirection);
                if ("title".equalsIgnoreCase(sortBy)) {
                    allContent.sort((a, b) -> ascending ? a.getTitle().compareTo(b.getTitle()) : b.getTitle().compareTo(a.getTitle()));
                } else if ("releaseDate".equalsIgnoreCase(sortBy)) {
                    allContent.sort((a, b) -> {
                        if (a.getReleaseDate() == null || b.getReleaseDate() == null) return 0;
                        return ascending ? a.getReleaseDate().compareTo(b.getReleaseDate()) : b.getReleaseDate().compareTo(a.getReleaseDate());
                    });
                }
            }

            // Paginate results
            int totalElements = allContent.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int startIndex = Math.min(page * size, totalElements);
            int endIndex = Math.min((page + 1) * size, totalElements);

            List<ContentDTO> pageContent = allContent.subList(startIndex, endIndex);

            return PageResponseDTO.<ContentDTO>builder()
                    .content(pageContent)
                    .page(page)
                    .size(size)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .first(page == 0)
                    .last(page >= totalPages - 1)
                    .numberOfElements(pageContent.size())
                    .hasNext(page < totalPages - 1)
                    .hasPrevious(page > 0)
                    .build();
        } catch (Exception e) {
            log.error("Error fetching paginated content: {}", e.getMessage());
            return emptyPage(page, size);
        }
    }

    @Override
    public FilmDTO updateFilm(String id, FilmDTO filmDTO) {
        Film film = findContentOfType(id, Film.class, "Film");

        mapUpdatableFieldsToEntity(filmDTO, film);
        film.setDurationInMinutes(filmDTO.getDurationInMinutes());
        film.setDirector(filmDTO.getDirector());

        return mapToFilmDTO(contentRepository.save(film));
    }

    @Override
    public SeriesDTO updateSeries(String id, SeriesDTO seriesDTO) {
        Series series = findContentOfType(id, Series.class, "Series");

        mapUpdatableFieldsToEntity(seriesDTO, series);
        series.setNumberOfSeasons(seriesDTO.getNumberOfSeasons());
        series.setNumberOfEpisodes(seriesDTO.getNumberOfEpisodes());
        series.setIsCompleted(seriesDTO.getIsCompleted());

        return mapToSeriesDTO(contentRepository.save(series));
    }

    @Override
    public DocumentaryDTO updateDocumentary(String id, DocumentaryDTO documentaryDTO) {
        Documentary doc = findContentOfType(id, Documentary.class, "Documentary");

        mapUpdatableFieldsToEntity(documentaryDTO, doc);
        doc.setTopic(documentaryDTO.getTopic());
        doc.setNarrator(documentaryDTO.getNarrator());

        return mapToDocumentaryDTO(contentRepository.save(doc));
    }

    @Override
    public void deleteContent(String id) {
        if (!contentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Content not found: " + id);
        }
        contentRepository.deleteById(id);
    }

    private <T extends Content> T findContentOfType(String id, Class<T> type, String label) {
        return contentRepository.findById(id)
                .filter(type::isInstance)
                .map(type::cast)
                .orElseThrow(() -> new ResourceNotFoundException(label + " not found: " + id));
    }

    private PageResponseDTO<ContentDTO> emptyPage(int page, int size) {
        return PageResponseDTO.<ContentDTO>builder()
                .content(new ArrayList<>())
                .page(page)
                .size(size)
                .totalElements(0)
                .totalPages(0)
                .first(true)
                .last(true)
                .numberOfElements(0)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

    /**
     * Without access to the user's history (it lives in other services), every genre in the catalog counts as preferred
     */
    private Set<String> resolvePreferredGenres(List<Content> contents) {
        Set<String> preferredGenres = new HashSet<>();
        contents.forEach(content -> preferredGenres.addAll(content.getGenreIds()));
        return preferredGenres;
    }

    private List<ContentRecommendationDTO> callAiRecommendations(List<Content> contents, int limit) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("limit", Math.max(1, limit));
        payload.put("user", buildAiUserProfile(contents));
        payload.put("contents", contents.stream().map(this::toAiContentPayload).collect(Collectors.toList()));

        String requestBody = jsonMapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(recommendationServiceBaseUrl + "/recommend"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(12))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("AI service returned HTTP " + response.statusCode());
        }

        List<Map<String, Object>> responseItems = jsonMapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        return responseItems.stream()
                .map(this::mapAiRecommendation)
                .collect(Collectors.toList());
    }

    private Map<String, Object> buildAiUserProfile(List<Content> contents) {
        Set<String> preferredCategories = new HashSet<>();
        Set<String> preferredTypes = new HashSet<>();

        contents.stream()
                .map(Content::getCategory)
                .filter(category -> category != null)
                .map(Enum::name)
                .forEach(preferredCategories::add);
        contents.stream().map(this::resolveContentType).forEach(preferredTypes::add);

        Map<String, Object> profile = new HashMap<>();
        profile.put("preferredCategories", new ArrayList<>(preferredCategories));
        profile.put("preferredTypes", new ArrayList<>(preferredTypes));
        profile.put("preferredGenres", new ArrayList<>(resolvePreferredGenres(contents)));
        return profile;
    }

    private Map<String, Object> toAiContentPayload(Content content) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentId", content.getId());
        payload.put("title", content.getTitle());
        payload.put("description", content.getDescription());
        payload.put("category", content.getCategory() != null ? content.getCategory().name() : "MOVIE");
        payload.put("contentType", resolveContentType(content));
        payload.put("genres", content.getGenreIds() != null ? new ArrayList<>(content.getGenreIds()) : List.of());
        payload.put("viewCount", content.getViewCount() != null ? content.getViewCount() : 0);
        payload.put("publishAt", content.getPublishAt() != null ? content.getPublishAt().toString() : null);
        payload.put("releaseDate", content.getReleaseDate() != null ? content.getReleaseDate().toString() : null);
        payload.put("status", content.getStatus() != null ? content.getStatus().name() : null);
        return payload;
    }

    private ContentRecommendationDTO mapAiRecommendation(Map<String, Object> item) {
        String categoryValue = item.get("category") != null ? String.valueOf(item.get("category")) : "MOVIE";
        ContentCategory category = ContentCategory.fromString(categoryValue);
        if (category == null) {
            category = ContentCategory.MOVIE;
        }

        Integer viewCount = item.get("viewCount") != null ? ((Number) item.get("viewCount")).intValue() : 0;
        Double recommendationScore = item.get("recommendationScore") != null
                ? ((Number) item.get("recommendationScore")).doubleValue()
                : 0.0d;
        Double engagementScore = item.get("engagementScore") != null
                ? ((Number) item.get("engagementScore")).doubleValue()
                : recommendationScore;

        List<String> genres = new ArrayList<>();
        Object rawGenres = item.get("genres");
        if (rawGenres instanceof List<?> list) {
            for (Object value : list) {
                if (value != null) {
                    genres.add(String.valueOf(value));
                }
            }
        }

        return ContentRecommendationDTO.builder()
                .contentId(item.get("contentId") != null ? String.valueOf(item.get("contentId")) : null)
                .title(item.get("title") != null ? String.valueOf(item.get("title")) : "Untitled")
                .description(item.get("description") != null ? String.valueOf(item.get("description")) : null)
                .category(category)
                .genres(genres)
                .viewCount(viewCount)
                .engagementScore(engagementScore)
                .recommendationScore(recommendationScore)
                .reason(item.get("reason") != null ? String.valueOf(item.get("reason")) : "AI-ranked recommendation")
                .build();
    }

    private String resolveContentType(Content content) {
        if (content instanceof Film) {
            return "FILM";
        }
        if (content instanceof Series) {
            return "SERIES";
        }
        if (content instanceof Documentary) {
            return "DOCUMENTARY";
        }
        return content.getContentType() != null ? content.getContentType() : "UNKNOWN";
    }

    private ContentRecommendationDTO toRecommendation(Content content,
                                                       ContentAnalyticsDTO analytics,
                                                       Set<String> preferredGenres,
                                                       LocalDateTime now) {
        int viewCount = content.getViewCount() == null ? 0 : content.getViewCount();
        double engagementScore = analytics != null ? analytics.getEngagementScore() : viewCount * 0.7d;
        double popularityScore = Math.log1p(viewCount) * 8.0d;
        double genreMatchScore = content.getGenreIds().stream().anyMatch(preferredGenres::contains)
                ? 15.0d
                : Math.min(content.getGenreIds().size(), 3) * 2.0d;
        double freshnessScore = computeFreshnessScore(content, now);

        double recommendationScore = engagementScore + popularityScore + genreMatchScore + freshnessScore;

        return ContentRecommendationDTO.builder()
                .contentId(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .category(content.getCategory())
                .genres(new ArrayList<>(content.getGenreIds()))
                .viewCount(viewCount)
                .engagementScore(engagementScore)
                .recommendationScore(recommendationScore)
                .reason(buildRecommendationReason(content, preferredGenres, freshnessScore))
                .build();
    }

    private double computeFreshnessScore(Content content, LocalDateTime now) {
        LocalDateTime referenceDate = content.getPublishAt() != null ? content.getPublishAt() : content.getReleaseDate();
        if (referenceDate == null) {
            return 0.0d;
        }

        long ageDays = ChronoUnit.DAYS.between(referenceDate, now);
        if (ageDays <= 0) {
            return 12.0d;
        }
        if (ageDays <= 30) {
            return 10.0d;
        }
        if (ageDays <= 180) {
            return 6.0d;
        }
        if (ageDays <= 730) {
            return 3.0d;
        }
        return 1.0d;
    }

    private String buildRecommendationReason(Content content,
                                             Set<String> preferredGenres,
                                             double freshnessScore) {
        List<String> reasons = new ArrayList<>();
        if ((content.getViewCount() != null ? content.getViewCount() : 0) >= 1000) {
            reasons.add("high viewer demand");
        }
        if (content.getGenreIds().stream().anyMatch(preferredGenres::contains)) {
            reasons.add("matches preferred genres");
        }
        if (freshnessScore >= 10.0d) {
            reasons.add("recent release");
        }
        if (reasons.isEmpty()) {
            reasons.add("balanced content profile");
        }
        return String.join(", ", reasons);
    }

    private void mapCommonFieldsToEntity(ContentDTO dto, Content entity, String username) {
        entity.setAddedByUsername(username);
        mapUpdatableFieldsToEntity(dto, entity);
    }

    private void mapUpdatableFieldsToEntity(ContentDTO dto, Content entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setReleaseDate(dto.getReleaseDate());
        entity.setPublishAt(dto.getPublishAt() != null ? dto.getPublishAt() : dto.getReleaseDate());
        entity.setExpireAt(dto.getExpireAt());
        entity.setCategory(dto.getCategory());
        entity.setGenreIds(dto.getGenreIds() != null ? dto.getGenreIds() : new ArrayList<>());
        entity.setViewCount(dto.getViewCount() != null ? dto.getViewCount() : 0);

        initializeLifecycleState(entity, dto.getStatus());
    }

    private void mapCommonFieldsToDTO(Content entity, ContentDTO dto) {
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setReleaseDate(entity.getReleaseDate());
        dto.setPublishAt(entity.getPublishAt());
        dto.setExpireAt(entity.getExpireAt());
        dto.setPublishedAt(entity.getPublishedAt());
        dto.setCategory(entity.getCategory());
        dto.setGenreIds(entity.getGenreIds() != null ? entity.getGenreIds() : new ArrayList<>());
        dto.setStatus(entity.getStatus());
        dto.setVisible(entity.getVisible());
        dto.setViewCount(entity.getViewCount() != null ? entity.getViewCount() : 0);
        dto.setAddedById(entity.getAddedById());
        dto.setAddedByUsername(entity.getAddedByUsername());
    }

    private void initializeLifecycleState(Content entity, ContentStatus requestedStatus) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime publishAt = entity.getPublishAt();

        if (requestedStatus != null) {
            entity.setStatus(requestedStatus);
            if (requestedStatus == ContentStatus.PUBLISHED) {
                entity.setVisible(Boolean.TRUE);
                if (entity.getPublishedAt() == null) {
                    entity.setPublishedAt(now);
                }
            } else {
                entity.setVisible(Boolean.FALSE);
            }
            return;
        }

        if (publishAt != null && publishAt.isAfter(now)) {
            entity.setStatus(ContentStatus.SCHEDULED);
            entity.setVisible(Boolean.FALSE);
            return;
        }

        entity.setStatus(ContentStatus.PUBLISHED);
        entity.setVisible(Boolean.TRUE);
        if (entity.getPublishedAt() == null) {
            entity.setPublishedAt(now);
        }
    }

    private ContentDTO mapToDTO(Content content) {
        if (content instanceof Film film) return mapToFilmDTO(film);
        if (content instanceof Series series) return mapToSeriesDTO(series);
        if (content instanceof Documentary documentary) return mapToDocumentaryDTO(documentary);
        return null;
    }

    private FilmDTO mapToFilmDTO(Film film) {
        FilmDTO dto = new FilmDTO();
        mapCommonFieldsToDTO(film, dto);
        dto.setDurationInMinutes(film.getDurationInMinutes());
        dto.setDirector(film.getDirector());
        dto.setContentType("FILM");
        return dto;
    }

    private SeriesDTO mapToSeriesDTO(Series series) {
        SeriesDTO dto = new SeriesDTO();
        mapCommonFieldsToDTO(series, dto);
        dto.setNumberOfSeasons(series.getNumberOfSeasons());
        dto.setNumberOfEpisodes(series.getNumberOfEpisodes());
        dto.setIsCompleted(series.getIsCompleted());
        dto.setContentType("SERIES");
        return dto;
    }

    private DocumentaryDTO mapToDocumentaryDTO(Documentary doc) {
        DocumentaryDTO dto = new DocumentaryDTO();
        mapCommonFieldsToDTO(doc, dto);
        dto.setTopic(doc.getTopic());
        dto.setNarrator(doc.getNarrator());
        dto.setContentType("DOCUMENTARY");
        return dto;
    }
}
