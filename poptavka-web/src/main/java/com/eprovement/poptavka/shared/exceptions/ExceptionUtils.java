package com.eprovement.poptavka.shared.exceptions;

import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;

/**
 * @author ivan
 * @author Martin Slavkovsky (2.2.2013 added uiBinder)
 */
public final class ExceptionUtils extends Composite {

    /**************************************************************************/
    /**  UiBinder.                                                           **/
    /**************************************************************************/
    private ExceptionUtilsUiBinder uiBinder = GWT.create(ExceptionUtilsUiBinder.class);

    interface ExceptionUtilsUiBinder extends UiBinder<Widget, ExceptionUtils> {
    }

    /**************************************************************************/
    /**  Initialization.                                                     **/
    /**************************************************************************/
    private ExceptionUtils() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }
    /**************************************************************************/
    /**  Singleton definition.                                               **/
    /**************************************************************************/
    private static ExceptionUtils instance;

    public static ExceptionUtils getInstance() {
        if (instance == null) {
            instance = new ExceptionUtils();
        }
        return instance;
    }
    /**************************************************************************/
    /**  ShowErrorDialog.                                                    **/
    /**************************************************************************/
    /** UiBinder attributes. **/
    static @UiField Modal showErrorModal;
    static @UiField ModalFooter showErrorFooter;
    static @UiField Label showErrorLabel;
    static @UiField Button showErrorCloseBtn;

    /** UiHandlers. **/
    @UiHandler("showErrorCloseBtn")
    public void showErrorCloseBtnHandler(ClickEvent e) {
        ExceptionUtils.showErrorModal.hide();
    }

    /** Setup bootstrap modal popup. **/
    public static void showErrorDialog(String errorTitle, String errorMessage) {
        getInstance();
        showErrorLabel.setText(errorMessage);
        showErrorModal.setTitle(errorTitle);
        showErrorModal.show();
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
