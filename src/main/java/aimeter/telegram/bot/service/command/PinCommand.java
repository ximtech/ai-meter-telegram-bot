package aimeter.telegram.bot.service.command;

import aimeter.telegram.bot.domain.entity.AIMeterConfig;
import aimeter.telegram.bot.domain.entity.AIMeterSubscription;
import aimeter.telegram.bot.domain.entity.AIMeterToken;
import aimeter.telegram.bot.domain.repository.MeterConfigRepository;
import aimeter.telegram.bot.domain.repository.MeterSubscriptionRepository;
import aimeter.telegram.bot.domain.repository.OTPTokenRepository;
import aimeter.telegram.bot.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PinCommand implements Command {

    public static final int REQUIRED_PIN_NUMBER_LENGTH = 4;
    
    final OTPTokenRepository otpTokenRepository;
    final MeterConfigRepository meterConfigRepository;
    final MeterSubscriptionRepository meterSubscriptionRepository;

    @Override
    public String name() {
        return "/pin";
    }

    @Override
    public String description() {
        return "pin.command.description";
    }

    @Override
    @Transactional
    public SendMessage apply(MessageHandler messageHandler, Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Pin command started for chat id: [{}]", chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        
        String pinString = StringUtils.substringAfter(update.getMessage().getText(), name()).trim();
        log.info("PIN entered: [{}]", pinString);
        Integer pinNumber = parsePinNumber(pinString);
        if (pinNumber == null) {
            log.warn("Invalid pin number entered");
            sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.pin.invalid"));
            return sendMessage;
        }

       Optional<AIMeterToken> meterTokenOptional = otpTokenRepository.findAIMeterOTP(pinNumber);
        if (meterTokenOptional.isEmpty()) {
            log.warn("Not existing OTP entered");
            sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.pin.notfound"));
            return sendMessage;
        }
        
        AIMeterToken meterToken = meterTokenOptional.get();
        Optional<AIMeterSubscription> meterSubscriptionOptional = meterSubscriptionRepository.findByTelegramChatIdAndMeterId(chatId, meterToken.getMeterId());
        if (meterSubscriptionOptional.isPresent()) {
            AIMeterConfig meterConfig = meterConfigRepository.findMeterConfigByMeterId(meterSubscriptionOptional.get().getMeterId());
            log.warn("Meter already subscribed: {}", meterConfig.getDeviceName());
            sendMessage.enableHtml(true);
            sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.already.subscribed", meterConfig.getDeviceName()));
            return sendMessage;
        }
        
        if (meterToken.getExpiresIn().isBefore(LocalDateTime.now())) {
            log.warn("Expired OTP entered");
            otpTokenRepository.delete(meterToken);
            sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.pin.expired"));
            return sendMessage;
        }
        
        AIMeterSubscription meterSubscription = new AIMeterSubscription(meterToken.getMeterId(), chatId);
        meterSubscriptionRepository.save(meterSubscription);
        otpTokenRepository.delete(meterToken);

        AIMeterConfig meterConfig = meterConfigRepository.findMeterConfigByMeterId(meterToken.getMeterId());
        sendMessage.enableHtml(true);
        sendMessage.setText(messageHandler.getMessage(chatId, "bot.answer.meter.subscribed", meterConfig.getDeviceName()));
        log.info("Subscription successfully created for meter: [{}]", meterConfig.getDeviceName());
        return sendMessage;
    }
    
    private Integer parsePinNumber(String pinNumber) {
        if (StringUtils.isBlank(pinNumber) || pinNumber.length() != REQUIRED_PIN_NUMBER_LENGTH) {
            return null;
        }
        
        try {
            return Integer.parseInt(pinNumber);
        } catch (NumberFormatException ignore) {
            return null;
        }
    }
}
