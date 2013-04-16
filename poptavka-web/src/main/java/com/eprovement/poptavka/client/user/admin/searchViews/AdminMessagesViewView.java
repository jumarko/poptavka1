package com.eprovement.poptavka.client.user.admin.searchViews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.search.SearchModulePresenter;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.domain.type.MessageType;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import java.util.ArrayList;

public class AdminMessagesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminMessagesViewView> {
    }
    @UiField
    TextBox messageIdFrom, messageIdTo, demandIdFrom, demandIdTo, parentIdFrom,
    parentIdTo, senderIdFrom, senderIdTo, subject, body;
    @UiField
    DateBox createdFrom, createdTo, sentFrom, sentTo;
    @UiField
    ListBox type, state;
    @UiField
    Button clearBtn;

    public AdminMessagesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        type.addItem(Storage.MSGS.commonListDefault());
        for (MessageType msgtype : MessageType.values()) {
            type.addItem(msgtype.name());
        }
        state.addItem(Storage.MSGS.commonListDefault());
        for (MessageState msgState : MessageState.values()) {
            state.addItem(msgState.name());
        }
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        int group = 0;
        if (!messageIdFrom.getText().equals("")) {
            filters.add(new FilterItem("id", Operation.OPERATION_FROM, messageIdFrom.getText(), group++));
        }
        if (!messageIdTo.getText().equals("")) {
            filters.add(new FilterItem("id", Operation.OPERATION_TO, messageIdTo.getText(), group++));
        }
        if (!demandIdFrom.getText().equals("")) {
            filters.add(new FilterItem("demand.id", Operation.OPERATION_FROM, demandIdFrom.getText(), group++));
        }
        if (!demandIdTo.getText().equals("")) {
            filters.add(new FilterItem("demand.id", Operation.OPERATION_TO, demandIdTo.getText(), group++));
        }
        if (!parentIdFrom.getText().equals("")) {
            filters.add(new FilterItem("parent.id", Operation.OPERATION_FROM, parentIdFrom.getText(), group++));
        }
        if (!parentIdTo.getText().equals("")) {
            filters.add(new FilterItem("parent.id", Operation.OPERATION_TO, parentIdTo.getText(), group++));
        }
        if (!senderIdFrom.getText().equals("")) {
            filters.add(new FilterItem("sender.id", Operation.OPERATION_FROM, senderIdFrom.getText(), group++));
        }
        if (!senderIdTo.getText().equals("")) {
            filters.add(new FilterItem("sender.id", Operation.OPERATION_TO, senderIdTo.getText(), group++));
        }
        if (!subject.getText().equals("")) {
            filters.add(new FilterItem("subject", Operation.OPERATION_LIKE, subject.getText(), group++));
        }
        if (!body.getText().equals("")) {
            filters.add(new FilterItem("body", Operation.OPERATION_LIKE, body.getText(), group++));
        }
        if (createdFrom.getValue() != null) {
            filters.add(new FilterItem("created", Operation.OPERATION_FROM, createdFrom.getValue(), group++));
        }
        if (createdTo.getValue() != null) {
            filters.add(new FilterItem("created", Operation.OPERATION_TO, createdTo.getValue(), group++));
        }
        if (sentFrom.getValue() != null) {
            filters.add(new FilterItem("sent", Operation.OPERATION_FROM, sentFrom.getValue(), group++));
        }
        if (sentTo.getValue() != null) {
            filters.add(new FilterItem("sent", Operation.OPERATION_TO, sentTo.getValue(), group++));
        }
        if (type.getSelectedIndex() != 0) {
            filters.add(new FilterItem("messageType", Operation.OPERATION_EQUALS,
                    type.getItemText(type.getSelectedIndex()), group++));
        }
        if (state.getSelectedIndex() != 0) {
            filters.add(new FilterItem("messageState", Operation.OPERATION_EQUALS,
                    state.getItemText(state.getSelectedIndex()), group++));
        }
        return filters;
    }

    @UiHandler("messageIdFrom")
    void validateMessageIdFrom(ChangeEvent event) {
        if (!messageIdFrom.getText().matches("[0-9]+")) {
            messageIdFrom.setText("");
        }
    }

    @UiHandler("messageIdTo")
    void validateMessageIdTo(ChangeEvent event) {
        if (!messageIdTo.getText().matches("[0-9]+")) {
            messageIdTo.setText("");
        }
    }

    @UiHandler("demandIdFrom")
    void validateDemandIdFrom(ChangeEvent event) {
        if (!demandIdFrom.getText().matches("[0-9]+")) {
            demandIdFrom.setText("");
        }
    }

    @UiHandler("demandIdTo")
    void validateDemandIdTo(ChangeEvent event) {
        if (!demandIdTo.getText().matches("[0-9]+")) {
            demandIdTo.setText("");
        }
    }

    @UiHandler("parentIdFrom")
    void validateParentIdFrom(ChangeEvent event) {
        if (!parentIdFrom.getText().matches("[0-9]+")) {
            parentIdFrom.setText("");
        }
    }

    @UiHandler("parentIdTo")
    void validateParentIdTo(ChangeEvent event) {
        if (!parentIdTo.getText().matches("[0-9]+")) {
            parentIdTo.setText("");
        }
    }

    @UiHandler("senderIdFrom")
    void validateSenderIdFrom(ChangeEvent event) {
        if (!senderIdFrom.getText().matches("[0-9]+")) {
            senderIdFrom.setText("");
        }
    }

    @UiHandler("senderIdTo")
    void validateSenderIdTo(ChangeEvent event) {
        if (!senderIdTo.getText().matches("[0-9]+")) {
            senderIdTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        clear();
    }

    @Override
    public void clear() {
        messageIdFrom.setText("");
        messageIdTo.setText("");
        demandIdFrom.setText("");
        demandIdTo.setText("");
        parentIdFrom.setText("");
        parentIdTo.setText("");
        senderIdFrom.setText("");
        senderIdTo.setText("");
        subject.setText("");
        body.setText("");
        createdFrom.setValue(null);
        createdTo.setValue(null);
        sentFrom.setValue(null);
        sentTo.setValue(null);
        type.setSelectedIndex(0);
        state.setSelectedIndex(0);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}