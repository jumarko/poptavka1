package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;

public class MessagesTabViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, MessagesTabViewView> {
    }
    @UiField
    TextBox idFrom, idTo, sender, subject;
    @UiField
    CheckBox isStar;
    @UiField
    DateBox createdFrom, createdTo;
    @UiField
    Button clearBtn;

//    @Override
//    public void createView() {
    public MessagesTabViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initMessagesTab();
        if (!idFrom.getText().equals("")) {
            data.getMessagesTab().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getMessagesTab().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!sender.getText().equals("")) {
            data.getMessagesTab().setSender(sender.getText());
        }
        if (!subject.getText().equals("")) {
            data.getMessagesTab().setSubject(subject.getText());
        }
        if (!isStar.getText().equals("")) {
            data.getMessagesTab().setIsStar(isStar.getValue());
        }
        if (createdFrom.getValue() != null) {
            data.getMessagesTab().setCreateFrom(createdFrom.getValue());
        }
        if (createdTo.getValue() != null) {
            data.getMessagesTab().setCreateTo(createdTo.getValue());
        }
        return data;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public ListBox getCategoryList() {
        return null;
    }

    @Override
    public ListBox getLocalityList() {
        return null;
    }

    @Override
    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getMessagesTab().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getMessagesTab().getIdFrom());
        }
        if (data.getMessagesTab().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getMessagesTab().getIdTo());
        }
        if (data.getMessagesTab().getSender() != null) {
            infoText.append("sender:");
            infoText.append(data.getMessagesTab().getSender());
        }
        if (data.getMessagesTab().getSubject() != null) {
            infoText.append("subject:");
            infoText.append(data.getMessagesTab().getSubject());
        }
        if (data.getMessagesTab().getIsStar() != null) {
            infoText.append("star:");
            infoText.append(data.getMessagesTab().getIsStar().toString());
        }
        if (data.getMessagesTab().getCreateFrom() != null) {
            infoText.append("createdFrom:");
            infoText.append(data.getMessagesTab().getCreateFrom().toString());
        }
        if (data.getMessagesTab().getCreateTo() != null) {
            infoText.append("createdTo:");
            infoText.append(data.getMessagesTab().getCreateTo().toString());
        }
        infoHolder.setText(infoText.toString());
    }

    @UiHandler("idFrom")
    void validatePriceFrom(ChangeEvent event) {
        if (!idFrom.getText().matches("[0-9]+")) {
            idFrom.setText("");
        }
    }

    @UiHandler("idTo")
    void validatePriceTo(ChangeEvent event) {
        if (!idTo.getText().matches("[0-9]+")) {
            idTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        idFrom.setText("");
        idTo.setText("");
        sender.setText("");
        subject.setText("");
        isStar.setText("");
        createdFrom.setValue(null);
        createdTo.setValue(null);
    }
}