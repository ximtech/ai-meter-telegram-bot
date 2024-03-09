package aimeter.telegram.bot.service.callback;

import aimeter.telegram.bot.service.MessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class LanguageChooseCallback implements Callback {
    
    @Override
    public String type() {
        return "LANG_CHOOSE";
    }

    @Override
    public SendMessage apply(MessageHandler messageHandler, String data, Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        messageHandler.saveOrUpdateUserSelectedLang(chatId, data);
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);
        answer.setText(messageHandler.getMessage(chatId, "bot.answer.lang.saved"));
        return answer;
    }
}
