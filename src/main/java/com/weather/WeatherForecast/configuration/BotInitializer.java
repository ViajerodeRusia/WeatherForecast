package com.weather.WeatherForecast.configuration;

import com.weather.WeatherForecast.service.BotService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class BotInitializer {
    private final BotService botService;

    @Autowired
    public BotInitializer(BotService botService) {
        this.botService = botService;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(botService);
            log.info("Бот успешно инициализирован.");
        } catch (TelegramApiException e) {
            log.error("Ошибка при инициализации бота: " + e.getMessage(), e);
        }
    }
}
