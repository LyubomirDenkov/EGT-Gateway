package com.egt.gateway.repository.currency.currencydata;

import com.egt.gateway.model.CurrencyData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

@Repository
@RedisHash
public interface CurrencyDataRepository extends JpaRepository<CurrencyData, Long> {
    @Query("SELECT cd FROM CurrencyData cd WHERE cd.baseCurrency.code = :baseCurrency ORDER BY cd.updatedTime DESC limit 1")
    Optional<CurrencyData> findLatestByBaseCurrency(String baseCurrency);

    @Query("SELECT cd FROM CurrencyData cd WHERE cd.baseCurrency.code = :baseCurrency AND cd.updatedTime > :startPeriod ORDER BY cd.updatedTime DESC")
    List<CurrencyData> findByBaseCurrencyAndPeriod(String baseCurrency, LocalDateTime startPeriod);

}
