package com.weather.WeatherForecast.model;

import java.math.BigDecimal;

public class WeatherWind {
    private BigDecimal speed;
    private Integer deg;

    public WeatherWind() {
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WeatherWind{");
        sb.append("speed=").append(speed);
        sb.append(", deg=").append(deg);
        sb.append('}');
        return sb.toString();
    }
}
