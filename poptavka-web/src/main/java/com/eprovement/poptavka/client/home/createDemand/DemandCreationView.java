package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.resources.StyleResource;

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
    StatusIconLabel basicStatus, categoryStatus, localityStatus, advStatus, userStatus;
    @UiField
    SimplePanel basicInfoHolder, categoryHolder, localityHolder, advInfoHolder, userFormHolder;
    @UiField
    TabLayoutPanel mainPanel;
    @UiField
    DockLayoutPanel userFormPanel;
    @UiField
    Button demandCreateBtn, nextButton1, nextButton2, nextButton3, nextButton4;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        /** filling status list **/
        StatusIconLabel[] array = {basicStatus, categoryStatus, localityStatus, advStatus, userStatus};
        statusLabels = Arrays.asList(array);
        /** filling panels list **/
        SimplePanel[] panels = {basicInfoHolder, categoryHolder, localityHolder, advInfoHolder, userFormHolder};
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
    public HasClickHandlers getCreateDemandButton() {
        return demandCreateBtn;
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

    @Override
    public Button getNextButton4() {
        return nextButton4;
    }

    /** OTHERS. **/
    @Override
    public void toggleLoginRegistration() {
        demandCreateBtn.setVisible(!demandCreateBtn.isVisible());
        switchUserFormMessages(demandCreateBtn.isVisible());

    }

    public void switchUserFormMessages(boolean toRegister) {
        if (toRegister) {
            userStatus.setTexts(MSGS.regMessage(), MSGS.regDescription());
            userStatus.setStyleState(StyleResource.INSTANCE.common().infoMessage(), State.INFO_16);
        } else {
            userStatus.setTexts(MSGS.loginMessage(), MSGS.loginDescription());
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
