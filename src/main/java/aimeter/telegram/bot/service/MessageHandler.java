package aimeter.telegram.bot.service;

import aimeter.telegram.bot.domain.entity.AIMeterBotChat;
import aimeter.telegram.bot.domain.repository.BotChatRepository;
import aimeter.telegram.bot.service.command.Command;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Component
@RequiredArgsConstructor
public class MessageHandler {

    public static final String DEFAULT_LANG = "en";
    
    final List<Command> commands;
    final BotChatRepository botChatRepository;
    final ResourceBundleMessageSource messageSource;
    
    public Locale getUserLocale(long chatId) {
        return botChatRepository.findUserSelectedLanguage(chatId)
                .map(Locale::of)
                .orElse(Locale.ENGLISH);
    }

    @CacheEvict(value = "userLang", key = "#chatId")
    public void saveOrUpdateUserSelectedLang(long chatId, String lang) {
        Optional<AIMeterBotChat> botChatOptional = botChatRepository.findAIMeterBotChatByChatId(chatId);
        if (botChatOptional.isPresent()) {
            botChatRepository.updateUserLanguage(chatId, lang);
            return;
        }
        AIMeterBotChat botChat = new AIMeterBotChat(chatId, lang);
        botChatRepository.save(botChat);
    }
    
    public String getMessage(long chatId, String code, Object... args) {
        return messageSource.getMessage(code, args, getUserLocale(chatId));
    }
    
    public String formatHelpMessage(long chatId) {
        Locale userLang = getUserLocale(chatId);
        return commands.stream()
                .map((Command command) -> "%s - %s".formatted(command.name(), messageSource.getMessage(command.description(), null, userLang)))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
