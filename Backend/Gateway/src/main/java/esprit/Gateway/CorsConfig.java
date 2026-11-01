package esprit.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;

/**
 * Configuration CORS globale de l'API Gateway.
 * Permet au Frontend (Angular, React, Flutter Web) d'appeler l'API Gateway
 * sans être bloqué par les politiques de sécurité du navigateur.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 1. Origines autorisées (Angular par défaut : 4200, React : 3000, 5173)
        corsConfig.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "http://localhost:3000",
                "http://localhost:5173"
        ));

        // 2. Méthodes HTTP autorisées
        corsConfig.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // 3. En-têtes HTTP autorisés (Authorization pour JWT, Content-Type, etc.)
        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        // 4. Autoriser les cookies et identifiants d'authentification
        corsConfig.setAllowCredentials(false);

        // 5. Mise en cache de la requête OPTIONS (preflight) pendant 1 heure
        corsConfig.setMaxAge(3600L);

        // 6. Appliquer cette règle à TOUTES les routes de la Gateway
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
