package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

public class SupplierDetailInfoView extends Composite
        implements SupplierDetailInfoPresenter.SupplierDetailInfoInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierDetailInfoViewUiBinder uiBinder = GWT.create(SupplierDetailInfoViewUiBinder.class);

    interface SupplierDetailInfoViewUiBinder extends UiBinder<Widget, SupplierDetailInfoView> {
    }
    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor website, companyName, identificationNumber, taxId, description;
    @UiField SimplePanel addressHolder;
    /** Class attributes. **/
    private List<ValidationMonitor> validationMonitors;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        companyName = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        description = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        website = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        identificationNumber = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        taxId = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);

        initWidget(uiBinder.createAndBindUi(this));

        validationMonitors = Arrays.asList(website, companyName, identificationNumber, taxId, description);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public boolean isValid() {
        boolean valid = true;
        for (ValidationMonitor box : validationMonitors) {
            valid = box.isValid() && valid;
        }
        boolean validAddress = ((AddressSelectorView) addressHolder.getWidget()).isValid();
        return valid && validAddress;
    }

    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public BusinessUserDetail updateBusinessUserDetail(BusinessUserDetail user) {
        user.setCompanyName((String) companyName.getValue());
        user.setDescription((String) description.getValue());
        user.setIdentificationNumber((String) identificationNumber.getValue());
        user.setTaxId((String) taxId.getValue());
        user.setWebsite((String) website.getValue());
        user.setAddresses(Arrays.asList(((AddressSelectorView) addressHolder.getWidget()).createAddress()));
        return user;
    }
}
