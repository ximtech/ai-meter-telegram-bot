package aimeter.telegram.bot.service.command;

import aimeter.telegram.bot.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    @Override
    public String name() {
        return "/help";
    }

    @Override
    public String description() {
        return "help.command.description";
    }

    @Override
    public SendMessage apply(MessageHandler messageHandler, Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Meters command help for chat id: [{}]", chatId);
        return new SendMessage(String.valueOf(chatId), messageHandler.formatHelpMessage(chatId));
    }
}
