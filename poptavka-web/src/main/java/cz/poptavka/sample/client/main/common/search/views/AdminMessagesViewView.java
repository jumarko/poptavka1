package cz.poptavka.sample.client.main.common.search.views;

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
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.shared.domain.type.MessageType;

public class AdminMessagesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminMessagesViewView> {
    }
    @UiField
    TextBox messageIdFrom, messageIdTo, demandIdFrom, demandIdTo, parentIdFrom,
    parentIdTo, senderIdFrom, senderIdTo, receiverIdFrom, receiverIdTo, subject, body;
    @UiField
    DateBox createdFrom, createdTo, sentFrom, sentTo;
    @UiField
    ListBox type, state;
    @UiField
    Button clearBtn;

    @Override
    public void createView() {
//    public AdminMessagesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        type.addItem(Storage.MSGS.select());
        for (MessageType msgtype : MessageType.values()) {
            type.addItem(msgtype.name());
        }
        state.addItem(Storage.MSGS.select());
        for (MessageState msgState : MessageState.values()) {
            state.addItem(msgState.name());
        }
    }

    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminMessages();
        if (!messageIdFrom.getText().equals("")) {
            data.getAdminMessages().setMessageIdFrom(Long.valueOf(messageIdFrom.getText()));
        }
        if (!messageIdTo.getText().equals("")) {
            data.getAdminMessages().setMessageIdTo(Long.valueOf(messageIdTo.getText()));
        }
        if (!demandIdFrom.getText().equals("")) {
            data.getAdminMessages().setDemandIdFrom(Long.valueOf(demandIdFrom.getText()));
        }
        if (!demandIdTo.getText().equals("")) {
            data.getAdminMessages().setDemandIdTo(Long.valueOf(demandIdTo.getText()));
        }
        if (!parentIdFrom.getText().equals("")) {
            data.getAdminMessages().setParentIdFrom(Long.valueOf(parentIdFrom.getText()));
        }
        if (!parentIdTo.getText().equals("")) {
            data.getAdminMessages().setParentIdTo(Long.valueOf(parentIdTo.getText()));
        }
        if (!senderIdFrom.getText().equals("")) {
            data.getAdminMessages().setSenderIdFrom(Long.valueOf(senderIdFrom.getText()));
        }
        if (!senderIdTo.getText().equals("")) {
            data.getAdminMessages().setSenderIdTo(Long.valueOf(senderIdTo.getText()));
        }
        if (!receiverIdFrom.getText().equals("")) {
            data.getAdminMessages().setReceiverIdFrom(Long.valueOf(receiverIdFrom.getText()));
        }
        if (!receiverIdTo.getText().equals("")) {
            data.getAdminMessages().setReceiverIdTo(Long.valueOf(receiverIdTo.getText()));
        }
        if (!subject.getText().equals("")) {
            data.getAdminMessages().setSubject(subject.getText());
        }
        if (!body.getText().equals("")) {
            data.getAdminMessages().setBody(body.getText());
        }
        if (createdFrom.getValue() != null) {
            data.getAdminMessages().setCreatedFrom(createdFrom.getValue());
        }
        if (createdTo.getValue() != null) {
            data.getAdminMessages().setCreatedTo(createdTo.getValue());
        }
        if (sentFrom.getValue() != null) {
            data.getAdminMessages().setSentFrom(sentFrom.getValue());
        }
        if (sentTo.getValue() != null) {
            data.getAdminMessages().setSentTo(sentTo.getValue());
        }
        if (type.getSelectedIndex() != 0) {
            data.getAdminMessages().setType(type.getItemText(type.getSelectedIndex()));
        }
        if (state.getSelectedIndex() != 0) {
            data.getAdminMessages().setState(state.getItemText(state.getSelectedIndex()));
        }
        return data;
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminMessages().getMessageIdFrom() != null) {
            infoText.append("messageIdFrom:");
            infoText.append(data.getAdminMessages().getMessageIdFrom());
        }
        if (data.getAdminMessages().getMessageIdTo() != null) {
            infoText.append("messageIdTo:");
            infoText.append(data.getAdminMessages().getMessageIdTo());
        }
        if (data.getAdminMessages().getDemandIdFrom() != null) {
            infoText.append("demandIdFrom:");
            infoText.append(data.getAdminMessages().getDemandIdFrom());
        }
        if (data.getAdminMessages().getDemandIdTo() != null) {
            infoText.append("demandIdTo:");
            infoText.append(data.getAdminMessages().getDemandIdTo());
        }
        if (data.getAdminMessages().getParentIdFrom() != null) {
            infoText.append("parentIdFrom:");
            infoText.append(data.getAdminMessages().getParentIdFrom());
        }
        if (data.getAdminMessages().getParentIdTo() != null) {
            infoText.append("parentIdTo:");
            infoText.append(data.getAdminMessages().getParentIdTo());
        }
        if (data.getAdminMessages().getSenderIdFrom() != null) {
            infoText.append("senderIdFrom:");
            infoText.append(data.getAdminMessages().getSenderIdFrom());
        }
        if (data.getAdminMessages().getSenderIdTo() != null) {
            infoText.append("senderIdTo:");
            infoText.append(data.getAdminMessages().getSenderIdTo());
        }
        if (data.getAdminMessages().getReceiverIdFrom() != null) {
            infoText.append("receiverIdFrom:");
            infoText.append(data.getAdminMessages().getReceiverIdFrom());
        }
        if (data.getAdminMessages().getReceiverIdTo() != null) {
            infoText.append("receiverIdTo:");
            infoText.append(data.getAdminMessages().getReceiverIdTo());
        }
        if (data.getAdminMessages().getSubject() != null) {
            infoText.append("subject:");
            infoText.append(data.getAdminMessages().getSubject());
        }
        if (data.getAdminMessages().getBody() != null) {
            infoText.append("body:");
            infoText.append(data.getAdminMessages().getBody());
        }
        if (data.getAdminMessages().getCreatedFrom() != null) {
            infoText.append("createdFrom:");
            infoText.append(data.getAdminMessages().getCreatedFrom());
        }
        if (data.getAdminMessages().getCreatedTo() != null) {
            infoText.append("createdTo:");
            infoText.append(data.getAdminMessages().getCreatedTo());
        }
        if (data.getAdminMessages().getSentFrom() != null) {
            infoText.append("sentFrom:");
            infoText.append(data.getAdminMessages().getSentFrom());
        }
        if (data.getAdminMessages().getSentFrom() != null) {
            infoText.append("sentTo:");
            infoText.append(data.getAdminMessages().getSentTo());
        }
        if (data.getAdminMessages().getType() != null) {
            infoText.append("type:");
            infoText.append(data.getAdminMessages().getType());
        }
        if (data.getAdminMessages().getState() != null) {
            infoText.append("state:");
            infoText.append(data.getAdminMessages().getState());
        }

        infoHolder.setText(infoText.toString());
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

    @UiHandler("receiverIdFrom")
    void validateReceiverIdFrom(ChangeEvent event) {
        if (!receiverIdFrom.getText().matches("[0-9]+")) {
            receiverIdFrom.setText("");
        }
    }

    @UiHandler("receiverIdTo")
    void validateReceiverIdTo(ChangeEvent event) {
        if (!receiverIdTo.getText().matches("[0-9]+")) {
            receiverIdTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        messageIdFrom.setText("");
        messageIdTo.setText("");
        demandIdFrom.setText("");
        demandIdTo.setText("");
        parentIdFrom.setText("");
        parentIdTo.setText("");
        senderIdFrom.setText("");
        senderIdTo.setText("");
        receiverIdFrom.setText("");
        receiverIdTo.setText("");
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
    public ListBox getCategoryList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListBox getLocalityList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}