package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.validation.groups.Default;

public class FormDemandBasicView extends Composite
        implements FormDemandBasicPresenter.FormDemandBasicInterface, ProvidesValidate {

    private static FormDemandBasicUiBinder uiBinder = GWT.create(FormDemandBasicUiBinder.class);

    interface FormDemandBasicUiBinder extends UiBinder<Widget, FormDemandBasicView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor titleMonitor, priceMonitor, descriptionMonitor, endDateMonitor;
    /** Class attributes. **/
    ArrayList<ValidationMonitor> monitors;

    /**************************************************************************/
    /* Initializatiob                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
        initEndDateDatePricker();
    }

    private void initValidationMonitors() {
        titleMonitor = new ValidationMonitor<FullDemandDetail>(
                FullDemandDetail.class, Default.class, DemandField.TITLE.getValue());
        priceMonitor = new ValidationMonitor<FullDemandDetail>(
                FullDemandDetail.class, Default.class, DemandField.PRICE.getValue());
        endDateMonitor = new ValidationMonitor<FullDemandDetail>(
                FullDemandDetail.class, Default.class, DemandField.END_DATE.getValue());
        descriptionMonitor = new ValidationMonitor<FullDemandDetail>(
                FullDemandDetail.class, Default.class, DemandField.DESCRIPTION.getValue());
        monitors = new ArrayList<ValidationMonitor>(
                Arrays.asList(titleMonitor, priceMonitor, endDateMonitor, descriptionMonitor));
    }

    private void initEndDateDatePricker() {
        ((DateBox) endDateMonitor.getWidget()).setFormat(new DateBox.DefaultFormat(Storage.DATE_FORMAT));
        ((DateBox) endDateMonitor.getWidget()).getDatePicker().getParent().addHandler(
                new CloseHandler<PopupPanel>() {
                @Override
                public void onClose(CloseEvent event) {
                    endDateMonitor.validate();
                }
            }, CloseEvent.getType());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public FullDemandDetail updateBasicDemandInfo(FullDemandDetail demandToUpdate) {
        demandToUpdate.setTitle((String) titleMonitor.getValue());
        demandToUpdate.setPrice((BigDecimal) priceMonitor.getValue());
        demandToUpdate.setEndDate((Date) endDateMonitor.getValue());
        demandToUpdate.setDescription((String) descriptionMonitor.getValue());
        return demandToUpdate;
    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        for (ValidationMonitor monitor : monitors) {
            valid = monitor.isValid() && valid;
        }
        return valid;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}