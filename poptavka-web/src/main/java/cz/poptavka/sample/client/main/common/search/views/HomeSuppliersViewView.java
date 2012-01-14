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
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class HomeSuppliersViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeSuppliersViewView> {
    }
    @UiField
    TextBox companyName, ratingFrom, ratingTo, supplierDescription;
    @UiField
    ListBox category, locality;
    @UiField
    Button clearBtn;

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
        data.initHomeSuppliers();
        if (!companyName.getText().equals("")) {
            data.getHomeSuppliers().setSupplierName(companyName.getText());
        }
        if (!supplierDescription.getText().equals("")) {
            data.getHomeSuppliers().setSupplierDescription(supplierDescription.getText());
        }
        int selected = category.getSelectedIndex();
        if (selected != 0) {
            data.getHomeSuppliers().setSupplierCategory(new CategoryDetail(Long.valueOf(category.getValue(selected)),
                    category.getItemText(selected)));
        }
        selected = locality.getSelectedIndex();
        if (selected != 0) {
            data.getHomeSuppliers().setSupplierLocality(new LocalityDetail(locality.getItemText(selected),
                    locality.getValue(selected)));
        }
        if (!ratingFrom.getText().equals("0")) {
            data.getHomeSuppliers().setRatingFrom(Integer.valueOf(ratingFrom.getText()));
        }
        if (!ratingTo.getText().equals("100")) {
            data.getHomeSuppliers().setRatingTo(Integer.valueOf(ratingTo.getText()));
        }
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

    @Override
    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getHomeSuppliers().getSupplierName() != null) {
            infoText.append("supplier:" + data.getHomeSuppliers().getSupplierName());
        }
        if (data.getHomeSuppliers().getSupplierDescription() != null) {
            infoText.append("desc:" + data.getHomeSuppliers().getSupplierDescription());
        }
        if (data.getHomeSuppliers().getSupplierCategory() != null) {
            infoText.append("category:" + data.getHomeSuppliers().getSupplierCategory().getName());
        }
        if (data.getHomeSuppliers().getSupplierLocality() != null) {
            infoText.append("locality:" + data.getHomeSuppliers().getSupplierLocality().getName());
        }
        if (data.getHomeSuppliers().getRatingFrom() != null) {
            infoText.append("ratingFrom:" + data.getHomeSuppliers().getRatingFrom());
        }
        if (data.getHomeSuppliers().getRatingTo() != null) {
            infoText.append("ratingTo:" + data.getHomeSuppliers().getRatingTo());
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

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        companyName.setText("");
        supplierDescription.setText("");
        category.setSelectedIndex(0);
        locality.setSelectedIndex(0);
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }
}