package com.weather.WeatherForecast.service;

import com.weather.WeatherForecast.configuration.BotConfig;
import com.weather.WeatherForecast.model.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class BotService extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final WeatherService weatherService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public BotService(BotConfig botConfig, WeatherService weatherService, SubscriptionService subscriptionService) {
        this.botConfig = botConfig;
        this.weatherService = weatherService;
        this.subscriptionService = subscriptionService;
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
        String greetingMessage = "Привет, " + name + "! Добро пожаловать в наш бот прогноза погоды! " +
                "Введите название города, чтобы получить прогноз.";
        sendMessage(chatId, greetingMessage);
    }

    public void handleTextMessage(long chatId, String messageText) {
        String city = messageText.trim(); // Убираем лишние пробелы

        if (!city.isEmpty()) {
            subscriptionService.addSubscription(chatId, city); // Добавляем подписку
            sendMessage(chatId, "Вы подписаны на уведомления о погоде в " + city + ". Уведомления будут отправляться дважды в день.");
        } else {
            sendMessage(chatId, "Пожалуйста, укажите город для подписки.");
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