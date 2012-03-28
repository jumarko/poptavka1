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
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.demand.DemandType.Type;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class HomeDemandViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeDemandViewView> {
    }
    @UiField
    TextBox demandTitle, priceFrom, priceTo;
    @UiField
    ListBox category, locality, demandTypes, creationDate;
    @UiField
    DateBox finnishDate;
    @UiField
    Button clearBtn;

    @Override
    public void createView() {
//    public HomeDemandViewView() {
        initWidget(uiBinder.createAndBindUi(this));

        demandTypes.addItem(Storage.MSGS.select());
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
        data.initHomeDemands();
        if (!demandTitle.getText().equals("")) {
            data.getHomeDemands().setDemandTitle(demandTitle.getText());
        }
        if (demandTypes.getSelectedIndex() != 0) {
            data.getHomeDemands().setDemandType(demandTypes.getItemText(demandTypes.getSelectedIndex()));
        }
        int selected = category.getSelectedIndex();
        if (selected != 0) {
            data.getHomeDemands().setDemandCategory(new CategoryDetail(Long.valueOf(category.getValue(selected)),
                    category.getItemText(selected)));
        }
        selected = locality.getSelectedIndex();
        if (selected != 0) {
            data.getHomeDemands().setDemandLocality(new LocalityDetail(locality.getItemText(selected),
                    locality.getValue(selected)));
        }
        if (!priceFrom.getText().equals("")) {
            data.getHomeDemands().setPriceFrom(Integer.valueOf(priceFrom.getText()));
        }
        if (!priceTo.getText().equals("")) {
            data.getHomeDemands().setPriceTo(Integer.valueOf(priceTo.getText()));
        }
        data.getHomeDemands().setCreationDate(creationDate.getSelectedIndex());
        if (finnishDate.getValue() != null) {
            data.getHomeDemands().setEndDate(finnishDate.getValue());
        }
        return data;
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
        if (data.getHomeDemands().getDemandTitle() != null) {
            infoText.append("title:" + data.getHomeDemands().getDemandTitle());
        }
        if (data.getHomeDemands().getDemandType() != null) {
            infoText.append("type:" + data.getHomeDemands().getDemandType());
        }
        if (data.getHomeDemands().getDemandCategory() != null) {
            infoText.append("category:" + data.getHomeDemands().getDemandCategory().getName());
        }
        if (data.getHomeDemands().getDemandLocality() != null) {
            infoText.append("locality:" + data.getHomeDemands().getDemandLocality().getName());
        }
        if (data.getHomeDemands().getCreationDate() != null) {
            infoText.append("creationDate:" + creationDate.getItemText(data.getHomeDemands().getCreationDate()));
        }
        if (data.getHomeDemands().getEndDate() != null) {
            infoText.append("finnishDate:" + data.getHomeDemands().getEndDate().toString());
        }
        if (data.getHomeDemands().getPriceFrom() != null) {
            infoText.append("priceFrom:" + data.getHomeDemands().getPriceFrom());
        }
        if (data.getHomeDemands().getPriceTo() != null) {
            infoText.append("priceTo:" + data.getHomeDemands().getPriceTo());
        }
        infoHolder.setText(infoText.toString());
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

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        demandTitle.setText("");
        demandTypes.setSelectedIndex(0);
        category.setSelectedIndex(0);
        locality.setSelectedIndex(0);
        priceFrom.setText("");
        priceTo.setText("");
        creationDate.setSelectedIndex(0);
    }
}