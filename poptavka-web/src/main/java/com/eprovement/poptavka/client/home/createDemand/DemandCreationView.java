package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.resources.StyleResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class DemandCreationView extends OverflowComposite implements DemandCreationPresenter.CreationViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);

    interface CreationViewUiBinder extends UiBinder<Widget, DemandCreationView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final Logger LOGGER = Logger.getLogger("DemandCreationView");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private List<StatusIconLabel> statusLabels = new ArrayList<StatusIconLabel>();
    private List<SimplePanel> holderPanels = new ArrayList<SimplePanel>();
    @UiField
    StatusIconLabel userStatus, basicStatus, categoryStatus, localityStatus, advStatus;
    @UiField
    SimplePanel userFormHolder, basicInfoHolder, categoryHolder, localityHolder, advInfoHolder;
    @UiField
    TabLayoutPanel mainPanel;
    @UiField
    DockLayoutPanel userFormPanel;
    @UiField
    Button loginBtn, registerBtn, createDemandBtn;
    @UiField
    Button nextButtonTab2, nextButtonTab3, nextButtonTab4,
    backButtonTab3, backButtonTab4, backButtonTab5;
    @UiField
    HTML firstTabHeader;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        /** filling status list **/
        StatusIconLabel[] array = {userStatus, basicStatus, categoryStatus, localityStatus, advStatus};
        statusLabels = Arrays.asList(array);
        /** filling panels list **/
        SimplePanel[] panels = {userFormHolder, basicInfoHolder, categoryHolder, localityHolder, advInfoHolder};
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
    /* UiHandlers                                                             */
    /**************************************************************************/
    /** NEXT. **/
    @UiHandler("nextButtonTab2")
    public void nextButtonTab2ClickHandler(ClickEvent event) {
        selectNextTab();
    }

    @UiHandler("nextButtonTab3")
    public void nextButtonTab3ClickHandler(ClickEvent event) {
        selectNextTab();
    }

    @UiHandler("nextButtonTab4")
    public void nextButtonTab4ClickHandler(ClickEvent event) {
        selectNextTab();
    }

    /** BACK. **/
    @UiHandler("backButtonTab3")
    public void backButtonTab3ClickHandler(ClickEvent event) {
        selectPreviousTab();
    }

    @UiHandler("backButtonTab4")
    public void backButtonTab4ClickHandler(ClickEvent event) {
        selectPreviousTab();
    }

    @UiHandler("backButtonTab5")
    public void backButtonTab5ClickHandler(ClickEvent event) {
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

    /** LABELS. **/
    @Override
    public StatusIconLabel getStatusLabel(int order) {
        return statusLabels.get(order);
    }

    /** HEADERS. **/
    @Override
    public HTML getFirstTabHeader() {
        return firstTabHeader;
    }

    /** BUTTONS. **/
    @Override
    public Button getLoginBtn() {
        return loginBtn;
    }

    @Override
    public Button getRegisterBtn() {
        return registerBtn;
    }

    @Override
    public Button getCreateDemandButton() {
        return createDemandBtn;
    }

    public Button getBackButtonTab3() {
        return backButtonTab3;
    }

    public Button getBackButtonTab4() {
        return backButtonTab4;
    }

    public Button getBackButtonTab5() {
        return backButtonTab5;
    }

    /** OTHERS. **/
//    @Override
//    public void toggleLoginRegistration() {
//        createDemandBtn.setVisible(!createDemandBtn.isVisible());
//        switchUserFormMessages(createDemandBtn.isVisible());
//
//    }
//
//    public void switchUserFormMessages(boolean toRegister) {
//        if (toRegister) {
//            userStatus.setTexts(MSGS.regMessage(), MSGS.regDescription());
//            userStatus.setStyleState(StyleResource.INSTANCE.common().infoMessage(), State.INFO_16);
//        } else {
//            userStatus.setTexts(MSGS.loginMessage(), MSGS.loginDescription());
//        }
//    }
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
        //history back
        History.back();
//        mainPanel.selectTab(mainPanel.getSelectedIndex() - 1, true);
    }
}
