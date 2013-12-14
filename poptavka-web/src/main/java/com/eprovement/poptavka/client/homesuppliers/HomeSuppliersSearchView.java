/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.ui.WSIntegerBox;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import com.github.gwtbootstrap.client.ui.TextBox;
import java.util.ArrayList;

/**
 * Home suppliers search presenter.
 *
 * @author Martin Slavkovsky
 */
public class HomeSuppliersSearchView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeSuppliersSearchView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor companyMonitor, ratingMonitorFrom, ratingMonitorTo;
    @UiField TextBox supplierDescription;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public HomeSuppliersSearchView() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Clear view components.
     */
    @Override
    public void clear() {
        companyMonitor.setValue("");
        supplierDescription.setText("");
        ((WSIntegerBox) ratingMonitorFrom.getWidget()).setText("");
        ratingMonitorFrom.resetValidation();
        ((WSIntegerBox) ratingMonitorTo.getWidget()).setText("");
        ratingMonitorTo.resetValidation();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * Gets search filters.
     * @return list of search filters
     */
    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        int group = 0;
        if (!((TextBox) companyMonitor.getWidget()).getText().isEmpty()) {
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserField.COMPANY_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyMonitor.getValue(), group));
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserField.FIRST_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyMonitor.getValue(), group));
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserField.LAST_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyMonitor.getValue(), group++));
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

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Inits validation monitors.
     */
    private void initValidationMonitors() {
        Class<?>[] groups = {SearchGroup.class};
        companyMonitor = createValidationMonitor(BusinessUserDetail.UserField.COMPANY_NAME, groups);
        ratingMonitorFrom = createValidationMonitor(BusinessUserDetail.UserField.OVERALL_RATING, groups);
        ratingMonitorTo = createValidationMonitor(BusinessUserDetail.UserField.OVERALL_RATING, groups);
    }

    /**
     * Creates validation monitors
     * @param field - validation field
     * @param groups - validation groups
     * @return created validation monitor
     */
    private ValidationMonitor createValidationMonitor(BusinessUserDetail.UserField field, Class<?>[] groups) {
        return new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class, groups, field.getValue());
    }
}