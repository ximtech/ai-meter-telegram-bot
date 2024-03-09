package aimeter.telegram.bot.domain.repository;

import aimeter.telegram.bot.domain.entity.AIMeterSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterSubscriptionRepository extends CrudRepository<AIMeterSubscription, Long> {
    
    Optional<AIMeterSubscription> findByTelegramChatIdAndMeterId(long chatId, long meterId);
    
    List<AIMeterSubscription> findAIMeterSubscriptionByTelegramChatId(Long chatId);
}
