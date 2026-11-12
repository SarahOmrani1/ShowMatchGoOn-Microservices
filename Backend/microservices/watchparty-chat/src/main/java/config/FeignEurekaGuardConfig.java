package config;

import exception.EurekaUnavailableException;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.HttpURLConnection;
import java.net.URI;

@Configuration
public class FeignEurekaGuardConfig {

    @Bean
    public RequestInterceptor eurekaGuardInterceptor(
            @Value("${eureka.client.service-url.defaultZone}") String eurekaUrl) {

        return template -> {
            try {
                HttpURLConnection conn =
                        (HttpURLConnection) URI.create(eurekaUrl + "/apps").toURL().openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(1000);
                conn.setReadTimeout(1000);
                int code = conn.getResponseCode();
                conn.disconnect();
                if (code >= 500) {
                    throw new EurekaUnavailableException("Eureka indisponible");
                }
            } catch (EurekaUnavailableException e) {
                throw e;
            } catch (Exception e) {
                throw new EurekaUnavailableException("Eureka indisponible");
            }
        };
    }
}