package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
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
    @UiField Label demandName, price, endDate, description, clientRating;
    @UiField Image urgency;
    @UiField Tooltip urgencyTooltip;
    /** Class attributes. **/
    private FullDemandDetail demandDetail;
    /** Constants. **/
    private static final String EMPTY = "";

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    //Constructors
    public DemandDetailView() {
        categories = new CellList<CategoryDetail>(new CategoryCell(CategoryCell.DISPLAY_COUNT_DISABLED));
        localities = new CellList<LocalityDetail>(new LocalityCell(LocalityCell.DISPLAY_COUNT_DISABLED));
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.detailViews().ensureInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setDemanDetail(FullDemandDetail demandDetail) {
        GWT.log("detail detail" + demandDetail.toString());
        this.demandDetail = demandDetail;
        demandName.setText(demandDetail.getTitle());
        clientRating.setText(Integer.toString(demandDetail.getClientRating()));
        price.setText(Storage.CURRENCY_FORMAT.format(demandDetail.getPrice()));
        endDate.setText(Storage.DATE_FORMAT.format(demandDetail.getEndDate()));
        setValidTo(demandDetail.getValidTo());
        categories.setRowData(demandDetail.getCategories());
        localities.setRowData(demandDetail.getLocalities());
        description.setText(demandDetail.getDescription());
    }

    private void setValidTo(Date validTo) {
        Date now = new Date();
        int daysBetween = CalendarUtil.getDaysBetween(now, validTo);
        if (daysBetween <= Constants.DAYS_URGENCY_HIGH) {
            urgency.setResource(Storage.RSCS.images().urgencyRed());
            urgencyTooltip.setTitle(Storage.MSGS.urgencyHighDesc());
        } else if (daysBetween <= Constants.DAYS_URGENCY_HIGHER) {
            urgency.setResource(Storage.RSCS.images().urgencyOrange());
            urgencyTooltip .setTitle(Storage.MSGS.urgencyHigherDesc());
        } else {
            urgency.setResource(Storage.RSCS.images().urgencyGreen());
            urgencyTooltip.setTitle(Storage.MSGS.urgencyNormalDesc());
        }
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
