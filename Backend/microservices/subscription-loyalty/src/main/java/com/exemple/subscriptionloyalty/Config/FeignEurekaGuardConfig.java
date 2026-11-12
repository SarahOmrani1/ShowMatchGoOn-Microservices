package com.exemple.subscriptionloyalty.Config;

import com.exemple.subscriptionloyalty.Exception.EurekaUnavailableException;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.HttpURLConnection;
import java.net.URI;

@Configuration
public class FeignEurekaGuardConfig {

    @Bean
    RequestInterceptor eurekaGuardInterceptor(
            @Value("${eureka.client.service-url.defaultZone}") String eurekaUrl) {
        return template -> {
            HttpURLConnection connection = null;
            try {
                String baseUrl = eurekaUrl.replaceAll("/+$", "");
                connection = (HttpURLConnection) URI.create(baseUrl + "/apps")
                        .toURL().openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                connection.setUseCaches(false);

                int status = connection.getResponseCode();
                if (status < HttpURLConnection.HTTP_OK || status >= HttpURLConnection.HTTP_MULT_CHOICE) {
                    throw new EurekaUnavailableException(
                            "Eureka indisponible (HTTP " + status + ")");
                }
            } catch (EurekaUnavailableException exception) {
                throw exception;
            } catch (Exception exception) {
                throw new EurekaUnavailableException("Eureka indisponible");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        };
    }
}
