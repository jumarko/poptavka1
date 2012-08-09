package com.eprovement.poptavka.shared.exceptions;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import java.util.Arrays;

public final class ExceptionUtils {

    private ExceptionUtils() {
        super();
    }

    public static void showErrorDialog(String errorTitle, String errorMessage) {


        // TODO: show more handy popup
        final PopupPanel errorPanel = new PopupPanel(true, true);
        errorPanel.setWidget(new HTML(errorMessage));
        errorPanel.center();
        errorPanel.show();

    }


    /**
     * Extracts message and stacktrace from given {@code throwable} and its cause (if any).
     * @param throwable
     * @return String containing message and stactrace
     */
    public static String getFullErrorMessage(Throwable throwable) {
        if (throwable == null) {
            throw new IllegalArgumentException("throwable cannot be null");
        }

        StringBuilder message = new StringBuilder();
        message.append("Unexpected error occured when calling the server:\n"
                + "Error message: " + throwable.getMessage() + "\n"
                + "Stack trace:\n" + ExceptionUtils.getStackTrace(throwable));
        if (throwable.getCause() != null) {
            message.append("\n" + "    Cause error message: " + throwable.getCause().getMessage() + "\n"
                    + "    Cause Stack trace:\n" + ExceptionUtils.getStackTrace(throwable.getCause()));
        }

        return message.toString();
    }
    /**
     * <p>A way to get the stack-trace of an throwable.</p>
     * @param throwable  the <code>Throwable</code> to be examined
     * @return the stack trace
     * @since 2.0
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable != null && throwable.getStackTrace() != null) {
            return Arrays.toString(throwable.getStackTrace());
        }
        return "";
    }


}
