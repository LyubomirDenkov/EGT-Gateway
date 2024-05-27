package com.egt.gateway.service.currency.util;

import com.egt.gateway.exceptions.ParametersNotProvidedException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UtilServiceImpl implements UtilService {

    @Override
    public void validateDataIsPresent(Object data) throws ParametersNotProvidedException {
        if (ObjectUtils.isEmpty(data)) {
            throw new ParametersNotProvidedException(ParametersNotProvidedException.PARAMETER_MISSING);
        }
    }
}
