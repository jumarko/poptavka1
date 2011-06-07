package cz.poptavka.sample.client.home.demands.demand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

public class DemandView extends Composite implements
        DemandPresenter.DemandViewInterface {

    private static DemandViewUiBinder uiBinder = GWT.create(DemandViewUiBinder.class);

    interface DemandViewUiBinder extends UiBinder<Widget, DemandView> {
    }

    public DemandView() {
        initWidget(uiBinder.createAndBindUi(this));
        buttonAttachments.setVisible(false);

    }
    @UiField
    Button buttonAttachments;
    @UiField
    Button buttonLogin;
    @UiField
    Button buttonRegister;
    @UiField
    FlexTable infoTable;
//    @UiField
//    Label label1;
//    @UiField
//    Label label2;
    @UiField
    TextArea textArea;

    public DemandView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        buttonAttachments.setVisible(false);
    }

    @Override
    public HasClickHandlers getButtonAttachments() {
        return buttonAttachments;
    }

    @Override
    public HasClickHandlers getButtonLogin() {
        return buttonLogin;
    }

    @Override
    public HasClickHandlers getButtonRegister() {
        return buttonRegister;
    }

    public void setDemand(FullDemandDetail demand) {
        infoTable.clear();
        textArea.setText("");
        buttonAttachments.setVisible(false);

        int row = 0;

        if (demand.getDescription() != null) {
            textArea.setText(demand.getDescription());
        }

        if (demand.getPrice() != null) {
            infoTable.setWidget(row, 0, new Label("Název: "));
            infoTable.setWidget(row++, 1, new Label(demand.getTitle().toString()));
        }

        if (demand.getPrice() != null) {
            infoTable.setWidget(row, 0, new Label("Cena: "));
            infoTable.setWidget(row++, 1, new Label(demand.getPrice().toPlainString()));
        }

        if (demand.getEndDate() != null) {
            infoTable.setWidget(row, 0, new Label("Dátum ukončení: "));
            infoTable.setWidget(row++, 1, new Label(demand.getEndDate().toString()));
        }

        if (demand.getValidToDate() != null) {
            infoTable.setWidget(row, 0, new Label("Validní do: "));
            infoTable.setWidget(row++, 1, new Label(demand.getValidToDate().toString()));
        }

        if (demand.getDemandType() != null) {
            infoTable.setWidget(row, 0, new Label("Typ: "));
            infoTable.setWidget(row++, 1, new Label(demand.getDemandType()));
        }

        if (demand.getCategories() != null) {
            infoTable.setWidget(row, 0, new Label("Kategórie: "));
            infoTable.setWidget(row++, 1, new Label(demand.getCategories().toString()));
        }

        if (demand.getLocalities() != null) {
            infoTable.setWidget(row, 0, new Label("Lokality: "));
            infoTable.setWidget(row++, 1, new Label(demand.getLocalities().toString()));
        }

        infoTable.setWidget(row, 0, new Label("Ponuky: "));
        infoTable.setWidget(row++, 1, new Label(Integer.toString(demand.getMaxOffers())));

        if (demand.getPrice() != null) {
            infoTable.setWidget(row++, 0, new Label("Prílohy: "));
            infoTable.setWidget(row, 1, new Label(demand.getTitle().toString()));
            buttonAttachments.setVisible(true);
        }
    }
}
