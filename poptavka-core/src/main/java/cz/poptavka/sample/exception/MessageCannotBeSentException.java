package cz.poptavka.sample.exception;

/**
 * Should be raised whenever a message cannot be sent (i.e. it is not in a state
 * that permits the messsage to be sent or it lacks some necessary information)
 *
 * @author Vojtech Hubr
 *         Date: 21.8.2011
 */
public class MessageCannotBeSentException extends RuntimeException {
    /**
     * Creates a new instance of <code>MessageCannotBeSentException</code> without detail message.
     */
    public MessageCannotBeSentException() {
    }


    /**
     * Constructs an instance of <code>MessageCannotBeSentException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MessageCannotBeSentException(String msg) {
        super(msg);
    }
}
