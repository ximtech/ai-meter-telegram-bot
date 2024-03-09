package aimeter.telegram.bot.service.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    String name();
    
    SendMessage apply(Update update);
    
}
