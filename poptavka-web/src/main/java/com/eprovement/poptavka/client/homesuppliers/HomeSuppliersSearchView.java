package com.eprovement.poptavka.client.homesuppliers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.common.search.SearchModulePresenter;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.IntegerBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import java.util.ArrayList;

public class HomeSuppliersSearchView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeSuppliersSearchView> {
    }
    @UiField
    TextBox companyName, supplierDescription;
    @UiField
    IntegerBox ratingFrom, ratingTo;
    @UiField
    Button clearBtn;

    public HomeSuppliersSearchView() {
        initWidget(uiBinder.createAndBindUi(this));
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!companyName.getText().equals("")) {
            filters.add(new FilterItem("name", FilterItem.OPERATION_LIKE, companyName.getText()));
        }
        if (!supplierDescription.getText().equals("")) {
            filters.add(new FilterItem("description", FilterItem.OPERATION_LIKE,
                    supplierDescription.getText()));
        }
        if (!ratingFrom.getText().equals("0")) {
            filters.add(new FilterItem("overalRating", FilterItem.OPERATION_FROM, ratingFrom.getValue()));
        }
        if (!ratingTo.getText().equals("100")) {
            filters.add(new FilterItem("overalRating", FilterItem.OPERATION_FROM, ratingTo.getValue()));
        }
        return filters;
    }

    @UiHandler("ratingFrom")
    void validateRatingFrom(ChangeEvent event) {
        if (!ratingFrom.getText().matches("[0-9]+")) {
            ratingFrom.setText("0");
        }
    }

    @UiHandler("ratingTo")
    void validateRatingTo(ChangeEvent event) {
        if (!ratingTo.getText().matches("[0-9]+")) {
            ratingTo.setText("100");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        companyName.setText("");
        supplierDescription.setText("");
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}