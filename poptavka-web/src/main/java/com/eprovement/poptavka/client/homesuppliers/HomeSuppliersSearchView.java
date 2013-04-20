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
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
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
        int group = 0;
        if (!companyName.getText().isEmpty()) {
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserField.COMPANY_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyName.getText(), group));
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserField.FIRST_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyName.getText(), group));
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserField.LAST_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyName.getText(), group++));
        }
        if (!supplierDescription.getText().isEmpty()) {
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserField.DESCRIPTION.getValue()),
                    Operation.OPERATION_LIKE, supplierDescription.getText(), group++));
        }
        if (ratingMonitorFrom.getValue() != null) {
            filters.add(new FilterItem(
                    UserField.OVERALL_RATING.getValue(),
                    Operation.OPERATION_FROM, ratingMonitorFrom.getValue(), group++));
        }
        if (ratingMonitorTo.getValue() != null) {
            filters.add(new FilterItem(
                    UserField.OVERALL_RATING.getValue(),
                    Operation.OPERATION_TO, ratingMonitorTo.getValue(), group++));
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