package aimeter.telegram.bot.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static aimeter.telegram.bot.utils.MessageConstants.HELP_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    @Override
    public String name() {
        return "/help";
    }

    @Override
    public SendMessage apply(Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Meters command help for chat id: [{}]", chatId);
        return new SendMessage(String.valueOf(chatId), HELP_MESSAGE);
    }
}
