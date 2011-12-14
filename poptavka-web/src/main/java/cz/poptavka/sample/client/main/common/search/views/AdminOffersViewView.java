package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.domain.offer.OfferState;
import cz.poptavka.sample.domain.offer.OfferState.Type;

public class AdminOffersViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminOffersViewView> {
    }
    @UiField
    TextBox offerIdFrom, offerIdTo, demandIdFrom, demandIdTo, supplierIdFrom,
    supplierIdTo, priceFrom, priceTo;
    @UiField
    DateBox createdFrom, createdTo, finnishFrom, finnishTo;
    @UiField
    ListBox state;

    //    @Override
//    public void createView() {
    public AdminOffersViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        for (Type type : OfferState.Type.values()) {
            state.addItem(type.name());
        }
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminOffers();
        if (!offerIdFrom.getText().equals("")) {
            data.getAdminOffers().setOfferIdFrom(Long.valueOf(offerIdFrom.getText()));
        }
        if (!offerIdTo.getText().equals("")) {
            data.getAdminOffers().setOfferIdTo(Long.valueOf(offerIdTo.getText()));
        }
        if (!demandIdFrom.getText().equals("")) {
            data.getAdminOffers().setDemandIdFrom(Long.valueOf(demandIdFrom.getText()));
        }
        if (!demandIdTo.getText().equals("")) {
            data.getAdminOffers().setDemandIdTo(Long.valueOf(demandIdTo.getText()));
        }
        if (!supplierIdFrom.getText().equals("")) {
            data.getAdminOffers().setSupplierIdFrom(Long.valueOf(supplierIdFrom.getText()));
        }
        if (!supplierIdTo.getText().equals("")) {
            data.getAdminOffers().setSupplierIdTo(Long.valueOf(supplierIdTo.getText()));
        }
        if (!priceFrom.getText().equals("")) {
            data.getAdminOffers().setPriceFrom(Integer.valueOf(priceFrom.getText()));
        }
        if (!priceTo.getText().equals("")) {
            data.getAdminOffers().setPriceTo(Integer.valueOf(priceTo.getText()));
        }
        if (createdFrom.getValue() != null) {
            data.getAdminOffers().setCreatedFrom(createdFrom.getValue());
        }
        if (createdTo.getValue() != null) {
            data.getAdminOffers().setCreatedTo(createdTo.getValue());
        }
        if (finnishFrom.getValue() != null) {
            data.getAdminOffers().setFinnishFrom(finnishFrom.getValue());
        }
        if (finnishTo.getValue() != null) {
            data.getAdminOffers().setFinnishTo(finnishTo.getValue());
        }
        data.getAdminOffers().setState(state.getItemText(state.getSelectedIndex()));
        return data;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public ListBox getCategoryList() {
        return null;
    }

    @Override
    public ListBox getLocalityList() {
        return null;
    }

    @Override
    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminOffers().getOfferIdFrom() != null) {
            infoText.append("offerIdFrom:");
            infoText.append(data.getAdminOffers().getOfferIdFrom());
        }
        if (data.getAdminOffers().getOfferIdTo() != null) {
            infoText.append("offerIdTo:");
            infoText.append(data.getAdminOffers().getOfferIdTo());
        }
        if (data.getAdminOffers().getDemandIdFrom() != null) {
            infoText.append("demandIdFrom:");
            infoText.append(data.getAdminOffers().getDemandIdTo());
        }
        if (data.getAdminOffers().getDemandIdTo() != null) {
            infoText.append("demandIdTo:");
            infoText.append(data.getAdminOffers().getDemandIdTo());
        }
        if (data.getAdminOffers().getSupplierIdFrom() != null) {
            infoText.append("supplierIdFrom:");
            infoText.append(data.getAdminOffers().getSupplierIdFrom());
        }
        if (data.getAdminOffers().getSupplierIdTo() != null) {
            infoText.append("supplierIdTo:");
            infoText.append(data.getAdminOffers().getSupplierIdTo());
        }
        if (data.getAdminOffers().getPriceFrom() != null) {
            infoText.append("priceFrom:");
            infoText.append(data.getAdminOffers().getPriceFrom());
        }
        if (data.getAdminOffers().getPriceTo() != null) {
            infoText.append("priceTo:");
            infoText.append(data.getAdminOffers().getPriceTo());
        }
        if (data.getAdminOffers().getCreatedFrom() != null) {
            infoText.append("createdFrom:");
            infoText.append(data.getAdminOffers().getCreatedFrom());
        }
        if (data.getAdminOffers().getCreatedTo() != null) {
            infoText.append("createdTo:");
            infoText.append(data.getAdminOffers().getCreatedTo());
        }
        if (data.getAdminOffers().getFinnishFrom() != null) {
            infoText.append("finnishFrom:");
            infoText.append(data.getAdminOffers().getFinnishFrom());
        }
        if (data.getAdminOffers().getFinnishTo() != null) {
            infoText.append("finnishTo:");
            infoText.append(data.getAdminOffers().getFinnishTo());
        }
        if (data.getAdminOffers().getState() != null) {
            infoText.append("state:");
            infoText.append(data.getAdminOffers().getState());
        }

        infoHolder.setText(infoText.toString());
    }

    @UiHandler("offerIdFrom")
    void validateOfferIdFrom(ChangeEvent event) {
        if (!offerIdFrom.getText().matches("[0-9]+")) {
            offerIdFrom.setText("");
        }
    }

    @UiHandler("offerIdTo")
    void validateOfferIdTo(ChangeEvent event) {
        if (!offerIdTo.getText().matches("[0-9]+")) {
            offerIdTo.setText("");
        }
    }

    @UiHandler("demandIdFrom")
    void validateDemandIdFrom(ChangeEvent event) {
        if (!demandIdFrom.getText().matches("[0-9]+")) {
            demandIdFrom.setText("");
        }
    }

    @UiHandler("demandIdTo")
    void validateDemandIdTo(ChangeEvent event) {
        if (!demandIdTo.getText().matches("[0-9]+")) {
            demandIdTo.setText("");
        }
    }

    @UiHandler("supplierIdFrom")
    void validateSupplierIdFrom(ChangeEvent event) {
        if (!supplierIdFrom.getText().matches("[0-9]+")) {
            supplierIdFrom.setText("");
        }
    }

    @UiHandler("supplierIdTo")
    void validateSupplierIdTo(ChangeEvent event) {
        if (!supplierIdTo.getText().matches("[0-9]+")) {
            supplierIdTo.setText("");
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
}