package cz.poptavka.sample.exception;

/**
 * Exception which should be raised when client tries to modify some type of
 * {@link cz.poptavka.sample.domain.common.TreeItem} object, ie. any type of usage of Create-Update-Remove methods.
 *
 * @author Juraj Martinka
 *         Date: 25.5.11
 */
public class TreeItemModificationException extends RuntimeException {

    private static final String MESSAGE_TEXT = "TreeItem cannot be modified using applicaiton logic - please check"
            + "the stored procedure for filling localities and categories.";

    public TreeItemModificationException() {
        this(MESSAGE_TEXT);
    }

    public TreeItemModificationException(String message) {
        this(message, null);
    }

    public TreeItemModificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
