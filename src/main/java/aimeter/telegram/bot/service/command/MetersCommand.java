package aimeter.telegram.bot.service.command;

import aimeter.telegram.bot.domain.entity.AIMeterConfig;
import aimeter.telegram.bot.domain.entity.AIMeterSubscription;
import aimeter.telegram.bot.domain.repository.MeterConfigRepository;
import aimeter.telegram.bot.domain.repository.MeterSubscriptionRepository;
import aimeter.telegram.bot.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetersCommand implements Command {
    
    final MeterSubscriptionRepository meterSubscriptionRepository;
    final MeterConfigRepository meterConfigRepository;

    @Override
    public String name() {
        return "/meters";
    }

    @Override
    public String description() {
        return "meter.command.description";
    }

    @Override
    public SendMessage apply(MessageHandler messageHandler, Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Meters command started for chat id: [{}]", chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);

        List<Long> subscriptionIds = meterSubscriptionRepository.findAIMeterSubscriptionByTelegramChatId(chatId).stream()
                .map((AIMeterSubscription::getMeterId)).toList();
        if (CollectionUtils.isEmpty(subscriptionIds)) {
            sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.no.meters.subscribed"));
            return sendMessage;
        }

        AtomicInteger counter = new AtomicInteger(1);
        String meterNamesFormatted = meterConfigRepository.findConfigsByMeterIds(subscriptionIds).stream()
                .map((AIMeterConfig deviceConfig) -> "%d. <b>%s</b>%n".formatted(counter.getAndIncrement(), deviceConfig.getDeviceName()))
                .collect(Collectors.joining());
        sendMessage.setText(meterNamesFormatted);
        return sendMessage;
    }
}
