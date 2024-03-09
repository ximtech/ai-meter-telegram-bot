package aimeter.telegram.bot.service.command;

import aimeter.telegram.bot.service.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    String name();
    
    String description();
    
    SendMessage apply(MessageHandler messageHandler, Update update);
    
}
