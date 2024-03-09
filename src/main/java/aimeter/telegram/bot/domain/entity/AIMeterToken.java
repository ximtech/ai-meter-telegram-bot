package aimeter.telegram.bot.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
public class AIMeterToken {

    public static final String TOKEN_TYPE = "TELEGRAM_OTP";
    
    @Id
    long id;
    Long meterId;
    Integer accessToken;
    LocalDateTime expiresIn;

}
