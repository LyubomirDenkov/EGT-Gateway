package com.egt.gateway.service.ratescollector.rates;

import com.egt.gateway.model.CurrencyCode;
import com.egt.gateway.model.CurrencyData;
import com.egt.gateway.repository.currency.currencycode.CurrencyCodeRepository;
import com.egt.gateway.repository.currency.currencydata.CurrencyDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatesServiceImpl implements RatesService {

    private final CurrencyCodeRepository currencyCodeRepository;
    private final CurrencyDataRepository currencyDataRepository;

    @Override
    public void updateRatedValues(String baseCurrencyCode, String ratesJsonData) {
        Optional<CurrencyCode> baseCode = currencyCodeRepository.findByBaseCurrency(baseCurrencyCode);
        baseCode.ifPresent(c -> {
            currencyDataRepository.save(CurrencyData.builder()
                    .baseCurrency(c)
                    .updatedTime(LocalDateTime.now())
                    .currencyRates(ratesJsonData)
                    .build());
        });
    }
}
