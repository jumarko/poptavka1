package com.eprovement.poptavka.shared.exceptions;

import java.io.Serializable;

public class RPCException extends RuntimeException implements Serializable {
    private String symbol;

    private Throwable cause;

    public RPCException() {
    }

    public RPCException(String symbol) {
        super(symbol);
        this.symbol = symbol;
    }

    public RPCException(String symbol, Throwable cause) {
        super(symbol, cause);
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
