package aimeter.telegram.bot.service.callback;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Callback {

    String type();
    
    SendMessage apply(String data, Update update);
}
