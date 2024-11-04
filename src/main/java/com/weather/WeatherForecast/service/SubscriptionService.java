package com.weather.WeatherForecast.service;

import com.weather.WeatherForecast.model.Subscription;
import com.weather.WeatherForecast.model.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Управляет подписками, добавляет новые и отправляет уведомления по расписанию
 */
@Service
@Slf4j
public class SubscriptionService {
    private final WeatherService weatherService;
    private final BotService botService;
    private final List<Subscription> subscriptions = new ArrayList<>();

    public SubscriptionService(WeatherService weatherService, BotService botService) {
        this.weatherService = weatherService;
        this.botService = botService;
    }

    public void addSubscription(long chatId, String city) {
        subscriptions.removeIf(subscription -> subscription.getChatId() == chatId); //Удаляем старую запись
        subscriptions.add(new Subscription(chatId, city));
    }
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Scheduled(cron = "0 0 8,17 * * ?") // Время 8-00 и 17-00
    public void sendWeatherUpdates() {
        for (Subscription subscription : subscriptions) {
            String city = subscription.getCity();
            long chatId = subscription.getChatId();

            try {
                // Получаем погоду для города
                Weather weather = weatherService.getWeather(city);
                if (weather != null && weather.getMain() != null && weather.getWeatherWind() != null) {
                    // Формируем сообщение с погодой
                    String response = String.format("Погода в %s:\nТемпература: %s°C\nВлажность: %s%%\nСкорость ветра: %s м/с",
                            city,
                            weather.getMain().getTemp().intValue(),
                            weather.getMain().getHumidity().intValue(),
                            weather.getWeatherWind().getSpeed().intValue());

                    // Отправляем сообщение пользователю
                    botService.sendMessage(chatId, response);
                } else {
                    // Если погода не найдена, отправляем сообщение об ошибке
                    botService.sendMessage(chatId, "Извините, я не смог найти погоду для города " + city + ". Пожалуйста, проверьте название города.");
                }
            } catch (Exception e) {
                log.error("Ошибка при получении погоды для города {}: {}", city, e.getMessage());
                botService.sendMessage(chatId, "Произошла ошибка при получении данных о погоде для города " + city + ". Пожалуйста, попробуйте позже.");
            }
        }
    }
}
