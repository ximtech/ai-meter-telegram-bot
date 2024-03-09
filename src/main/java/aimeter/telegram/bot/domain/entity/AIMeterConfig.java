package aimeter.telegram.bot.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class AIMeterConfig {

    @Id
    long id;
    String deviceName;
}
