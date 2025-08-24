package com.weatherapp.mapper;

import com.weatherapp.dto.OpenWeatherDto;
import com.weatherapp.entity.WeatherData;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-10T18:50:07+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class OpenWeatherMapperImpl implements OpenWeatherMapper {

    @Override
    public WeatherData apiResponseToEntity(OpenWeatherDto.Response response, Long apiResponseTimeMs) {
        if ( response == null && apiResponseTimeMs == null ) {
            return null;
        }

        WeatherData.WeatherDataBuilder weatherData = WeatherData.builder();

        if ( response != null ) {
            weatherData.location( coordinatesToPoint( response ) );
            weatherData.cityName( response.getName() );
            weatherData.country( responseSysCountry( response ) );
            weatherData.temperature( responseMainTemp( response ) );
            weatherData.feelsLike( responseMainFeelsLike( response ) );
            weatherData.humidity( responseMainHumidity( response ) );
            weatherData.pressure( responseMainPressure( response ) );
            weatherData.weatherMain( extractWeatherMain( response ) );
            weatherData.weatherDescription( extractWeatherDescription( response ) );
            weatherData.windSpeed( responseWindSpeed( response ) );
            weatherData.windDirection( responseWindDeg( response ) );
        }
        weatherData.apiResponseTimeMs( apiResponseTimeMs );
        weatherData.createdAt( java.time.LocalDateTime.now() );

        return weatherData.build();
    }

    private String responseSysCountry(OpenWeatherDto.Response response) {
        OpenWeatherDto.Sys sys = response.getSys();
        if ( sys == null ) {
            return null;
        }
        return sys.getCountry();
    }

    private Double responseMainTemp(OpenWeatherDto.Response response) {
        OpenWeatherDto.Main main = response.getMain();
        if ( main == null ) {
            return null;
        }
        return main.getTemp();
    }

    private Double responseMainFeelsLike(OpenWeatherDto.Response response) {
        OpenWeatherDto.Main main = response.getMain();
        if ( main == null ) {
            return null;
        }
        return main.getFeelsLike();
    }

    private Integer responseMainHumidity(OpenWeatherDto.Response response) {
        OpenWeatherDto.Main main = response.getMain();
        if ( main == null ) {
            return null;
        }
        return main.getHumidity();
    }

    private Integer responseMainPressure(OpenWeatherDto.Response response) {
        OpenWeatherDto.Main main = response.getMain();
        if ( main == null ) {
            return null;
        }
        return main.getPressure();
    }

    private Double responseWindSpeed(OpenWeatherDto.Response response) {
        OpenWeatherDto.Wind wind = response.getWind();
        if ( wind == null ) {
            return null;
        }
        return wind.getSpeed();
    }

    private Integer responseWindDeg(OpenWeatherDto.Response response) {
        OpenWeatherDto.Wind wind = response.getWind();
        if ( wind == null ) {
            return null;
        }
        return wind.getDeg();
    }
}
