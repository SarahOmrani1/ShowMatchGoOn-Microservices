package com.exemple.contentnotification.Repository;

import com.exemple.contentnotification.dto.ContentAnalyticsDTO;
import com.exemple.contentnotification.dto.ContentSearchResultDTO;

import java.util.List;

public interface ContentRepositoryCustom {
    List<ContentAnalyticsDTO> findContentAnalytics(String category, String genreKeyword, int limit);

    List<ContentSearchResultDTO> advancedKeywordSearch(String keyword, String genreKeyword, String category, int limit);
}