package aimeter.telegram.bot.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AIMeterSubscription {

    public static final String SUBSCRIPTION_TYPE_NAME = "TELEGRAM";

    @Id
    long id;
    LocalDateTime dateCreated = LocalDateTime.now();
    LocalDateTime dateUpdated = LocalDateTime.now();
    String subscriptionType = SUBSCRIPTION_TYPE_NAME;
    
    Long meterId;
    String description;
    Long telegramChatId;

    public AIMeterSubscription(Long meterId, Long telegramChatId) {
        this.meterId = meterId;
        this.telegramChatId = telegramChatId;
    }
}
