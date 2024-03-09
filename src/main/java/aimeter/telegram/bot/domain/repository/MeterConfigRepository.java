package aimeter.telegram.bot.domain.repository;

import aimeter.telegram.bot.domain.entity.AIMeterConfig;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeterConfigRepository extends CrudRepository<AIMeterConfig, Long> {
    
    @Query("""
        SELECT * FROM ai_meter_config AS config
        JOIN ai_meter_device AS device ON config.id = device.config_id
        AND device.id = :id
        AND device.deleted IS FALSE""")
    AIMeterConfig findMeterConfigByMeterId(@Param("id") long meterId);

    @Query("""
        SELECT * FROM ai_meter_config AS config
        JOIN ai_meter_device AS device ON config.id = device.config_id
        AND device.id IN (:meterIds)
        AND device.deleted IS FALSE""")
    List<AIMeterConfig> findConfigsByMeterIds(@Param("meterIds") List<Long> meterIds);
}
