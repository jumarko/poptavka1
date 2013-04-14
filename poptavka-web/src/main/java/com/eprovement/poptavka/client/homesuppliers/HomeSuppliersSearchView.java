package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.MyIntegerBox;
import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.common.search.SearchModulePresenter;
import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import java.util.ArrayList;

public class HomeSuppliersSearchView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeSuppliersSearchView> {
    }
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor ratingMonitorFrom, ratingMonitorTo;
    @UiField TextBox companyName, supplierDescription;
    @UiField Button clearBtn;
    /** Search Fields. **/
    private static final String FIELD_NAME = "businessUser.businessUserData.companyName";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_RATING = "overalRating";

    public HomeSuppliersSearchView() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initValidationMonitors() {
        ratingMonitorFrom = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, SearchGroup.class, BusinessUserDetail.UserField.OVERALL_RATING.getValue());
        ratingMonitorTo = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, SearchGroup.class, BusinessUserDetail.UserField.OVERALL_RATING.getValue());
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!companyName.getText().isEmpty()) {
            filters.add(new FilterItem(FIELD_NAME, FilterItem.OPERATION_LIKE, companyName.getText()));
        }
        if (!supplierDescription.getText().isEmpty()) {
            filters.add(new FilterItem(FIELD_DESCRIPTION, FilterItem.OPERATION_LIKE,
                    supplierDescription.getText()));
        }
        if (ratingMonitorFrom.getValue() != null) {
            filters.add(new FilterItem(FIELD_RATING, FilterItem.OPERATION_FROM, ratingMonitorFrom.getValue()));
        }
        if (ratingMonitorTo.getValue() != null) {
            filters.add(new FilterItem(FIELD_RATING, FilterItem.OPERATION_TO, ratingMonitorTo.getValue()));
        }
        return filters;
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        clear();
    }

    @Override
    public void clear() {
        companyName.setText("");
        supplierDescription.setText("");
        ((MyIntegerBox) ratingMonitorFrom.getWidget()).setText("");
        ratingMonitorFrom.reset();
        ((MyIntegerBox) ratingMonitorTo.getWidget()).setText("");
        ratingMonitorTo.reset();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}