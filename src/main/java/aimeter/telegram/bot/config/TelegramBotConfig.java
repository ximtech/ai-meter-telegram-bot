package aimeter.telegram.bot.config;

import aimeter.telegram.bot.service.CommandsHandler;
import aimeter.telegram.bot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Value("${telegram.bot.name}")
    String telegramBotName;

    @Value("${telegram.bot.token}")
    String telegramBotToken;
    
    @Bean
    public TelegramBot telegramBot(CommandsHandler commandsHandler) {
        return new TelegramBot(telegramBotName, telegramBotToken, commandsHandler, null);
    }

}
