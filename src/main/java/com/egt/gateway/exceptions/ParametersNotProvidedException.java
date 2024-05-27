package com.egt.gateway.exceptions;

public class ParametersNotProvidedException extends RuntimeException {
    public static final String PARAMETER_MISSING = "Parameter missing";

    public ParametersNotProvidedException() {
    }

    public ParametersNotProvidedException(String message) {
        super(message);
    }
}
