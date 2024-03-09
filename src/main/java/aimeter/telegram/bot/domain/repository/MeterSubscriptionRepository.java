package aimeter.telegram.bot.domain.repository;

import aimeter.telegram.bot.domain.entity.AIMeterSubscription;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterSubscriptionRepository extends CrudRepository<AIMeterSubscription, Long> {
    
    Optional<AIMeterSubscription> findByTelegramChatIdAndMeterId(long chatId, long meterId);
    
    List<AIMeterSubscription> findAIMeterSubscriptionByTelegramChatId(Long chatId);

    @Modifying
    @Query("""
        DELETE FROM ai_meter_subscription AS subscr
        WHERE subscr.meter_id = (
        SELECT device.id FROM ai_meter_device AS device
                 JOIN ai_meter_config meterConfig ON meterConfig.id = device.config_id
                 JOIN ai_meter_subscription meterSubsc ON device.id = meterSubsc.meter_id
        WHERE meterConfig.device_name = :meterName
        AND meterSubsc.telegram_chat_id = :chatId
        AND meterSubsc.subscription_type = 'TELEGRAM')
        AND subscr.subscription_type ='""" + AIMeterSubscription.SUBSCRIPTION_TYPE_NAME + "'")
    int deleteMeterSubscriptionByName(@Param("meterName") String meterName, @Param("chatId") long chatId);
}
