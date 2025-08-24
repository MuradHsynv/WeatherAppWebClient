package com.weatherapp.mapper;

import com.weatherapp.dto.WeatherResponse;
import com.weatherapp.entity.WeatherData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.locationtech.jts.geom.Point;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(target = "latitude", source = "location", qualifiedByName = "pointToLatitude")
    @Mapping(target = "longitude", source = "location", qualifiedByName = "pointToLongitude")
    @Mapping(target = "timestamp", source = "createdAt")
    WeatherResponse entityToResponse(WeatherData weatherData);

    @Named("pointToLatitude")
    default Double pointToLatitude(Point point) {
        return point != null ? point.getY() : null;
    }

    @Named("pointToLongitude")
    default Double pointToLongitude(Point point) {
        return point != null ? point.getX() : null;
    }
}