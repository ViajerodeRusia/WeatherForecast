package com.weather.WeatherForecast.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Хранит информацию о подписках (ID чата и город)
 */

@Data
@AllArgsConstructor
public class Subscription {
    private long chatId;
    private String city;
}
