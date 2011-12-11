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
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.demand.DemandType.Type;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class HomeDemandViewView extends Composite implements SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeDemandViewView> {
    }
    @UiField
    UListElement searchList;
//    @UiField
//    Button searchBtn;
    @UiField
    TextBox demandTitle, priceFrom, priceTo;
    @UiField
    ListBox category, locality, demandTypes, creationDate;
    @UiField
    DateBox finnishDate;

//    @Override
//    public void createView() {
    public HomeDemandViewView() {
        initWidget(uiBinder.createAndBindUi(this));

        for (Type type : DemandType.Type.values()) {
            demandTypes.addItem(type.name());
        }
        creationDate.addItem(Storage.MSGS.today());
        creationDate.addItem(Storage.MSGS.yesterday());
        creationDate.addItem(Storage.MSGS.lastWeek());
        creationDate.addItem(Storage.MSGS.lastMonth());
        creationDate.addItem(Storage.MSGS.noLimits());
        creationDate.setSelectedIndex(4);
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
//        data.setType("homeDemands");
        data.setDemandTitle(demandTitle.getText());
        data.setDemandType(demandTypes.getItemText(demandTypes.getSelectedIndex()));
        int selected = category.getSelectedIndex();
        if (selected != 0) {
            data.setDemandCategory(new CategoryDetail(Long.valueOf(category.getValue(selected)),
                    category.getItemText(selected)));
        }
        selected = locality.getSelectedIndex();
        if (selected != 0) {
            data.setDemandLocality(new LocalityDetail(locality.getItemText(selected),
                    locality.getValue(selected)));
        }
        data.setPriceFrom(this.getPriceFrom());
        data.setPriceTo(this.getPriceTo());
        data.setCreationDate(creationDate.getSelectedIndex());
        data.setEndDate(finnishDate.getValue());
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

    public int getPriceFrom() {
        if (priceFrom.getText().equals("")) {
            return -1;
        } else {
            return Integer.valueOf(priceFrom.getText());
        }
    }

    public int getPriceTo() {
        if (priceTo.getText().equals("")) {
            return -1;
        } else {
            return Integer.valueOf(priceTo.getText());
        }
    }

    @UiHandler("priceFrom")
    void validatePriceFrom(ChangeEvent event) {
        if (!priceFrom.getText().matches("[0-9]+")) {
            priceFrom.setText("");
        }
    }

    @UiHandler("priceTo")
    void validatePriceTo(ChangeEvent event) {
        if (!priceTo.getText().matches("[0-9]+")) {
            priceTo.setText("");
        }
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getDemandTitle().equals("")) {
            infoText.append("title:" + data.getDemandTitle());
        }
        if (!data.getDemandType().equals("")) {
            infoText.append("type:" + data.getDemandType());
        }
        if (data.getDemandCategory() != null) {
            infoText.append("category:" + data.getDemandCategory().getName());
        }
        if (data.getDemandLocality() != null) {
            infoText.append("locality:" + data.getDemandLocality().getName());
        }
        if (data.getCreationDate() != 4) {
            infoText.append("creationDate:" + creationDate.getItemText(data.getCreationDate()));
        }
        if (data.getEndDate() != null) {
            infoText.append("finnishDate:" + data.getEndDate().toString());
        }
        if (data.getPriceFrom() != 0) {
            infoText.append("priceFrom:" + data.getPriceFrom());
        }
        if (data.getPriceTo() != 0) {
            infoText.append("priceTo:" + data.getPriceTo());
        }
        infoHolder.setText(infoText.toString());
    }
}