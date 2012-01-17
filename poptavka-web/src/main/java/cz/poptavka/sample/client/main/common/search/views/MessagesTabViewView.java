package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;

public class MessagesTabViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, MessagesTabViewView> {
    }
    @UiField
    TextBox sender, subject, body;
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
        if (!sender.getText().equals("")) {
            data.getMessagesTab().setSender(sender.getText());
        }
        if (!subject.getText().equals("")) {
            data.getMessagesTab().setSubject(subject.getText());
        }
        if (!body.getText().equals("")) {
            data.getMessagesTab().setSubject(body.getText());
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
        if (data.getMessagesTab().getSender() != null) {
            infoText.append("sender:");
            infoText.append(data.getMessagesTab().getSender());
        }
        if (data.getMessagesTab().getSubject() != null) {
            infoText.append("subject:");
            infoText.append(data.getMessagesTab().getSubject());
        }
        if (data.getMessagesTab().getBody() != null) {
            infoText.append("body:");
            infoText.append(data.getMessagesTab().getBody());
        }
        infoHolder.setText(infoText.toString());
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        sender.setText("");
        subject.setText("");
        body.setText("");
    }
}