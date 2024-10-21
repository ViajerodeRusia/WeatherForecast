package com.weather.WeatherForecast.service;

import com.weather.WeatherForecast.configuration.BotConfig;
import com.weather.WeatherForecast.model.Weather;
import com.weather.WeatherForecast.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class BotService extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final WeatherService weatherService;

    @Autowired
    public BotService(BotConfig botConfig, WeatherService weatherService) {
        this.botConfig = botConfig;
        this.weatherService = weatherService;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            if (messageText.equals("/start")) {
                startCommandReceived(chatId, userName);
            } else {
                handleTextMessage(chatId, messageText);
            }
        }
    }

    public void startCommandReceived(long chatId, String name) {
        String greetingMessage = "Привет, " + name + "! Добро пожаловать в наш бот прогноза погоды! Введите название города, чтобы получить прогноз.";
        sendMessage(chatId, greetingMessage);
    }

    public void handleTextMessage(long chatId, String city) {
        try {
            Weather weather = weatherService.getWeather(city);
            if (weather != null && weather.getMain() != null && weather.getWeatherWind() != null) {
                String response = String.format("Погода в %s:\nТемпература: %s°C\nВлажность: %s%%\nСкорость ветра: %s м/с",
                        city,
                        weather.getMain().getTemp().intValue(),
                        weather.getMain().getHumidity().intValue(),
                        weather.getWeatherWind().getSpeed().intValue());
                sendMessage(chatId, response);
            } else {
                sendMessage(chatId, "Извините, я не смог найти погоду для этого города. Пожалуйста, попробуйте еще раз.");
            }
        } catch (Exception e) {
            log.error("Ошибка при получении погоды: " + e.getMessage(), e);
            sendMessage(chatId, "Произошла ошибка при получении данных о погоде. Пожалуйста, попробуйте позже.");
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Отправлено сообщение: \"" + textToSend + "\" в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: " + e.getMessage(), e);
        }
    }
}