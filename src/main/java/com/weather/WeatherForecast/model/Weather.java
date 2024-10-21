package com.weather.WeatherForecast.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
public class Weather {
    @JsonProperty("main")
    private  WeatherMain main;
    @JsonProperty("wind")
    private WeatherWind weatherWind;

    public Weather() {
    }

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }

    public WeatherWind getWeatherWind() {
        return weatherWind;
    }

    public void setWeatherWind(WeatherWind weatherWind) {
        this.weatherWind = weatherWind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Objects.equals(main, weather.main) && Objects.equals(weatherWind, weather.weatherWind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(main, weatherWind);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Weather{");
        sb.append("main=").append(main);
        sb.append(", weatherWind=").append(weatherWind);
        sb.append('}');
        return sb.toString();
    }
}
