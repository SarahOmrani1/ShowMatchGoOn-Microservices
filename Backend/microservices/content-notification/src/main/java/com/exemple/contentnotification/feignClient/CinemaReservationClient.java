package com.exemple.contentnotification.feignClient;

import com.exemple.contentnotification.dto.SeanceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "gateway", contextId = "cinemaReservationClient")
public interface CinemaReservationClient {
    @GetMapping("/api/seances")
    List<SeanceDTO> getAllSeances();

    @GetMapping("/api/seances/{id}")
    SeanceDTO getSeanceById(@PathVariable("id") String id);
}