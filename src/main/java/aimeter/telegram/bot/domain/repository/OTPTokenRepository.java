package aimeter.telegram.bot.domain.repository;

import aimeter.telegram.bot.domain.entity.AIMeterToken;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OTPTokenRepository extends CrudRepository<AIMeterToken, Long> {
    
    @Query("""
        SELECT * FROM ai_meter_token AS token
        WHERE CAST(token.access_token AS INTEGER) = :otpPin
        AND token.token_type ='""" + AIMeterToken.TOKEN_TYPE + "'")
    Optional<AIMeterToken> findAIMeterOTP(@Param("otpPin") Integer otpPin);
    
    @Modifying
    @Query("""
        DELETE FROM ai_meter_token AS token
        WHERE token.expires_in <= :date
        AND token.token_type ='""" + AIMeterToken.TOKEN_TYPE + "'")
    long deleteAllExpiredTokens(@Param("date") LocalDateTime fromDateTime);
}
