package com.exemple.contentnotification.feignClient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

/**
 * Exécuté automatiquement AVANT chaque appel Feign (communication synchrone).
 * Il vérifie EN DIRECT auprès du serveur Eureka que le service appelé est enregistré et UP.
 * Si Eureka est arrêté, ou si le service n'y est pas enregistré, l'appel est bloqué (503),
 * même si l'adresse du service est encore dans le cache local du client Eureka.
 */
@Slf4j
@Component
public class EurekaCheckInterceptor implements RequestInterceptor {

    private final String eurekaUrl;
    private final RestClient restClient;

    public EurekaCheckInterceptor(@Value("${eureka.client.service-url.defaultZone}") String eurekaUrl) {
        this.eurekaUrl = eurekaUrl.endsWith("/") ? eurekaUrl : eurekaUrl + "/";
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(2));
        requestFactory.setReadTimeout(Duration.ofSeconds(2));
        this.restClient = RestClient.builder().requestFactory(requestFactory).build();
    }

    @Override
    public void apply(RequestTemplate template) {
        String serviceName = template.feignTarget().name().toUpperCase();
        String body;
        try {
            // API REST d'Eureka : GET /eureka/apps/{SERVICE} -> 200 si enregistré, 404 sinon
            body = restClient.get()
                    .uri(eurekaUrl + "apps/" + serviceName)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("{} is not registered in Eureka: synchronous call blocked", serviceName);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    serviceName + " is not registered in Eureka: synchronous call blocked");
        } catch (RestClientException e) {
            log.error("Eureka server unreachable ({}): synchronous call to {} blocked", eurekaUrl, serviceName);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Eureka server unavailable: synchronous call to " + serviceName + " blocked");
        }

        if (body == null || !body.contains("\"UP\"")) {
            log.error("{} has no UP instance in Eureka: synchronous call blocked", serviceName);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    serviceName + " has no UP instance in Eureka: synchronous call blocked");
        }
        log.info("Eureka check OK: {} is registered and UP", serviceName);
    }
}
