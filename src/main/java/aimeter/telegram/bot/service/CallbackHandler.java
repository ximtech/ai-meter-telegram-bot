package aimeter.telegram.bot.service;

import aimeter.telegram.bot.service.callback.Callback;
import aimeter.telegram.bot.utils.JsonHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class CallbackHandler {

    final MessageHandler messageHandler;
    final Map<String, Callback> callbacks;

    public CallbackHandler(MessageHandler messageHandler, List<Callback> callbacks) {
        this.messageHandler = messageHandler;
        this.callbacks = callbacks.stream()
                .collect(Collectors.toMap(Callback::type, Function.identity()));
    }

    public SendMessage handleCallbacks(Update update) {
        List<String> callbackDataList = JsonHandler.toList(update.getCallbackQuery().getData());
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        
        if (CollectionUtils.isEmpty(callbackDataList)) {
            return new SendMessage(String.valueOf(chatId), messageHandler.getMessage(chatId, "bot.answer.error"));
        }
        return callbacks.get(callbackDataList.get(0)).apply(messageHandler, callbackDataList.get(1), update);
    }
}
