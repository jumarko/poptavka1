package com.eprovement.poptavka.client.user.admin.searchViews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.domain.enums.BusinessType;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import java.util.ArrayList;

public class AdminSuppliersViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminSuppliersViewView> {
    }
    @UiField
    TextBox ratingFrom, ratingTo, supplierDescription, idFrom, idTo, supplierName;
    @UiField
    ListBox type, certified, verified;
    @UiField
    Button clearBtn;

    public AdminSuppliersViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        //Rating
        ratingFrom.setText("0");
        ratingTo.setText("100");
        //BusinessType
        type.addItem(Storage.MSGS.commonListDefault());
        for (BusinessType bType : BusinessType.values()) {
            type.addItem(bType.getValue());
        }
        type.setSelectedIndex(0);
        //Certified
        certified.addItem(Storage.MSGS.commonListDefault());
        certified.addItem("true");
        certified.addItem("false");
        certified.setSelectedIndex(0);
        //Vertified
        verified.addItem(Storage.MSGS.commonListDefault());
        for (Verification type : Verification.values()) {
            verified.addItem(type.name());
        }
        verified.setSelectedIndex(0);
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        int group = 0;
        if (!supplierName.getText().equals("")) {
            filters.add(new FilterItem("name", Operation.OPERATION_FROM, supplierName.getText(), group++));
        }
        if (!supplierDescription.getText().equals("")) {
            filters.add(new FilterItem("description", Operation.OPERATION_TO,
                    supplierDescription.getText(), group++));
        }
        if (!ratingFrom.getText().equals("0")) {
            filters.add(new FilterItem("overalRating", Operation.OPERATION_FROM, ratingFrom.getText(), group++));
        }
        if (!ratingTo.getText().equals("100")) {
            filters.add(new FilterItem("overalRating", Operation.OPERATION_TO, ratingTo.getText(), group++));
        }
        if (type.getSelectedIndex() != 0) {
            filters.add(new FilterItem("type", Operation.OPERATION_EQUALS,
                    type.getItemText(type.getSelectedIndex()), group++));
        }
        if (certified.getSelectedIndex() != 0) {
            filters.add(new FilterItem("certified", Operation.OPERATION_EQUALS,
                    certified.getItemText(certified.getSelectedIndex()), group++));
        }
        if (verified.getSelectedIndex() != 0) {
            filters.add(new FilterItem("verification", Operation.OPERATION_FROM,
                    verified.getItemText(verified.getSelectedIndex()), group++));
        }
        if (!idFrom.getText().equals("")) {
            filters.add(new FilterItem("id", Operation.OPERATION_FROM, idFrom.getText(), group++));
        }
        if (!idTo.getText().equals("")) {
            filters.add(new FilterItem("id", Operation.OPERATION_TO, idTo.getText(), group++));
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
        supplierName.setText("");
        supplierDescription.setText("");
        ratingFrom.setText("0");
        ratingTo.setText("100");
        type.setSelectedIndex(0);
        certified.setSelectedIndex(0);
        verified.setSelectedIndex(0);
        idFrom.setText("");
        idTo.setText("");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}