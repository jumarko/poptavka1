package cz.poptavka.sample.client.user.messages;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

public class MessagesModuleView extends Composite
        implements MessagesModulePresenter.MessagesLayoutInterface, StyleInterface {

    private static MessagesModuleViewUiBinder uiBinder = GWT.create(MessagesModuleViewUiBinder.class);

    interface MessagesModuleViewUiBinder extends UiBinder<Widget, MessagesModuleView> {
    }
    private static final Logger LOGGER = Logger.getLogger(MessagesModuleView.class.getName());
//    @UiField Button myDemandsBtn, offersBtn, createDemandBtn;
    @UiField
    SimplePanel contentPanel;
    @UiField
    Button inboxButton, sentButton, trashButton;

    public MessagesModuleView() {
        StyleResource.INSTANCE.common().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setContent(Widget contentWidget) {
        contentPanel.setWidget(contentWidget);
    }

    @Override
    public Button getInboxButton() {
        return inboxButton;
    }

    @Override
    public Button getSentButton() {
        return sentButton;
    }

    @Override
    public Button getTrashButton() {
        return trashButton;
    }

    /** toggle visible actions/buttons for current user decided by his role. **/
    @Override
    public void setRoleInterface(Role role) {
        LOGGER.fine("Set User style for role " + role.toString());
        switch (role) {
            case SUPPLIER:
            //cascade, include client below, because supplier is always client too
//                supMenu.getStyle().setDisplay(Display.BLOCK);
            case CLIENT:
//                administration.setStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
//                myDemandsOperatorLink.setStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
                break;
            default:
                break;
        }
    }

    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }
}
