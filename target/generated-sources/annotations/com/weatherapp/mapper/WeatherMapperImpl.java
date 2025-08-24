package com.weatherapp.mapper;

import com.weatherapp.dto.WeatherResponse;
import com.weatherapp.entity.WeatherData;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-09T17:08:30+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class WeatherMapperImpl implements WeatherMapper {

    @Override
    public WeatherResponse entityToResponse(WeatherData weatherData) {
        if ( weatherData == null ) {
            return null;
        }

        WeatherResponse.WeatherResponseBuilder weatherResponse = WeatherResponse.builder();

        weatherResponse.latitude( pointToLatitude( weatherData.getLocation() ) );
        weatherResponse.longitude( pointToLongitude( weatherData.getLocation() ) );
        weatherResponse.timestamp( weatherData.getCreatedAt() );
        weatherResponse.id( weatherData.getId() );
        weatherResponse.cityName( weatherData.getCityName() );
        weatherResponse.country( weatherData.getCountry() );
        weatherResponse.temperature( weatherData.getTemperature() );
        weatherResponse.feelsLike( weatherData.getFeelsLike() );
        weatherResponse.humidity( weatherData.getHumidity() );
        weatherResponse.pressure( weatherData.getPressure() );
        weatherResponse.weatherMain( weatherData.getWeatherMain() );
        weatherResponse.weatherDescription( weatherData.getWeatherDescription() );
        weatherResponse.windSpeed( weatherData.getWindSpeed() );
        weatherResponse.windDirection( weatherData.getWindDirection() );
        weatherResponse.apiResponseTimeMs( weatherData.getApiResponseTimeMs() );

        return weatherResponse.build();
    }
}
