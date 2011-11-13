package cz.poptavka.sample.client.user.demands.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;

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
        detail.setVisible(true);
        init((FullDemandDetail) demand);
    }

    public DemandDetailView(BaseDemandDetail demand) {
        initWidget(uiBinder.createAndBindUi(this));
        detail.setVisible(true);
        init((BaseDemandDetail) demand);
    }

    private void init(BaseDemandDetail demand) {

    }

    private void init(FullDemandDetail demand) {
        GWT.log("Demand detail" + demand.toString());
        demandName.setText(demand.getTitle());
        price.setText(demand.getPriceString());
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
        for (String s : demand.getLocalities().values()) {
            localitiesBuilder.append(s);
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
        if (!empty) {
            detail.setVisible(!detail.isVisible());
        }
    }

}
