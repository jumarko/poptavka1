package com.eprovement.poptavka.client.user.admin.searchViews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.main.common.search.SearchModulePresenter;
import com.eprovement.poptavka.client.main.common.search.dataHolders.FilterItem;
import java.util.ArrayList;

public class AdminProblemsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminProblemsViewView> {
    }
    @UiField
    TextBox idFrom, idTo, text;
    @UiField
    Button clearBtn;

    public AdminProblemsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!idFrom.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_FROM, idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_TO, idTo.getText()));
        }
        if (!text.getText().equals("")) {
            filters.add(new FilterItem("text", FilterItem.OPERATION_LIKE, text.getText()));
        }
        return filters;
    }

    @UiHandler("idFrom")
    void validateIdFrom(ChangeEvent event) {
        if (!idFrom.getText().matches("[0-9]+")) {
            idFrom.setText("");
        }
    }

    @UiHandler("idTo")
    void validateIdTo(ChangeEvent event) {
        if (!idTo.getText().matches("[0-9]+")) {
            idTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        idFrom.setText("");
        idTo.setText("");
        text.setText("");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}