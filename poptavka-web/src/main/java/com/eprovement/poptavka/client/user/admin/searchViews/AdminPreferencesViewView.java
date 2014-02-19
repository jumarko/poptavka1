package com.eprovement.poptavka.client.user.admin.searchViews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import java.util.ArrayList;

public class AdminPreferencesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminPreferencesViewView> {
    }

    @UiField TextBox idFrom, idTo, key, value, description;

    public AdminPreferencesViewView() {
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
        if (!key.getText().equals("")) {
            filters.add(new FilterItem("key", Operation.OPERATION_LIKE, key.getText(), group++));
        }
        if (!value.getText().equals("")) {
            filters.add(new FilterItem("value", Operation.OPERATION_LIKE, value.getText(), group++));
        }
        if (!description.getText().equals("")) {
            filters.add(new FilterItem("description", Operation.OPERATION_IN, description.getText(), group++));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        idFrom.setText("");
        idTo.setText("");
        key.setText("");
        value.setText("");
        description.setText("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        //no validation moinitors
        return true;
    }
}