package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;

public class FormDemandAdvView extends Composite
        implements FormDemandAdvPresenter.FormDemandAdvViewInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static FormDemandAdvViewUiBinder uiBinder = GWT.create(FormDemandAdvViewUiBinder.class);

    interface FormDemandAdvViewUiBinder extends UiBinder<Widget, FormDemandAdvView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField IntegerBox maxOffersBox;
    @UiField RadioButton classicRadio, attractiveRadio;
    @UiField RadioButton urgency1, urgency2, urgency3;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Mehtods                                                                */
    /**************************************************************************/
    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public boolean isValid() {
        if (maxOffersBox.getValue() == null) {
            maxOffersBox.setStyleName(StyleResource.INSTANCE.common().errorField());
            return false;
        } else {
            maxOffersBox.removeStyleName(StyleResource.INSTANCE.common().errorField());
            return true;
        }
    }

    @Override
    public FullDemandDetail updateAdvDemandInfo(FullDemandDetail demandToUpdate) {
        demandToUpdate.setMaxSuppliers(maxOffersBox.getValue());
        demandToUpdate.setMinRating(0);
        demandToUpdate.setDemandType(getDemandType());
        demandToUpdate.setValidTo(getValidTo());
        return demandToUpdate;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Get selected demand type.
     * @return Selected demand type as string.
     */
    private String getDemandType() {
        if (attractiveRadio.getValue()) {
            return FullDemandDetail.DemandType.ATTRACTIVE.getValue();
        } else {
            return FullDemandDetail.DemandType.NORMAL.getValue();
        }
    }

    /**
     * Construct valid to date to represent urgency level of demand.
     * Date is constructed by adding days to current date. For HIGH urgency level
     * are added less days than to HIGHER or NORMAL. See appropriate constants
     * in Constants class.
     *
     * @return valid to date
     */
    private Date getValidTo() {
        Date validTo = new Date();
        if (urgency3.getValue()) {
            CalendarUtil.addDaysToDate(validTo, Constants.DAYS_URGENCY_HIGH);
        } else if (urgency2.getValue()) {
            CalendarUtil.addDaysToDate(validTo, Constants.DAYS_URGENCY_HIGHER);
        } else {
            CalendarUtil.addMonthsToDate(validTo, Constants.MONTHS_URGENCY_NORMAL);
        }
        return validTo;
    }
}
