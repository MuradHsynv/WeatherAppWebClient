package com.weatherapp.mapper;

import com.weatherapp.dto.OpenWeatherDto;
import com.weatherapp.entity.WeatherData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Coordinate;

@Mapper(componentModel = "spring")
public interface OpenWeatherMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "location", source = "response", qualifiedByName = "coordinatesToPoint")
    @Mapping(target = "cityName", source = "response.name")
    @Mapping(target = "country", source = "response.sys.country")
    @Mapping(target = "temperature", source = "response.main.temp")
    @Mapping(target = "feelsLike", source = "response.main.feelsLike")
    @Mapping(target = "humidity", source = "response.main.humidity")
    @Mapping(target = "pressure", source = "response.main.pressure")
    @Mapping(target = "weatherMain", source = "response", qualifiedByName = "extractWeatherMain")
    @Mapping(target = "weatherDescription", source = "response", qualifiedByName = "extractWeatherDescription")
    @Mapping(target = "windSpeed", source = "response.wind.speed")
    @Mapping(target = "windDirection", source = "response.wind.deg")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "apiResponseTimeMs", source = "apiResponseTimeMs")
    WeatherData apiResponseToEntity(OpenWeatherDto.Response response, Long apiResponseTimeMs);

    @Named("coordinatesToPoint")
    default Point coordinatesToPoint(OpenWeatherDto.Response response) {
        if (response.getCoord() == null) return null;
        GeometryFactory factory = new GeometryFactory();
        return factory.createPoint(new Coordinate(response.getCoord().getLon(), response.getCoord().getLat()));
    }

    @Named("extractWeatherMain")
    default String extractWeatherMain(OpenWeatherDto.Response response) {
        return response.getWeather() != null && !response.getWeather().isEmpty()
                ? response.getWeather().get(0).getMain() : null;
    }

    @Named("extractWeatherDescription")
    default String extractWeatherDescription(OpenWeatherDto.Response response) {
        return response.getWeather() != null && !response.getWeather().isEmpty()
                ? response.getWeather().get(0).getDescription() : null;
    }
}