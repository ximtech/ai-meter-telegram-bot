package aimeter.telegram.bot.service;

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
    final CommandHandler commandHandler;
    final CallbackHandler callbackHandler;

    public TelegramBot(String botName, String botToken, CommandHandler commandHandler, CallbackHandler callbackHandler) {
        super(botToken);
        this.botName = botName;
        this.commandHandler = commandHandler;
        this.callbackHandler = callbackHandler;
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
                sendMessage(commandHandler.handleCommands(update));
                return;
            }
            String errorMessage = commandHandler.getMessageHandler().getMessage(update.getMessage().getChatId(), "bot.answer.cant.understand");
            sendMessage(new SendMessage(chatId, errorMessage));

        } else if (update.hasCallbackQuery()) {
            sendMessage(callbackHandler.handleCallbacks(update));
        }
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
