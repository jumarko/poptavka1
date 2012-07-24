package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.shared.domain.demand.BaseDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;

public class DemandDetailView extends Composite {

    private static DemandDetailViewUiBinder uiBinder = GWT.create(DemandDetailViewUiBinder.class);

    interface DemandDetailViewUiBinder extends UiBinder<Widget, DemandDetailView> {
    }
    @UiField
    Label demandName, price, endDate, validTo, type, categories, localities,
    maxNumberOfSuppliers, minSupplierRating, excludedSuppliers, description;
    @UiField
    HTMLPanel detail;
    private boolean empty = true;

    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);

    private NumberFormat currencyFormat = NumberFormat.getFormat(bundle.currencyFormat());

    public Label getPrice() {
        return price;
    }

    public HTMLPanel getDetail() {
        return detail;
    }

    public Label getEndDate() {
        return endDate;
    }

    public Label getValidTo() {
        return validTo;
    }

    public Label getCategories() {
        return categories;
    }

    public Label getLocalities() {
        return localities;
    }

    public Label getMinSupplierRating() {
        return minSupplierRating;
    }

    public Label getExcludedSuppliers() {
        return excludedSuppliers;
    }

    public Label getMaxNumberOfSuppliers() {
        return maxNumberOfSuppliers;
    }

    public DemandDetailView() {
        initWidget(uiBinder.createAndBindUi(this));
        detail.setVisible(true);
    }

    public DemandDetailView(FullDemandDetail demand) {
        initWidget(uiBinder.createAndBindUi(this));
        //detail.setVisible(true);
        init((FullDemandDetail) demand);
    }

    public DemandDetailView(BaseDemandDetail demand) {
        initWidget(uiBinder.createAndBindUi(this));
        //detail.setVisible(true);
        init((BaseDemandDetail) demand);
    }

    private void init(BaseDemandDetail demand) {
    }

    private void init(FullDemandDetail demand) {
        GWT.log("Demand detail" + demand.toString());
        demandName.setText(demand.getTitle());
        price.setText(currencyFormat.format(demand.getPrice()));
        endDate.setText(demand.getEndDate().toString());
        validTo.setText(demand.getValidToDate().toString());
        type.setText(demand.getDetailType().getValue());
        StringBuilder categoriesBuilder = new StringBuilder();
        for (String s : demand.getCategories().values()) {
            categoriesBuilder.append(s);
            categoriesBuilder.append("\n");
        }
        categories.setText(categoriesBuilder.toString());
        StringBuilder localitiesBuilder = new StringBuilder();
        for (LocalityDetail s : demand.getLocalities()) {
            localitiesBuilder.append(s.getName());
            localitiesBuilder.append("\n");
        }
        localities.setText(localitiesBuilder.toString());
        maxNumberOfSuppliers.setText(Integer.toString(demand.getMaxOffers()));
        minSupplierRating.setText(Integer.toString(demand.getMinRating()) + "%");
        StringBuilder excludedSuppliersBuildes = new StringBuilder();
        for (FullSupplierDetail detail : demand.getExcludedSuppliers()) {
            excludedSuppliersBuildes.append(detail.getCompanyName());
            excludedSuppliersBuildes.append(", ");
        }
        excludedSuppliers.setText(excludedSuppliersBuildes.toString());
        description.setText(demand.getDescription());
        empty = false;
        detail.getElement().getFirstChildElement().getStyle().setDisplay(Display.BLOCK);
    }

    public void setDemanDetail(FullDemandDetail detail) {
        this.init(detail);
    }

    public void toggleVisible() {
        if (detail.isVisible()) {
            detail.getElement().getStyle().setDisplay(Display.NONE);
        } else {
            detail.getElement().getStyle().setDisplay(Display.BLOCK);
        }
    }
}
