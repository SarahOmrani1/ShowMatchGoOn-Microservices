package com.exemple.user.feignClient;

import com.exemple.user.dto.ReportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

// "MODERATION-SERVICE" est le nom exact déclaré dans Eureka
@FeignClient(name = "MODERATION-SERVICE")
public interface ModerationClient {

    @GetMapping("/api/reports/user/{userId}")
    List<ReportDTO> getReportsByUserId(@PathVariable("userId") Long userId);
}
