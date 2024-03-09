package aimeter.telegram.bot.service.command;

import aimeter.telegram.bot.domain.repository.MeterSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static aimeter.telegram.bot.utils.MessageConstants.METER_ALREADY_SUBSCRIBED;
import static aimeter.telegram.bot.utils.MessageConstants.START_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    final MeterSubscriptionRepository meterSubscriptionRepository;

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public SendMessage apply(Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Start command execution started for chat id: [{}]", chatId);
        return new SendMessage(String.valueOf(chatId), START_MESSAGE);
    }
}
