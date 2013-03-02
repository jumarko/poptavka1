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
import com.eprovement.poptavka.client.common.search.SearchModulePresenter;
import com.eprovement.poptavka.shared.search.FilterItem;
import java.util.ArrayList;

public class AdminClientsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminClientsViewView> {
    }
    @UiField
    TextBox idFrom, idTo, companyName, firstName, lastName, ratingFrom, ratingTo;
    @UiField
    Button clearBtn;

    public AdminClientsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        ratingFrom.setText("0");
        ratingTo.setText("100");
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
        if (!companyName.getText().equals("")) {
            filters.add(new FilterItem("companyName", FilterItem.OPERATION_LIKE, companyName.getText()));
        }
        if (!firstName.getText().equals("")) {
            filters.add(new FilterItem("personFirstName", FilterItem.OPERATION_LIKE, firstName.getText()));
        }
        if (!lastName.getText().equals("")) {
            filters.add(new FilterItem("personLastName", FilterItem.OPERATION_LIKE, lastName.getText()));
        }
        if (!ratingFrom.getText().equals("0")) {
            filters.add(new FilterItem("overalRating", FilterItem.OPERATION_LIKE, ratingFrom.getText()));
        }
        if (!ratingTo.getText().equals("100")) {
            filters.add(new FilterItem("overalRating", FilterItem.OPERATION_LIKE, ratingTo.getText()));
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

    @UiHandler("ratingFrom")
    void validateRatingFrom(ChangeEvent event) {
        if (!ratingFrom.getText().matches("[0-9]+")) {
            ratingFrom.setText("0");
        }
    }

    @UiHandler("ratingTo")
    void validateratingTo(ChangeEvent event) {
        if (!ratingTo.getText().matches("[0-9]+")) {
            ratingTo.setText("100");
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
        companyName.setText("");
        firstName.setText("");
        lastName.setText("");
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}