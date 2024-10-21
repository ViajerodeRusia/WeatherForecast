package com.weather.WeatherForecast.model;


import java.math.BigDecimal;

public class WeatherMain {
    private BigDecimal temp;
    private BigDecimal humidity;

    public WeatherMain() {
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WeatherMain{");
        sb.append("temp=").append(temp);
        sb.append(", humidity=").append(humidity);
        sb.append('}');
        return sb.toString();
    }
}
