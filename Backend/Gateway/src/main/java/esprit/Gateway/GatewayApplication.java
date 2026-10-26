package esprit.Gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
    /**
     * Définition programmatique des routes vers tous les microservices de ShowMatchGoOn
     * exactement comme dans le projet modèle de Mme Badia.
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 1. Microservice USER (Authentification & Gestion des utilisateurs)
                .route("user-service", r -> r
                        .path("/api/auth/**", "/api/users/**")
                        .uri("lb://USER"))
                // 2. Microservice MODÉRATION (Python FastAPI)
                .route("moderation-service", r -> r
                        .path("/api/reports/**", "/api/sanctions/**")
                        .uri("lb://MODERATION-SERVICE"))
                // 3. Microservice CONTENT-NOTIFICATION
                .route("content-notification-service", r -> r
                        .path("/api/contents/**", "/api/notifications/**")
                        .uri("lb://CONTENT-NOTIFICATION"))
                // 4. Microservice SUBSCRIPTION-LOYALTY
                .route("subscription-loyalty-service", r -> r
                        .path("/api/subscriptions/**", "/api/loyalty/**")
                        .uri("lb://SUBSCRIPTION-LOYALTY"))
                // 5. Microservice WATCHPARTY-CHAT
                .route("watchparty-chat-service", r -> r
                        .path("/api/watchparties/**", "/api/chat/**")
                        .uri("lb://WATCHPARTY-CHAT"))
                // 6. Microservice CINEMA-RESERVATION
                .route("cinema-reservation-service", r -> r
                        .path("/api/cinemas/**", "/api/reservations/**")
                        .uri("lb://CINEMA-RESERVATION"))
                // 7. Microservice FORUM-PROMOTION
                .route("forum-promotion-service", r -> r
                        .path("/api/forums/**", "/api/promotions/**")
                        .uri("lb://FORUM-PROMOTION"))
                .build();
    }
}