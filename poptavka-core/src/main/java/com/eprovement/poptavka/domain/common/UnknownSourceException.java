package com.eprovement.poptavka.domain.common;

public class UnknownSourceException extends RuntimeException {
    public UnknownSourceException(String sourceName) {
        super("Source '" + sourceName + "' does not exist!");
    }
}
