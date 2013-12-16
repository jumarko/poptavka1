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
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import java.util.ArrayList;

public class AdminPermissionsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminPermissionsViewView> {
    }
    @UiField
    TextBox idFrom, idTo, code, name, description;
    @UiField
    Button clearBtn;

    public AdminPermissionsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        int group = 0;
        if (!idFrom.getText().equals("")) {
            filters.add(new FilterItem("id", Operation.OPERATION_FROM, idFrom.getText(), group++));
        }
        if (!idTo.getText().equals("")) {
            filters.add(new FilterItem("id", Operation.OPERATION_TO, idTo.getText(), group++));
        }
        if (!code.getText().equals("")) {
            filters.add(new FilterItem("code", Operation.OPERATION_LIKE, code.getText(), group++));
        }
        if (!name.getText().equals("")) {
            filters.add(new FilterItem("name", Operation.OPERATION_LIKE, name.getText(), group++));
        }
        if (!description.getText().equals("")) {
            filters.add(new FilterItem("description", Operation.OPERATION_LIKE, description.getText(), group++));
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
        clear();
    }

    @Override
    public void clear() {
        idFrom.setText("");
        idTo.setText("");
        code.setText("");
        name.setText("");
        description.setText("");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}