package com.weather.WeatherForecast.service;

import com.weather.WeatherForecast.model.Weather;

public interface WeatherService {
    Weather getWeather(String city);
}
