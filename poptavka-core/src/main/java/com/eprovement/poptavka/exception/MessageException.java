package com.eprovement.poptavka.exception;

/**
 * Represents a number of problems when handling messages - e.g. message cannot
 * be sent, modified...
 *
 * @author Vojtech Hubr
 *         Date: 21.8.2011
 */
public class MessageException extends Exception {
    /**
     * Creates a new instance of <code>MessageException</code> without detail message.
     */
    public MessageException() {
    }


    /**
     * Constructs an instance of <code>MessageException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MessageException(String msg) {
        super(msg);
    }
}
