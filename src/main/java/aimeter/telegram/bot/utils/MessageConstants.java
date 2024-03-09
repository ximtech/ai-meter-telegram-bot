package aimeter.telegram.bot.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstants {
    
    public static final String UNKNOWN_COMMAND = "Sorry, I don't know this command";
    public static final String CANT_UNDERSTAND = "Sorry, I don't understand";
    public static final String ERROR = "Something goes wrong";
    
    public static final String START_MESSAGE = """
                    Hello! Lets subscribe your AI-Meter.
                    Please enter the PIN code in the next message, adding /pin at the beginning (example: /pin 1234).
                    Or enter /help to see available commands.
                    """;

    public static final String HELP_MESSAGE = """
                    /start - Start using bot and prepare to setup AI-Meter
                    /pin - Enter PIN number from meter subscription page to subscribe new device
                    /help - Display commands short description
                    /meters - Show all meter that is subscribed to this channel
                    """;

    public static final String RESTART_MESSAGE = "Please type /start to subscribe next meter. Thank you for using the bot.";

    public static final String SUBSCRIPTION_SUCCESS_MESSAGE = "Thank you, <b>%s</b> has been subscribed! " + RESTART_MESSAGE;
    public static final String INVALID_PIN_ENTERED = "Sorry, but you entered incorrect PIN, please try again";
    public static final String PIN_NOT_FOUND = "Sorry, but you entered not existing PIN";
    public static final String METER_ALREADY_SUBSCRIBED = "<b>%s</b> already subscribed to this channel";
    public static final String PIN_EXPIRED = "Sorry, but PIN number is expired. Please create new one by refreshing subscription page";
    
    public static final String NO_METERS_ALREADY_SUBSCRIBED = "There is no subscribed meters to this channel";
    
}
