package aimeter.telegram.bot.domain.repository;

import aimeter.telegram.bot.domain.entity.AIMeterBotChat;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotChatRepository extends CrudRepository<AIMeterBotChat, Long> {

    @Cacheable(value = "userLang", key = "#chatId")
    @Query("""
        SELECT botChat.user_language FROM ai_meter_bot_chat AS botChat
        WHERE botChat.chat_id = :chatId""")
    Optional<String> findUserSelectedLanguage(@Param("chatId") long chatId);
    
    Optional<AIMeterBotChat> findAIMeterBotChatByChatId(long chatId);
    
    @Modifying
    @Query("""
        UPDATE ai_meter_bot_chat
        SET user_language = :lang
        WHERE chat_id = :chatId""")
    void updateUserLanguage(@Param("chatId") long chatId, @Param("lang") String lang);
}
