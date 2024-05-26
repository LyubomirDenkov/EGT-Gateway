package com.egt.gateway.service.ratescollector.currencycodes;

import com.egt.gateway.model.CurrencyCode;
import com.egt.gateway.repository.currency.currencycode.CurrencyCodeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyCodeInitServiceImpl implements CurrencyCodeInitService {

    private final CurrencyCodeRepository currencyCodeRepository;

    @Override
    public void fillCurrencyCodeData(List<String> currencyCodes) {
        currencyCodes.forEach(currencyCode -> {
            Optional<CurrencyCode> codeDB = currencyCodeRepository.findByBaseCurrency(currencyCode);
            if (!codeDB.isPresent()) {
                currencyCodeRepository.save(CurrencyCode.builder()
                        .code(currencyCode)
                        .build());
            }
        });
    }
}
