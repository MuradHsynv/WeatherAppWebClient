package com.weatherapp.controller;

import com.weatherapp.dto.LocationRequest;
import com.weatherapp.dto.MetricsResponse;
import com.weatherapp.dto.WeatherResponse;
import com.weatherapp.service.MetricsService;
import com.weatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final MetricsService metricsService;

    @PostMapping("/weather")
    public Mono<WeatherResponse> getWeather(@Valid @RequestBody LocationRequest locationRequest) {
        log.info("Received weather request for location: {}, {}",
                locationRequest.getLatitude(), locationRequest.getLongitude());

        return weatherService.getWeatherDataReactive(locationRequest)
                .doOnSuccess(response -> log.info("Weather data retrieved successfully for ID: {}", response.getId()))
                .doOnError(error -> log.error("Error retrieving weather data"));
    }

    @GetMapping("/metrics")
    public Mono<MetricsResponse> getMetrics() {
        log.info("Metrics request");
        return metricsService.getApplicationMetricsReactive()
                .doOnError(error -> log.error("Error retrieving metrics", error));
    }
}