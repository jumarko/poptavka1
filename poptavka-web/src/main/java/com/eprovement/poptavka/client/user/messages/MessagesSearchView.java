package com.eprovement.poptavka.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import java.util.ArrayList;

public class MessagesSearchView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, MessagesSearchView> {
    }
    @UiField
    TextBox sender, subject, body;
    @UiField
    Button clearBtn;

    public MessagesSearchView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!sender.getText().equals("")) {
            filters.add(new FilterItem("companyName", Operation.OPERATION_LIKE, sender.getText(), 0));
        }
        if (!subject.getText().equals("")) {
            filters.add(new FilterItem("subject", Operation.OPERATION_LIKE, subject.getText(), 1));
        }
        if (!body.getText().equals("")) {
            filters.add(new FilterItem("body", Operation.OPERATION_LIKE, body.getText(), 2));
        }
        return filters;
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        clear();
    }

    @Override
    public void clear() {
        sender.setText("");
        subject.setText("");
        body.setText("");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}