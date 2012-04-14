package cz.poptavka.sample.shared.exceptions;

import java.io.Serializable;

public class RPCException extends Exception implements Serializable {
    private String symbol;

    public RPCException() {
    }

    public RPCException(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
