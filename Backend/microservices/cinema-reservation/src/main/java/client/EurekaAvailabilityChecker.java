package client;

import exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class EurekaAvailabilityChecker {

    private final RestClient restClient;
    private final String eurekaUrl;

    public EurekaAvailabilityChecker(
            @Value("${eureka.client.service-url.defaultZone}") String eurekaUrl) {
        this.restClient = RestClient.create();
        this.eurekaUrl = eurekaUrl.split(",")[0].trim();
    }

    public void requireAvailable() {
        try {
            restClient.get()
                    .uri(eurekaUrl.endsWith("/") ? eurekaUrl + "apps" : eurekaUrl + "/apps")
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException exception) {
            throw new ServiceUnavailableException("Eureka service is unavailable");
        }
    }
}
