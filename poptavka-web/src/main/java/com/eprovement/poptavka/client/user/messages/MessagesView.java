package com.eprovement.poptavka.client.user.messages;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.StyleInterface;
import com.eprovement.poptavka.shared.domain.UserDetail.Role;

public class MessagesView extends Composite
        implements MessagesPresenter.MessagesLayoutInterface, StyleInterface {

    private static MessagesModuleViewUiBinder uiBinder = GWT.create(MessagesModuleViewUiBinder.class);

    interface MessagesModuleViewUiBinder extends UiBinder<Widget, MessagesView> {
    }
    private static final Logger LOGGER = Logger.getLogger(MessagesView.class.getName());
//    @UiField Button myDemandsBtn, offersBtn, createDemandBtn;
    @UiField
    SimplePanel wrapperMain, wrapperDetail;
    @UiField
    Button inboxButton, sentButton, trashButton, composeButton, draftButton;
    @UiField
    SplitLayoutPanel splitPanelSouth;

    public MessagesView() {
        StyleResource.INSTANCE.common().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public SimplePanel getWrapperMain() {
        return wrapperMain;
    }

    @Override
    public SimplePanel getWrapperDetail() {
        return wrapperDetail;
    }

    @Override
    public Button getComposeButton() {
        return composeButton;
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

    @Override
    public Button getDraftButton() {
        return draftButton;
    }

    @Override
    public SplitLayoutPanel getSplitPanel() {
        return splitPanelSouth;
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
}
