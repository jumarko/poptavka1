package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.grid.cell.SupplierCell;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DemandDetailView extends Composite {

    private static DemandDetailViewUiBinder uiBinder = GWT.create(DemandDetailViewUiBinder.class);

    interface DemandDetailViewUiBinder extends UiBinder<Widget, DemandDetailView> {
    }
    @UiField(provided = true)
    CellList categories, localities, excludedSuppliers;
    @UiField
    Label demandName, price, endDate, validTo, maxNumberOfSuppliers, minSupplierRating, description;
    //i18n
    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(bundle.currencyFormat());
    private DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    //Constructors
    public DemandDetailView() {
        categories = new CellList<CategoryDetail>(new CategoryCell(CategoryCell.DISPLAY_COUNT_DISABLED));
        localities = new CellList<LocalityDetail>(new LocalityCell(LocalityCell.DISPLAY_COUNT_DISABLED));
        excludedSuppliers = new CellList<FullSupplierDetail>(new SupplierCell());
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.detailViews().ensureInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setDemanDetail(FullDemandDetail demandDetail) {
        GWT.log("detail detail" + demandDetail.toString());
        demandName.setText(demandDetail.getTitle());
        price.setText(currencyFormat.format(demandDetail.getPrice()));
        endDate.setText(dateFormat.format(demandDetail.getEndDate()));
        validTo.setText(dateFormat.format(demandDetail.getValidToDate()));
        categories.setRowData(demandDetail.getCategories());
        localities.setRowData(demandDetail.getLocalities());
        maxNumberOfSuppliers.setText(Integer.toString(demandDetail.getMaxOffers()));
        minSupplierRating.setText(Integer.toString(demandDetail.getMinRating()) + "%");
        excludedSuppliers.setRowData(demandDetail.getExcludedSuppliers());
        description.setText(demandDetail.getDescription());
    }
}
