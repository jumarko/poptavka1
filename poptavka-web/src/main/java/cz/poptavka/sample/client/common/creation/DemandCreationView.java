package cz.poptavka.sample.client.common.creation;

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

import cz.poptavka.sample.client.common.widget.OverflowComposite;
import cz.poptavka.sample.client.common.widget.StatusIconLabel;
import cz.poptavka.sample.client.common.widget.StatusIconLabel.State;
import cz.poptavka.sample.client.resources.StyleResource;

public class DemandCreationView extends OverflowComposite implements DemandCreationPresenter.CreationViewInterface {

    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);
    interface CreationViewUiBinder extends UiBinder<Widget, DemandCreationView> {    }

    private static final Logger LOGGER = Logger.getLogger("    DemandCreationView");

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    private List<StatusIconLabel> statusLabels = new ArrayList<StatusIconLabel>();

    //step1
    @UiField StackLayoutPanel mainPanel;
    @UiField SimplePanel basicInfoHolder;

    //step2
//    @UiField VerticalPanel stepTwo;
    @UiField SimplePanel categoryHolder;
    //step3
//    @UiField VerticalPanel stepThree;
    @UiField SimplePanel localityHolder;
    //step4
//    @UiField VerticalPanel stepFour;
    @UiField SimplePanel advInfoHolder;
    //step5
//    @UiField VerticalPanel stepFive;
    @UiField DockLayoutPanel userFormPanel;
    @UiField SimplePanel userFormHolder;
    @UiField Button demandCreateBtn;

    @UiField StatusIconLabel basicStatus, categoryStatus, localityStatus, advStatus, userStatus;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        /** filling status list **/
        StatusIconLabel[] array = {basicStatus, categoryStatus, localityStatus, advStatus, userStatus};
        statusLabels = Arrays.asList(array);

        /** style implementation and overflow tweaks **/
        StyleResource.INSTANCE.common().ensureInjected();
        setParentOverflow(basicInfoHolder, Overflow.AUTO);
        setParentOverflow(categoryHolder, Overflow.AUTO);
        setParentOverflow(localityHolder, Overflow.AUTO);
        setParentOverflow(advInfoHolder, Overflow.AUTO);
        setParentOverflow(userFormPanel, Overflow.AUTO);

//        basicStatus.set
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public SimplePanel getLocalityHolder() {
        return localityHolder;
    }

    @Override
    public SimplePanel getCategoryHolder() {
        return categoryHolder;
    }

    @Override
    public SimplePanel getBasicInfoHolder() {
        return basicInfoHolder;
    }

    @Override
    public SimplePanel getAdvInfoHolder() {
        return advInfoHolder;
    }

    @Override
    public SimplePanel getUserFormHolder() {
        return userFormHolder;
    }

    @Override
    public void toggleRegAndCreateButton() {
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

}
