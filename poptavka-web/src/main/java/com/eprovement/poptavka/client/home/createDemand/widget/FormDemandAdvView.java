package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    //Temporary disabled
//    @UiField IntegerBox minRatingBox;
//    @UiField Button excludeBtn;
//    @UiField ListBox excludedList;

    /** Class attributes. **/
    ArrayList<HasValue> widgets = new ArrayList<HasValue>();
    HashMap<DemandField, Object> map = new HashMap<DemandField, Object>();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        //Validation widgets
        widgets.add(maxOffersBox);
//        temporary disabled
//        widgets.add(minRatingBox);
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
        int errorCount = 0;
        for (HasValue item : widgets) {
            ((Widget) item).removeStyleName(StyleResource.INSTANCE.common().errorField());
            if (item.getValue() == null) {
                ((Widget) item).setStyleName(StyleResource.INSTANCE.common().errorField());
                errorCount++;
            }
        }
        return errorCount == 0;
    }

    @Override
    public HashMap<DemandField, Object> getValues() {
        map.put(DemandField.MAX_OFFERS, maxOffersBox.getValue());
        map.put(DemandField.MIN_RATING, 0);
        map.put(DemandField.DEMAND_TYPE, getDemandType());
        map.put(DemandField.VALID_TO_DATE, getValidTo());
//        Temporary disabled
//        map.put(DemandField.MIN_RATING, minRatingBox.getValue());
//        TODO excluded suppliers
        return map;
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
            CalendarUtil.addDaysToDate(validTo, Constants.DAYS_URGENCY_HIGHT);
        } else if (urgency2.getValue()) {
            CalendarUtil.addDaysToDate(validTo, Constants.DAYS_URGENCY_HIGHTER);
        } else {
            CalendarUtil.addMonthsToDate(validTo, Constants.MONTHS_URGENCY_NORMAL);
        }
        return validTo;
    }
}
