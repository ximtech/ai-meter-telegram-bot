package aimeter.telegram.bot.service.callback;

import aimeter.telegram.bot.service.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Callback {

    String type();
    
    SendMessage apply(MessageHandler messageHandler, String data, Update update);
}
