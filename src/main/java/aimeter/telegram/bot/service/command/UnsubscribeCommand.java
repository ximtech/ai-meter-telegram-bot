package aimeter.telegram.bot.service.command;

import aimeter.telegram.bot.domain.repository.MeterSubscriptionRepository;
import aimeter.telegram.bot.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnsubscribeCommand implements Command {
    
    final MeterSubscriptionRepository meterSubscriptionRepository;

    @Override
    public String name() {
        return "/remove";
    }

    @Override
    public String description() {
        return "remove.command.description";
    }

    @Override
    public SendMessage apply(MessageHandler messageHandler, Update update) {
        long chatId = update.getMessage().getChatId();
        String meterName = StringUtils.substringAfter(update.getMessage().getText(), name());
        log.info("Meters unsubscribe started for chat id: [{}] and name: [{}]", chatId, meterName);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        
        if (StringUtils.isBlank(meterName)) {
            sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.meter.name.notfound", meterName));
            return sendMessage;
        }
        
        boolean isSubscriptionDeleted = meterSubscriptionRepository.deleteMeterSubscriptionByName(meterName.trim(), chatId) > 0;
        if (!isSubscriptionDeleted) {
            sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.meter.name.notfound", meterName));
            return sendMessage;
        }
        sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.meter.unsubscribed", meterName));
        return sendMessage;
    }
}
