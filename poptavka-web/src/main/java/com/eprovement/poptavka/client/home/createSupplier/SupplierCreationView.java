package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.StatusIconLabel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SupplierCreationView extends OverflowComposite
        implements SupplierCreationPresenter.CreationViewInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);

    interface CreationViewUiBinder extends UiBinder<Widget, SupplierCreationView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final Logger LOGGER = Logger.getLogger("SupplierCreationView");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private List<StatusIconLabel> statusLabels = new ArrayList<StatusIconLabel>();
    private List<SimplePanel> holderPanels = new ArrayList<SimplePanel>();
    @UiField
    StatusIconLabel basicStatus, categoryStatus, localityStatus, serviceStatus;
    @UiField
    SimplePanel supplierInfoHolder, categoryHolder, localityHolder, serviceHolder;
    @UiField
    TabLayoutPanel mainPanel;
    @UiField
    HorizontalPanel agreementPanel;
    @UiField
    CheckBox agreedCheck;
    @UiField
    Button registerBtn, nextButton1, nextButton2, nextButton3;
    @UiField
    Anchor conditionLink;
    @UiField
    SimplePanel tableHolder;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        /** filling status list **/
        StatusIconLabel[] array = {basicStatus, categoryStatus, localityStatus, serviceStatus};
        statusLabels = Arrays.asList(array);
        /** filling panels list **/
        SimplePanel[] panels = {supplierInfoHolder, categoryHolder, localityHolder, serviceHolder};
        holderPanels = Arrays.asList(panels);

        /** style implementation and overflow tweaks **/
        StyleResource.INSTANCE.common().ensureInjected();
        StyleResource.INSTANCE.createTabPanel().ensureInjected();
        for (SimplePanel panel : holderPanels) {
            setParentOverflow(panel, Overflow.AUTO);
        }
        categoryHolder.setSize("956px", "350px");
    }

    /**************************************************************************/
    /* UiHandler                                                              */
    /**************************************************************************/
    @UiHandler("agreedCheck")
    public void agreedCheckChanged(ClickEvent e) {
        agreementPanel.setStyleName(StyleResource.INSTANCE.common().emptyStyle());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public TabLayoutPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public SimplePanel getHolderPanel(int order) {
        return holderPanels.get(order);
    }

    @Override
    public StatusIconLabel getStatusLabel(int order) {
        return statusLabels.get(order);
    }

    /** BUTTONS. **/
    @Override
    public HasClickHandlers getRegisterButton() {
        return registerBtn;
    }

    @Override
    public Button getNextButton1() {
        return nextButton1;
    }

    @Override
    public Button getNextButton2() {
        return nextButton2;
    }

    @Override
    public Button getNextButton3() {
        return nextButton3;
    }

    /** OTHERS. **/
    @Override
    public Anchor getConditionLink() {
        return conditionLink;
    }

    public void showConditions() {
        final PopupPanel panel = new PopupPanel(true, false);
        HTMLPanel contentPanel =
                new HTMLPanel("<div id='text' style='overflow: auto; height: 500px;'>"
                + "</div><hr /><div style='text-align: center' id='button'></div>");
        HTML content = new HTML(StyleResource.INSTANCE.conditions().getText());
        Button closeButton = new Button(MSGS.close());
        contentPanel.add(content, "text");
        contentPanel.add(closeButton, "button");
        panel.setWidget(contentPanel);
        panel.setWidth("580px");

        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                panel.hide();
            }
        });

        int x = (Window.getClientWidth() / 2) - 290;
        int y = (Window.getClientHeight() / 2) - 250;
        panel.setPopupPosition(x, y);

        panel.show();
    }

    @Override
    public boolean isValid() {
        if (agreedCheck.getValue()) {
            return agreedCheck.getValue();
        } else {
            DialogBox dialogBox = createDialogBox();
            dialogBox.center();
            dialogBox.show();
            agreementPanel.setStyleName(StyleResource.INSTANCE.common().errorField());
            return false;
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**
     * Create the dialog box for aggreement.
     *
     * @return the new dialog box
     */
    private DialogBox createDialogBox() {
        // Create a dialog box and set the caption text
        final DialogBox dialogBox = new DialogBox();
        dialogBox.ensureDebugId("cwDialogBox");
        dialogBox.setGlassEnabled(true);
        dialogBox.setAnimationEnabled(true);

        // Create a table to layout the content
        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setSpacing(4);
        dialogBox.setWidget(dialogContents);

        // Add some text to the top of the dialog
        HTML details = new HTML(MSGS.agreementMessage());
        dialogContents.add(details);
        dialogContents.setCellHorizontalAlignment(
                details, HasHorizontalAlignment.ALIGN_CENTER);

        // Add a close button at the bottom of the dialog
        Button closeButton = new Button(MSGS.close(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
        dialogContents.add(closeButton);
        if (LocaleInfo.getCurrentLocale().isRTL()) {
            dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_LEFT);
        } else {
            dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
        }

        return dialogBox;
    }
}
