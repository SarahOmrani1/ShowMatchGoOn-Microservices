package client;

import dto.ContentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "content-notification")
public interface ContentClient {

    @GetMapping("/api/contents/{id}")
    ContentResponseDTO getContentById(@PathVariable("id") String id);
}
