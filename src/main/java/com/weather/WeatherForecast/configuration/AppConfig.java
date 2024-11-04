package com.weather.WeatherForecast.configuration;

import com.weather.WeatherForecast.service.BotService;
import com.weather.WeatherForecast.service.SubscriptionService;
import com.weather.WeatherForecast.service.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AppConfig {
    @Bean
    public BotService botService(BotConfig botConfig, WeatherService weatherService, @Lazy SubscriptionService subscriptionService) {
        return new BotService(botConfig, weatherService, subscriptionService);
    }

    @Bean
    public SubscriptionService subscriptionService(WeatherService weatherService, @Lazy BotService botService) {
        return new SubscriptionService(weatherService, botService);
    }
}
