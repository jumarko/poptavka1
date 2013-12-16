package com.eprovement.poptavka.client.common.smallPopups;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Martin Slavkovsky
 */
public class SimpleConfirmPopup extends Modal {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SimpleConfirmPopupUiBinder uiBinder = GWT.create(SimpleConfirmPopupUiBinder.class);

    interface SimpleConfirmPopupUiBinder extends UiBinder<Widget, SimpleConfirmPopup> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureModalStylesInjected();
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField SimplePanel selectorPanel;
    @UiField Button submitBtn, cancelBtn;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public SimpleConfirmPopup() {
        add(uiBinder.createAndBindUi(this));

        addStyleName(StyleResource.INSTANCE.modal().cellBrowserSelectionModal());
        setDynamicSafe(true);
        setHideOthers(false);
    }

    /**************************************************************************/
    /* UiHanlders                                                             */
    /**************************************************************************/
    @UiHandler("submitBtn")
    public void submitBtnClickHandler(ClickEvent e) {
        hide();
    }

    @UiHandler("cancelBtn")
    public void cancelBtnClickHandler(ClickEvent e) {
        hide();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public SimplePanel getSelectorPanel() {
        return selectorPanel;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }
}
