package com.eprovement.poptavka.shared.exceptions;

import java.io.Serializable;

public class RPCException extends Exception implements Serializable {
    private String symbol;

    private Throwable cause;

    public RPCException() {
    }

    public RPCException(String symbol) {
        this.symbol = symbol;
    }

    public RPCException(String symbol, Throwable cause) {
        this.symbol = symbol;
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
