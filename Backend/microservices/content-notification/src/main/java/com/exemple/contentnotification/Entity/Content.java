package com.exemple.contentnotification.Entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "contents")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Film.class, name = "Film"),
    @JsonSubTypes.Type(value = Series.class, name = "Series"),
    @JsonSubTypes.Type(value = Documentary.class, name = "Documentary")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Content {
    @Id
    private String id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private LocalDateTime releaseDate;

    private LocalDateTime publishAt;

    private LocalDateTime expireAt;

    private LocalDateTime publishedAt;

    @NotNull(message = "Category is mandatory")
    private ContentCategory category;

    /**
     * Content type discriminator: FILM, SERIES, DOCUMENTARY
     * WHY: Helps identify the concrete type for polymorphic queries
     */
    private String contentType;

    private ContentStatus status;

    private Boolean visible = Boolean.FALSE;

    private Integer viewCount = 0;

    private Double rating;

    /**
     * Genre IDs: References to Genre documents (multiple genres per content)
     * WHY: Allows flexible genre management through a separate Genre collection
     */
    private List<String> genreIds = new ArrayList<>();

    /**
     * In microservices, User is referenced by ID only (not via @DBRef to another service's DB)
     */
    private String addedById;

    private String addedByUsername;

    /**
     * Comments stored as embedded simple objects (no cross-service @DBRef)
     */
    private List<String> commentIds = new ArrayList<>();
}
