package aimeter.telegram.bot;

import aimeter.telegram.bot.domain.repository.OTPTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Slf4j
@EnableCaching
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class AIMeterTelegramBotApplication {
    
    final OTPTokenRepository otpTokenRepository;

    public static void main(String[] args) {
        SpringApplication.run(AIMeterTelegramBotApplication.class, args);
    }

    @Scheduled(cron = "0 */10 * ? * *")
    public void deleteExpiredOTPsEveryTenMinutes() {
        log.info("Delete expired OTP started at: {}", LocalDateTime.now());
        long deletedTokensCount = otpTokenRepository.deleteAllExpiredTokens(LocalDateTime.now());
        log.info("Total deleted tokens: {}", deletedTokensCount);
    }

}
