package com.weather.WeatherForecast.service.impl;

import com.weather.WeatherForecast.model.Weather;
import com.weather.WeatherForecast.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Profile("production")
public class WeatherServiceProduction implements WeatherService {
    private static final String WEATHER_SERVICE_URL = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid={apikey}";
    private static final double KELVIN_TO_CELSIUS = 273.15;
    Logger logger = LoggerFactory.getLogger(WeatherServiceProduction.class);

    @Value("${weather.url}")
    private String weatherUrl;

    @Value("${weather.api-key}")
    private String weatherApiKey;

    @Autowired
    private RestTemplate restTemplate;

    public Weather getWeather(String city) {
        logger.debug("Requesting city for weather: {}", city);
        Weather weather = restTemplate.exchange(
                weatherUrl,
                HttpMethod.GET,
                new HttpEntity<>(HttpHeaders.EMPTY),
                Weather.class,
                city,
                weatherApiKey
        ).getBody();

        if (weather != null && weather.getMain() != null) {
            // Перевод из Кельвинов
            double tempInKelvin = weather.getMain().getTemp().doubleValue();
            double tempInCelsius = tempInKelvin - KELVIN_TO_CELSIUS;
            BigDecimal roundedTemp = BigDecimal.valueOf(tempInCelsius).setScale(0, RoundingMode.HALF_UP);
            weather.getMain().setTemp(roundedTemp);
        }

        if (weather != null && weather.getWeatherWind() != null) {
            // Округление
            BigDecimal roundedSpeed = weather.getWeatherWind().getSpeed().setScale(0, RoundingMode.HALF_UP);
            weather.getWeatherWind().setSpeed(roundedSpeed);
        }

        logger.debug("The weather for {} is {}", city, weather);
        return weather;
    }
}