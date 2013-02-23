package com.eprovement.poptavka.service.fulltext;

/**
 * @author Juraj Martinka
 *         Date: 20.5.11
 */
public class FulltextInitializationException extends RuntimeException {

    public FulltextInitializationException(String message) {
        super(message);
    }

    public FulltextInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
