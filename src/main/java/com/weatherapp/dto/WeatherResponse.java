package com.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String cityName;
    private String country;
    private Double temperature;
    private Double feelsLike;
    private Integer humidity;
    private Integer pressure;
    private String weatherMain;
    private String weatherDescription;
    private Double windSpeed;
    private Integer windDirection;
    private LocalDateTime timestamp;
    private Long apiResponseTimeMs;
}