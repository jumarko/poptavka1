package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class HomeSuppliersViewView extends Composite implements SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeSuppliersViewView> {
    }
    @UiField
    UListElement searchList;
//    @UiField
//    Button searchBtn;
    @UiField
    TextBox companyName, ratingFrom, ratingTo, supplierDescription;
    @UiField
    ListBox category, locality;

//    @Override
    public HomeSuppliersViewView() {
//    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
//        data.setType("homeSuppliers");
        data.setSupplierName(companyName.getText());
        data.setSupplierDescription(supplierDescription.getText());
        int selected = category.getSelectedIndex();
        if (selected != 0) {
            data.setSupplierCategory(new CategoryDetail(Long.valueOf(category.getValue(selected)),
                    category.getItemText(selected)));
        }
        selected = locality.getSelectedIndex();
        if (selected != 0) {
            data.setSupplierLocality(new LocalityDetail(locality.getItemText(selected),
                    locality.getValue(selected)));
        }
        data.setRatingFrom(this.getRatingFrom());
        data.setRatingTo(this.getRatingTo());
        return data;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public ListBox getCategoryList() {
        return category;
    }

    @Override
    public ListBox getLocalityList() {
        return locality;
    }

    public int getRatingFrom() {
        if (ratingFrom.getText().equals("")) {
            return -1;
        } else {
            return Integer.valueOf(ratingFrom.getText());
        }
    }

    public int getRatingTo() {
        if (ratingTo.getText().equals("")) {
            return -1;
        } else {
            return Integer.valueOf(ratingTo.getText());
        }
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

    @Override
    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getSupplierName().equals("")) {
            infoText.append("supplier:" + data.getSupplierName());
        }
        if (!data.getSupplierDescription().equals("")) {
            infoText.append("desc:" + data.getSupplierDescription());
        }
        if (data.getSupplierCategory() != null) {
            infoText.append("category:" + data.getSupplierCategory().getName());
        }
        if (data.getSupplierLocality() != null) {
            infoText.append("locality:" + data.getSupplierLocality().getName());
        }
        if (data.getRatingFrom() != 0) {
            infoText.append("ratingFrom:" + data.getRatingFrom());
        }
        if (data.getEndDate() != null) {
            infoText.append("ratingTo:" + data.getRatingTo());
        }
        infoHolder.setText(infoText.toString());
    }
}