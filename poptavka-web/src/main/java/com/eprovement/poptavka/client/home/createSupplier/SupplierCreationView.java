package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.google.gwt.user.client.ui.Label;
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
    private int back = 0;
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
    Button nextButtonTab1, nextButtonTab2, nextButtonTab3, registerBtn,
    backButtonTab2, backButtonTab3, backButtonTab4;
    @UiField
    Anchor conditionLink;

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
    public void agreedCheckChanged(ClickEvent event) {
        agreementPanel.setStyleName(StyleResource.INSTANCE.common().emptyStyle());
    }

    /** NEXT. **/
    @UiHandler("nextButtonTab1")
    public void nextButtonTab1ClickHandler(ClickEvent event) {
        selectNextTab();
    }

    @UiHandler("nextButtonTab2")
    public void nextButtonTab2ClickHandler(ClickEvent event) {
        selectNextTab();
    }

    @UiHandler("nextButtonTab3")
    public void nextButtonTab3ClickHandler(ClickEvent event) {
        selectNextTab();
    }

    /** BACK. **/
    @UiHandler("backButtonTab2")
    public void backButtonTab2ClickHandler(ClickEvent event) {
        selectPreviousTab();
    }

    @UiHandler("backButtonTab3")
    public void backButtonTab3ClickHandler(ClickEvent event) {
        selectPreviousTab();
    }

    @UiHandler("backButtonTab4")
    public void backButtonTab4ClickHandler(ClickEvent event) {
        selectPreviousTab();
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
        Button closeButton = new Button(MSGS.commonBtnClose());
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
            AggreementDialogBox b = new AggreementDialogBox();
            b.show();
            agreementPanel.setStyleName(StyleResource.INSTANCE.common().errorField());
            return false;
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void selectNextTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() + 1, true);
    }

    private void selectPreviousTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() - 1, true);
    }
}

class AggreementDialogBox extends PopupPanel {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public AggreementDialogBox() {
        // Create a table to layout the content
        VerticalPanel vp = new VerticalPanel();

        // Add some text to the top of the dialog
        vp.add(new Label(MSGS.supplierCreationAgreementMessage()));

        // Add a close button at the bottom of the dialog
        Button closeButton = new Button(MSGS.commonBtnClose(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        vp.add(closeButton);

        if (LocaleInfo.getCurrentLocale().isRTL()) {
            vp.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_LEFT);
        } else {
            vp.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
        }

        setWidget(vp);

        setGlassEnabled(true);
        setAnimationEnabled(true);
        center();
    }
}
