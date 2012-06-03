package com.eprovement.poptavka.client.home.createDemand;

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
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.main.common.OverflowComposite;
import com.eprovement.poptavka.client.main.common.StatusIconLabel;
import com.eprovement.poptavka.client.main.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.resources.StyleResource;

public class DemandCreationView extends OverflowComposite implements DemandCreationPresenter.CreationViewInterface {

    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);
    interface CreationViewUiBinder extends UiBinder<Widget, DemandCreationView> {    }

    private static final Logger LOGGER = Logger.getLogger("DemandCreationView");

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    private List<StatusIconLabel> statusLabels = new ArrayList<StatusIconLabel>();

    private List<SimplePanel> holderPanels = new ArrayList<SimplePanel>();

    @UiField StackLayoutPanel mainPanel;
    @UiField DockLayoutPanel userFormPanel;

    @UiField SimplePanel basicInfoHolder, categoryHolder, localityHolder, advInfoHolder, userFormHolder;
    @UiField StatusIconLabel basicStatus, categoryStatus, localityStatus, advStatus, userStatus;

    @UiField Button demandCreateBtn;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        /** filling status list **/
        StatusIconLabel[] array = {basicStatus, categoryStatus, localityStatus, advStatus, userStatus};
        statusLabels = Arrays.asList(array);

        SimplePanel[] panels = {basicInfoHolder, categoryHolder, localityHolder, advInfoHolder, userFormHolder};
        holderPanels  = Arrays.asList(panels);

        /** style implementation and overflow tweaks **/
        StyleResource.INSTANCE.common().ensureInjected();
        for (SimplePanel panel : holderPanels) {
            setParentOverflow(panel, Overflow.AUTO);
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void toggleLoginRegistration() {
        demandCreateBtn.setVisible(!demandCreateBtn.isVisible());
        switchUserFormMessages(demandCreateBtn.isVisible());

    }

    @Override
    public HasClickHandlers getCreateDemandButton() {
        return demandCreateBtn;
    }

    @Override
    public StackLayoutPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public StatusIconLabel getStatusLabel(int order) {
        return statusLabels.get(order - 1);
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
    public SimplePanel getHolderPanel(int order) {
        return holderPanels.get(order - 1);
    }

}
