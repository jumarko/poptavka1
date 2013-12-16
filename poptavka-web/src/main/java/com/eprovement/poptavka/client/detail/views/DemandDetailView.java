package com.eprovement.poptavka.client.detail.views;

import com.eprovement.poptavka.client.catLocSelector.others.CatLogSimpleCell;
import com.eprovement.poptavka.client.common.DateUtils;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.github.gwtbootstrap.client.ui.Column;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;

public class DemandDetailView extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static DemandDetailViewUiBinder uiBinder = GWT.create(DemandDetailViewUiBinder.class);

    interface DemandDetailViewUiBinder extends UiBinder<Widget, DemandDetailView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) CellList categories, localities;
    @UiField Label demandName, price, endDate, description, clientRating, urgencyLabel;
    @UiField Column ratingColumn;
    @UiField Image urgency;
    /** Class attributes. **/
    private FullDemandDetail demandDetail;
    /** Constants. **/
    private static final String EMPTY = "";

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    /**
     * Detail view of demand
     *
     * @param advancedView true to show fields for admin, false to show fields for clients
     */

    @UiConstructor
    public DemandDetailView(boolean advancedView) {
        categories = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        localities = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        initWidget(uiBinder.createAndBindUi(this));

        ratingColumn.setVisible(advancedView);

        StyleResource.INSTANCE.details().ensureInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setDemanDetail(FullDemandDetail demandDetail) {
        GWT.log("detail detail" + demandDetail.toString());
        this.demandDetail = demandDetail;
        demandName.setText(demandDetail.getDemandTitle());
        clientRating.setText(Integer.toString(demandDetail.getClientRating()));
        price.setText(Storage.CURRENCY_FORMAT.format(demandDetail.getPrice()));
        endDate.setText(DateUtils.toString(demandDetail.getEndDate()));
        setValidTo(demandDetail.getValidTo());
        categories.setRowData(demandDetail.getCategories());
        localities.setRowData(demandDetail.getLocalities());
        description.setText(demandDetail.getDescription());
    }

    private void setValidTo(Date validTo) {
        if (validTo == null) {
            urgencyLabel.setText(Storage.MSGS.commonNotDefined());
        } else {
            Date now = new Date();
            int daysBetween = CalendarUtil.getDaysBetween(now, validTo);
            if (daysBetween < 0) {
                urgency.setResource(Storage.RSCS.images().urgencyHeader());
                urgency.setTitle(Storage.MSGS.urgencyExpiredDesc());
            } else if (daysBetween <= Constants.DAYS_URGENCY_HIGH) {
                urgency.setResource(Storage.RSCS.images().urgencyRed());
                urgency.setTitle(Storage.MSGS.urgencyHighDesc());
            } else if (daysBetween <= Constants.DAYS_URGENCY_HIGHER) {
                urgency.setResource(Storage.RSCS.images().urgencyOrange());
                urgency.setTitle(Storage.MSGS.urgencyMediumDesc());
            } else {
                urgency.setResource(Storage.RSCS.images().urgencyGreen());
                urgency.setTitle(Storage.MSGS.urgencyLowDesc());
            }
        }
        urgencyLabel.setVisible(validTo == null);
        urgency.setVisible(validTo != null);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    public FullDemandDetail getDemandDetail() {
        return demandDetail;
    }

    /**************************************************************************/
    /* OTHERS                                                                 */
    /**************************************************************************/
    public void clear() {
        demandName.setText(EMPTY);
        price.setText(EMPTY);
        endDate.setText(EMPTY);
        urgency.setResource(Storage.RSCS.images().urgencyHeader());
        categories.setRowCount(0);
        localities.setRowCount(0);
        description.setText(EMPTY);
    }
}
