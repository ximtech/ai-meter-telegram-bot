package aimeter.telegram.bot.service;

import aimeter.telegram.bot.utils.MessageConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Getter
public class TelegramBot extends TelegramLongPollingBot {

    final String botName;
    final CommandsHandler commandsHandler;
    final CallbackHandler callbacksHandler;

    public TelegramBot(String botName, 
                       String botToken, 
                       CommandsHandler commandsHandler, 
                       CallbackHandler callbacksHandler) {
        super(botToken);
        this.botName = botName;
        this.commandsHandler = commandsHandler;
        this.callbacksHandler = callbacksHandler;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            
            if (update.getMessage().getText().startsWith("/")) {
                sendMessage(commandsHandler.handleCommands(update));
            } else {
                sendMessage(new SendMessage(chatId, MessageConstants.CANT_UNDERSTAND));
            }
            
        } else if (update.hasCallbackQuery()) {
            sendMessage(callbacksHandler.handleCallbacks(update));
        }
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            log.info("Reply successfully sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
