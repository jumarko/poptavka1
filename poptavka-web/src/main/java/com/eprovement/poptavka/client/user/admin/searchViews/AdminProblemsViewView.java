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

public class AdminProblemsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminProblemsViewView> {
    }

    @UiField TextBox idFrom, idTo, text;

    public AdminProblemsViewView() {
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
        if (!text.getText().equals("")) {
            filters.add(new FilterItem("text", Operation.OPERATION_LIKE, text.getText(), group++));
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
        text.setText("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        //no validation montitors
        return true;
    }
}