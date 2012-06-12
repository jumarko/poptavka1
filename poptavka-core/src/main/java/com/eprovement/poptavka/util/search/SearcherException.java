/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.util.search;

/**
 *
 * @author Vojtech Hubr
 */
public class SearcherException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>SearcherException</code> without detail message.
     */
    public SearcherException() {
    }

    /**
     * Constructs an instance of
     * <code>SearcherException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SearcherException(String msg) {
        super(msg);
    }
}
