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
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.demand.DemandType.Type;

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
    DateBox finnishDateFrom, finnishDateTo;
    @UiField
    Button clearBtn;

    public HomeDemandViewView() {
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
        if (!demandTitle.getText().equals("")) {
            data.getFilters().add(new FilterItem("title", FilterItem.OPERATION_LIKE, demandTitle.getText()));
        }
        if (demandTypes.getSelectedIndex() != 0) {
            data.getFilters().add(new FilterItem("type", FilterItem.OPERATION_EQUALS,
                    demandTypes.getItemText(demandTypes.getSelectedIndex())));
        }
        if (!priceFrom.getText().equals("")) {
            data.getFilters().add(new FilterItem("price", FilterItem.OPERATION_FROM, priceFrom.getText()));
        }
        if (!priceTo.getText().equals("")) {
            data.getFilters().add(new FilterItem("price", FilterItem.OPERATION_TO, priceTo.getText()));
        }
        data.getFilters().add(new FilterItem("creation", FilterItem.OPERATION_FROM, creationDate.getSelectedIndex()));
        if (finnishDateFrom.getValue() != null) {
            data.getFilters().add(new FilterItem("id", FilterItem.OPERATION_FROM, finnishDateFrom.getValue()));
        }
        if (finnishDateTo.getValue() != null) {
            data.getFilters().add(new FilterItem("id", FilterItem.OPERATION_TO, finnishDateTo.getValue()));
        }
        return data;
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

    @Override
    public Widget getWidgetView() {
        return this;
    }
}