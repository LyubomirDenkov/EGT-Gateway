package com.egt.gateway.repository.currency.currencycode;

import com.egt.gateway.model.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyCodeRepository extends JpaRepository<CurrencyCode, Long> {

    @Query("SELECT COUNT(cd) FROM CurrencyCode cd")
    Long countAll();

    @Query("SELECT cc FROM CurrencyCode cc WHERE cc.code = :baseCurrency")
    Optional<CurrencyCode> findByBaseCurrency(String baseCurrency);
}
