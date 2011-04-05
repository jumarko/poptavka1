package cz.poptavka.sample.client.home.demands;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.domain.demand.Demand;

public class DemandView extends Composite {

    private static DemandViewUiBinder uiBinder = GWT.create(DemandViewUiBinder.class);

    interface DemandViewUiBinder extends UiBinder<Widget, DemandView> { }

    public DemandView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField Button buttonAttachments;
    @UiField Button buttonLogin;
    @UiField Button buttonRegister;

    @UiField Label id;
    @UiField Label description;
    @UiField Label price;
//    @UiField Label validTo;
//    @UiField Label endDate;
//    @UiField Label status;
//    @UiField Label type;
//    @UiField Label client;
//    @UiField Label localities;
//    @UiField Label categories;
//    @UiField Label suppliers;
//    @UiField Label excSuppliers;
//    @UiField Label minRating;
//    @UiField Label maxSuppliers;

    @UiField Label label1;
    @UiField Label label2;

    @UiField TextArea textArea;

    @UiField(provided = true) CellList<Object> cellList = new CellList<Object>(new AbstractCell<Object>() {

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context arg0,
                Object arg1, SafeHtmlBuilder arg2) {
            // TODO Auto-generated method stub

        }

    });


    public DemandView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("buttonAttachments")
    void onClickAttachments(ClickEvent e) {
        Window.alert("Attachement!");
    }

    @UiHandler("buttonLogin")
    void onClickLogin(ClickEvent e) {
        Window.alert("Login!");
    }

    @UiHandler("buttonRegister")
    void onClickRegister(ClickEvent e) {
        Window.alert("Register");
    }

    public void setDemand(Demand demand) {
        id.setText(Long.toString(demand.getId()));
        description.setText(demand.getDescription());
        price.setText(demand.getPrice().toString());
//        validTo.setText(demand.getValidTo().toString());
//        endDate.setText(demand.getEndDate().toString());
//        status.setText(demand.getStatus().toString());
//        type.setText(demand.getType().toString());
//        client.setText(demand.getClient().getLogin());
//        localities.setText(demand.getLocalities().toString());
//        categories.setText(demand.getCategories().toString());
//        suppliers.setText(demand.getSuppliers().toString());
//        excSuppliers.setText(demand.getExcludedSuppliers().toString());
//        minRating.setText(demand.getMinRating().toString());
//        maxSuppliers.setText(demand.getMaxSuppliers().toString());
    }
}
