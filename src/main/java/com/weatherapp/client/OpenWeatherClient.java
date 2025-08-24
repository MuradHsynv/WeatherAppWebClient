package com.weatherapp.client;

import com.weatherapp.dto.OpenWeatherDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class OpenWeatherClient {

    private final WebClient webClient;
    private final String baseUrl;
    private final String apiKey;

    public OpenWeatherClient(@Value("${openweather.api.base-url}") String baseUrl,
                             @Value("${openweather.api.key}") String apiKey,
                             WebClient.Builder webClientBuilder) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    public Mono<OpenWeatherDto.Response> getCurrentWeatherReactive(double latitude, double longitude) {
        String uri = UriComponentsBuilder.fromPath("/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .build()
                .toString();

        log.debug("Calling OpenWeather API: {}{}", baseUrl, uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(errorBody -> new RuntimeException("OpenWeather API client error: " + errorBody))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .map(errorBody -> new RuntimeException("OpenWeather API server error: " + errorBody))
                )
                .bodyToMono(OpenWeatherDto.Response.class)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(response -> log.debug("Successfully received weather data for {}, {}", latitude, longitude))
                .doOnError(WebClientResponseException.class, ex ->
                        log.error("HTTP error calling OpenWeather API for {}, {}: {} - {}",
                                latitude, longitude, ex.getStatusCode(), ex.getResponseBodyAsString()))
                .doOnError(Exception.class, ex ->
                        log.error("Error calling OpenWeather API for {}, {}", latitude, longitude, ex))
                .onErrorMap(Exception.class, ex -> {
                    if (ex instanceof WebClientResponseException) {
                        return new RuntimeException("OpenWeather API error: " + ex.getMessage(), ex);
                    }
                    return new RuntimeException("Failed to fetch weather data", ex);
                });
    }
}