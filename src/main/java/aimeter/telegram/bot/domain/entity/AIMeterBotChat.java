package aimeter.telegram.bot.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AIMeterBotChat {

    public static final String CHAT_TYPE_NAME = "TELEGRAM";

    @Id
    long id;
    Long chatId;
    String userLanguage;
    String chatType = CHAT_TYPE_NAME;
    LocalDateTime dateCreated = LocalDateTime.now();

    public AIMeterBotChat(Long chatId, String userLanguage) {
        this.chatId = chatId;
        this.userLanguage = userLanguage;
    }
}
