package com.weather.WeatherForecast.service.impl;

import com.weather.WeatherForecast.model.Weather;
import com.weather.WeatherForecast.model.WeatherMain;
import com.weather.WeatherForecast.model.WeatherWind;
import com.weather.WeatherForecast.service.WeatherService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("!production")
public class WeatherServiceTest implements WeatherService {
    @Override
    public Weather getWeather(String city) {
        Weather weather = new Weather();
        WeatherMain weatherMain = new WeatherMain();
        weatherMain.setTemp(BigDecimal.ONE);
        weatherMain.setHumidity(BigDecimal.ONE);
        WeatherWind weatherWind = new WeatherWind();
        weatherWind.setDeg(1);
        weatherWind.setSpeed(BigDecimal.ONE);
        weather.setMain(weatherMain);
        weather.setWeatherWind(weatherWind);
        return weather;
    }
}
