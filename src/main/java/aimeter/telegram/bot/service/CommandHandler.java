package aimeter.telegram.bot.service;

import aimeter.telegram.bot.service.command.Command;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class CommandHandler {

    final MessageHandler messageHandler;
    final Map<String, Command> commands;

    public CommandHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.commands = messageHandler.getCommands().stream()
                .collect(Collectors.toMap(Command::name, Function.identity()));
    }

    public SendMessage handleCommands(Update update) {
        String messageText = update.getMessage().getText();
        String command = messageText.split(" ")[0];

        long chatId = update.getMessage().getChatId();
        Command commandHandler = commands.get(command);
        if (commandHandler != null) {
            return commandHandler.apply(messageHandler, update);
        }
        return new SendMessage(String.valueOf(chatId), messageHandler.getMessage(chatId, "bot.answer.unknown.command"));
    }

}
