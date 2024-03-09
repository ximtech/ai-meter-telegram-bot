package aimeter.telegram.bot.service.command;

import aimeter.telegram.bot.service.MessageHandler;
import aimeter.telegram.bot.service.callback.LanguageChooseCallback;
import aimeter.telegram.bot.utils.JsonHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.io.FileUtils.listFiles;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    @Value("classpath*:messages*.properties")
    List<Resource> resources;
    
    @Override
    public String name() {
        return "/start";
    }

    @Override
    public String description() {
        return "start.command.description";
    }

    @Override
    public SendMessage apply(MessageHandler messageHandler, Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Start command execution started for chat id: [{}]", chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.welcome"));
        addSelectKeyboard(sendMessage);
        return sendMessage;
    }
    
    private void addSelectKeyboard(SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        getAvailableLangNames().forEach((String langName) -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(langName.toUpperCase());
            String jsonCallback = JsonHandler.toJson(List.of(new LanguageChooseCallback().type(), langName));
            inlineKeyboardButton.setCallbackData(jsonCallback);
            keyboardButtonsRow.add(inlineKeyboardButton);
        });
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    private Set<String> getAvailableLangNames() {
        return resources.stream()
                .map((Resource resource) -> {
                    String langName = StringUtils.substringBetween(getResourceFileName(resource), "messages_", ".properties");
                    return StringUtils.isBlank(langName) ? MessageHandler.DEFAULT_LANG : langName.trim();
                }).collect(Collectors.toSet());
    }
    
    @SneakyThrows
    private String getResourceFileName(Resource resource) {
        return resource.getFile().getName();
    }
}
