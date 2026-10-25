package com.exemple.contentnotification.config;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Accepts either ISO date-time (yyyy-MM-ddTHH:mm:ss) or date-only (yyyy-MM-dd).
 * Date-only values are normalized to start-of-day (00:00:00).
 * Uses Jackson 3 (tools.jackson), which Spring Boot 4 uses for HTTP JSON.
 */
public class FlexibleLocalDateTimeDeserializer extends ValueDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctxt) {
        final String raw = parser.getValueAsString();
        if (raw == null) {
            return null;
        }

        final String value = raw.trim();
        if (value.isEmpty()) {
            return null;
        }

        try {
            if (value.length() == 10) {
                return LocalDate.parse(value).atStartOfDay();
            }
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ex) {
            throw DatabindException.from(
                    parser,
                    "Invalid releaseDate format. Use yyyy-MM-dd or yyyy-MM-ddTHH:mm:ss",
                    ex
            );
        }
    }
}
