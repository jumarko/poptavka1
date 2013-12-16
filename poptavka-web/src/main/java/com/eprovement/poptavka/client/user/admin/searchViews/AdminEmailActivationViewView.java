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
import com.google.gwt.user.datepicker.client.DateBox;
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import java.util.ArrayList;

public class AdminEmailActivationViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminEmailActivationViewView> {
    }
    @UiField
    TextBox idFrom, idTo, activationCode;
    @UiField
    DateBox timeoutFrom, timeoutTo;
    @UiField
    Button clearBtn;

    public AdminEmailActivationViewView() {
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
        if (!activationCode.getText().equals("")) {
            filters.add(new FilterItem("activationCode",
                    Operation.OPERATION_LIKE, activationCode.getText(), group++));
        }
        if (timeoutFrom.getValue() != null) {
            filters.add(new FilterItem("timeout", Operation.OPERATION_FROM, timeoutFrom.getValue(), group++));
        }
        if (timeoutTo.getValue() != null) {
            filters.add(new FilterItem("timeout", Operation.OPERATION_FROM, timeoutTo.getValue(), group++));
        }
        return filters;
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
        clear();
    }

    @Override
    public void clear() {
        idFrom.setText("");
        idTo.setText("");
        activationCode.setText("");
        timeoutFrom.setValue(null);
        timeoutTo.setValue(null);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}