/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homesuppliers;

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
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserDataField;
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
    @UiField(provided = true) ValidationMonitor companyMonitor, ratingMonitorFrom, ratingMonitorTo, descriptionMonitor;

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
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        companyMonitor.reset();
        descriptionMonitor.reset();
        ratingMonitorFrom.reset();
        ratingMonitorTo.reset();
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
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserDataField.COMPANY_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyMonitor.getValue(), group));
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserDataField.FIRST_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyMonitor.getValue(), group));
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserDataField.LAST_NAME.getValue()),
                    Operation.OPERATION_LIKE, companyMonitor.getValue(), group++));
        }
        if (!((TextBox) descriptionMonitor.getWidget()).getText().isEmpty()) {
            filters.add(new FilterItem(
                    Constants.PATH_TO_BUSINESS_DATA.concat(UserDataField.DESCRIPTION.getValue()),
                    Operation.OPERATION_LIKE, descriptionMonitor.getValue(), group++));
        }
        if (ratingMonitorFrom.getValue() != null) {
            filters.add(new FilterItem(
                    UserDataField.OVERALL_RATING.getValue(),
                    Operation.OPERATION_FROM, ratingMonitorFrom.getValue(), group++));
        }
        if (ratingMonitorTo.getValue() != null) {
            filters.add(new FilterItem(
                    UserDataField.OVERALL_RATING.getValue(),
                    Operation.OPERATION_TO, ratingMonitorTo.getValue(), group++));
        }
        return filters;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = companyMonitor.isValid() && valid;
        valid = descriptionMonitor.isValid() && valid;
        valid = ratingMonitorFrom.isValid() && valid;
        valid = ratingMonitorTo.isValid() && valid;
        return valid;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Inits validation monitors.
     */
    private void initValidationMonitors() {
        Class<?>[] groups = {SearchGroup.class};
        companyMonitor = createValidationMonitor(BusinessUserDetail.UserDataField.COMPANY_NAME, groups);
        ratingMonitorFrom = createValidationMonitor(BusinessUserDetail.UserDataField.OVERALL_RATING, groups);
        ratingMonitorTo = createValidationMonitor(BusinessUserDetail.UserDataField.OVERALL_RATING, groups);
        descriptionMonitor = createValidationMonitor(BusinessUserDetail.UserDataField.DESCRIPTION, groups);
    }

    /**
     * Creates validation monitors
     * @param field - validation field
     * @param groups - validation groups
     * @return created validation monitor
     */
    private ValidationMonitor createValidationMonitor(BusinessUserDetail.UserDataField field, Class<?>[] groups) {
        return new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class, groups, field.getValue());
    }
}