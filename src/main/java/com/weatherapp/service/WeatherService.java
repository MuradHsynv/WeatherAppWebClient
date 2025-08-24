package com.weatherapp.service;

import com.weatherapp.client.OpenWeatherClient;
import com.weatherapp.dto.LocationRequest;
import com.weatherapp.dto.WeatherResponse;
import com.weatherapp.entity.WeatherData;
import com.weatherapp.mapper.OpenWeatherMapper;
import com.weatherapp.mapper.WeatherMapper;
import com.weatherapp.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;
    private final WeatherDataRepository weatherDataRepository;
    private final WeatherMapper weatherMapper;
    private final OpenWeatherMapper openWeatherMapper;

    public Mono<WeatherResponse> getWeatherDataReactive(LocationRequest locationRequest) {
        long startTime = System.currentTimeMillis();

        return openWeatherClient.getCurrentWeatherReactive(locationRequest.getLatitude(), locationRequest.getLongitude())
                .map(apiResponse -> {
                    long apiResponseTime = System.currentTimeMillis() - startTime;
                    return openWeatherMapper.apiResponseToEntity(apiResponse, apiResponseTime);
                })
                .flatMap(this::saveWeatherDataReactive)
                .map(weatherMapper::entityToResponse)
                .doOnSuccess(response -> log.info("Weather data processed successfully for location: {}, {} in {}ms",
                        locationRequest.getLatitude(), locationRequest.getLongitude(),
                        System.currentTimeMillis() - startTime))
                .onErrorMap(Exception.class, ex -> {
                    log.error("Error processing weather data for location: {}, {}",
                            locationRequest.getLatitude(), locationRequest.getLongitude(), ex);
                    return ex;
                });
    }
    private Mono<WeatherData> saveWeatherDataReactive(WeatherData weatherData) {
        return Mono.fromCallable(() -> weatherDataRepository.save(weatherData))
                .subscribeOn(Schedulers.boundedElastic());
    }
}