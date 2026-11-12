package com.exemple.contentnotification.feignClient;

import com.exemple.contentnotification.dto.SanctionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Communication synchrone content-notification -> moderation, en passant OBLIGATOIREMENT par l'API Gateway.
 * name = "gateway" : nom de l'API Gateway dans Eureka (Feign trouve son adresse via Eureka, pas d'url en dur).
 * La Gateway route ensuite /api/sanctions/** vers lb://MODERATION-SERVICE (elle aussi via Eureka).
 * contextId : obligatoire car deux clients Feign utilisent le même name "gateway".
 */
@FeignClient(name = "gateway", contextId = "moderationClient")
public interface ModerationClient {

    @GetMapping("/api/sanctions/user/{userId}")
    List<SanctionDTO> getSanctionsByUserId(@PathVariable("userId") Long userId);
}
