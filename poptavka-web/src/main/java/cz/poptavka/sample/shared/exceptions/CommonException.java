package cz.poptavka.sample.shared.exceptions;

import java.io.Serializable;

public class CommonException extends Exception implements Serializable {
    private String symbol;

    public CommonException() {
    }

    public CommonException(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
