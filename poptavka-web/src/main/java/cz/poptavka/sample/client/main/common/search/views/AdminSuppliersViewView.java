package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.domain.user.BusinessType;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class AdminSuppliersViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminSuppliersViewView> {
    }
    @UiField
    TextBox ratingFrom, ratingTo, supplierDescription, idFrom, idTo, supplierName;
    @UiField
    ListBox supplierCategory, supplierLocality, type, certified, verified;
    @UiField
    Button clearBtn;

    //    @Override
    public AdminSuppliersViewView() {
//    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        //Rating
        ratingFrom.setText("0");
        ratingTo.setText("100");
        //BusinessType
        type.addItem(Storage.MSGS.select());
        for (BusinessType bType : BusinessType.values()) {
            type.addItem(bType.getValue());
        }
        type.setSelectedIndex(0);
        //Certified
        certified.addItem(Storage.MSGS.select());
        certified.addItem("true");
        certified.addItem("false");
        certified.setSelectedIndex(0);
        //Vertified
        verified.addItem(Storage.MSGS.select());
        for (Verification type : Verification.values()) {
            verified.addItem(type.name());
        }
        verified.setSelectedIndex(0);
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminSuppliers();
        if (!supplierName.getText().equals("")) {
            data.getAdminSuppliers().setSupplierName(supplierName.getText());
        }
        if (!supplierDescription.getText().equals("")) {
            data.getAdminSuppliers().setSupplierDescription(supplierDescription.getText());
        }
        int selected = supplierCategory.getSelectedIndex();
        if (selected != 0) {
            data.getAdminSuppliers().setSupplierCategory(new CategoryDetail(
                    Long.valueOf(supplierCategory.getValue(selected)),
                    supplierCategory.getItemText(selected)));
        }
        selected = supplierLocality.getSelectedIndex();
        if (selected != 0) {
            data.getAdminSuppliers().setSupplierLocality(new LocalityDetail(supplierLocality.getItemText(selected),
                    supplierLocality.getValue(selected)));
        }
        if (!ratingFrom.getText().equals("0")) {
            data.getAdminSuppliers().setRatingFrom(Integer.valueOf(ratingFrom.getText()));
        }
        if (!ratingTo.getText().equals("100")) {
            data.getAdminSuppliers().setRatingTo(Integer.valueOf(ratingTo.getText()));
        }
        if (type.getSelectedIndex() != 0) {
            data.getAdminSuppliers().setType(type.getItemText(type.getSelectedIndex()));
        }
        if (certified.getSelectedIndex() != 0) {
            data.getAdminSuppliers().setCertified(Boolean.valueOf(certified.getItemText(certified.getSelectedIndex())));
        }
        if (verified.getSelectedIndex() != 0) {
            data.getAdminSuppliers().setVerified(verified.getItemText(verified.getSelectedIndex()));
        }
        if (!idFrom.getText().equals("")) {
            data.getAdminDemands().setDemandIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminDemands().setDemandIdTo(Long.valueOf(idTo.getText()));
        }
        return data;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public ListBox getCategoryList() {
        return supplierCategory;
    }

    @Override
    public ListBox getLocalityList() {
        return supplierLocality;
    }

    @Override
    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminSuppliers().getSupplierName() != null) {
            infoText.append("supplier:");
            infoText.append(data.getAdminSuppliers().getSupplierName());
        }
        if (data.getAdminSuppliers().getSupplierDescription() != null) {
            infoText.append("desc:");
            infoText.append(data.getAdminSuppliers().getSupplierDescription());
        }
        if (data.getAdminSuppliers().getSupplierCategory() != null) {
            infoText.append("category:");
            infoText.append(data.getAdminSuppliers().getSupplierCategory().getName());
        }
        if (data.getAdminSuppliers().getSupplierLocality() != null) {
            infoText.append("locality:");
            infoText.append(data.getAdminSuppliers().getSupplierLocality().getName());
        }
        if (data.getAdminSuppliers().getRatingFrom() != null) {
            infoText.append("ratingFrom:");
            infoText.append(data.getAdminSuppliers().getRatingFrom());
        }
        if (data.getAdminSuppliers().getRatingTo() != null) {
            infoText.append("ratingTo:");
            infoText.append(data.getAdminSuppliers().getRatingTo());
        }
        if (data.getAdminSuppliers().getType() != null) {
            infoText.append("type:");
            infoText.append(data.getAdminSuppliers().getType());
        }
        if (data.getAdminSuppliers().getCertified() != null) {
            infoText.append("cert:");
            infoText.append(data.getAdminSuppliers().getCertified());
        }
        if (data.getAdminSuppliers().getVerified() != null) {
            infoText.append("verif:");
            infoText.append(data.getAdminSuppliers().getVerified());
        }
        if (data.getAdminSuppliers().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminSuppliers().getIdFrom());
        }
        if (data.getAdminSuppliers().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminSuppliers().getIdTo());
        }
        infoHolder.setText(infoText.toString());
    }

    @UiHandler("ratingFrom")
    void validateRatingFrom(ChangeEvent event) {
        if (!ratingFrom.getText().matches("[0-9]+")) {
            ratingFrom.setText("0");
        }
    }

    @UiHandler("ratingTo")
    void validateRatingTo(ChangeEvent event) {
        if (!ratingTo.getText().matches("[0-9]+")) {
            ratingTo.setText("100");
        }
    }

    @UiHandler("idFrom")
    void validateIdFrom(ChangeEvent event) {
        if (!idFrom.getText().matches("[0-9]+")) {
            idFrom.setText("");
        }
    }

    @UiHandler("idTo")
    void validateIdTo(ChangeEvent event) {
        if (!idTo.getText().matches("[0-9]+")) {
            idTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        supplierName.setText("");
        supplierDescription.setText("");
        supplierCategory.setSelectedIndex(0);
        supplierLocality.setSelectedIndex(0);
        ratingFrom.setText("0");
        ratingTo.setText("100");
        type.setSelectedIndex(0);
        certified.setSelectedIndex(0);
        verified.setSelectedIndex(0);
        idFrom.setText("");
        idTo.setText("");
    }
}