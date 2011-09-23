package cz.poptavka.sample.client.user.demands.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

public class DemandDetailView extends Composite {

    private static DemandDetailViewUiBinder uiBinder = GWT.create(DemandDetailViewUiBinder.class);
    interface DemandDetailViewUiBinder extends UiBinder<Widget, DemandDetailView> {
    }

    @UiField FlexTable detailTable;
    @UiField Label textArea;
    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);

    public DemandDetailView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public DemandDetailView(FullDemandDetail demand) {
        initWidget(uiBinder.createAndBindUi(this));
        init((FullDemandDetail) demand);
    }

    public DemandDetailView(BaseDemandDetail demand) {
        initWidget(uiBinder.createAndBindUi(this));
        init((BaseDemandDetail) demand);
    }

    private void init(BaseDemandDetail demand) {
    }

    private void init(FullDemandDetail demand) {
        detailTable.clear();
        textArea.setText("");

        textArea.getElement().getStyle().setProperty("whiteSpace", "pre");
        int row = 0;
        detailTable.setWidget(row, 0, new Label(bundle.commonInfo() + ":"));
        if (demand.getDescription() != null) {
            textArea.setText(demand.getDescription());
        }

        if (demand.getTitle() != null) {
            detailTable.setWidget(row, 0, new Label(bundle.title() + ":"));
            detailTable.setWidget(row++, 1, new Label(demand.getTitle().toString()));
        }

        if (demand.getPrice() != null) {
            detailTable.setWidget(row, 0, new Label(bundle.price() + ":"));
            detailTable.setWidget(row++, 1, new Label(demand.getPrice().toPlainString()));
        }

        if (demand.getEndDate() != null) {
            detailTable.setWidget(row, 0, new Label(bundle.endDate() + ":"));
            detailTable.setWidget(row++, 1, new Label(demand.getEndDate().toString()));
        }

        if (demand.getValidToDate() != null) {
            detailTable.setWidget(row, 0, new Label(bundle.validTo() + ":"));
            detailTable.setWidget(row++, 1, new Label(demand.getValidToDate().toString()));
        }

        if (demand.getDemandType() != null) {
            detailTable.setWidget(row, 0, new Label(bundle.type() + ":"));
            detailTable.setWidget(row++, 1, new Label(demand.getDemandType()));
        }

        if (demand.getCategories() != null) {
            detailTable.setWidget(row, 0, new Label(bundle.category() + ":"));
            detailTable.setWidget(row++, 1,
                    new Label(demand.getCategories().toString()
                    .substring(1, demand.getCategories().toString().length() - 1)));
        }

        if (demand.getLocalities() != null) {
            detailTable.setWidget(row, 0, new Label(bundle.locality() + ":"));
            detailTable.setWidget(row++, 1,
                    new Label(demand.getLocalities().toString()
                    .substring(1, demand.getLocalities().toString().length() - 1)));
        }

        if (demand.getPrice() != null) {
            detailTable.setWidget(row++, 0, new Label(bundle.attachment() + ":"));
            detailTable.setWidget(row, 1, new Label(demand.getPrice().toString()));
        }
        detailTable.setWidget(row++, 0, new Label(bundle.maxOffers() + ":"));
        detailTable.setWidget(row, 1, new Label(new Integer(demand.getMaxOffers()).toString()));
        detailTable.setWidget(row++, 0, new Label(bundle.minRating() + ":"));
        detailTable.setWidget(row, 1, new Label(new Integer(demand.getMinRating()).toString()));
        detailTable.setWidget(row++, 0, new Label(bundle.excludedSuppliers() + ":"));
        detailTable.setWidget(row, 1, new Label(demand.getExcludedSuppliers().toString()
                .substring(1, demand.getExcludedSuppliers().toString().length() - 1)));
    }

    public void setDemanDetail(FullDemandDetail detail) {
        this.init(detail);
    }

}
