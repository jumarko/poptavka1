package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;

public class PotentialDemandMessagesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, PotentialDemandMessagesViewView> {
    }
    @UiField
    TextBox idFrom, idTo, sender, title, ratingFrom, ratingTo, priceFrom, priceTo;
    @UiField
    CheckBox isStar;
    @UiField
    ListBox urgent;
    @UiField
    DateBox createdFrom, createdTo;

//    @Override
//    public void createView() {
    public PotentialDemandMessagesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initPotentialDemandMessages();
        if (!idFrom.getText().equals("")) {
            data.getPotentialDemandMessages().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getPotentialDemandMessages().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!sender.getText().equals("")) {
            data.getPotentialDemandMessages().setSender(sender.getText());
        }
        if (!title.getText().equals("")) {
            data.getPotentialDemandMessages().setDemandTitle(title.getText());
        }
        if (!isStar.getText().equals("")) {
            data.getPotentialDemandMessages().setIsStared(isStar.getValue());
        }
        if (!ratingFrom.getText().equals("")) {
            data.getPotentialDemandMessages().setRatingFrom(Integer.valueOf(ratingFrom.getText()));
        }
        if (!ratingTo.getText().equals("")) {
            data.getPotentialDemandMessages().setRatingTo(Integer.valueOf(ratingTo.getText()));
        }
        if (!priceFrom.getText().equals("")) {
            data.getPotentialDemandMessages().setPriceFrom(Integer.valueOf(priceFrom.getText()));
        }
        if (!priceTo.getText().equals("")) {
            data.getPotentialDemandMessages().setPriceTo(Integer.valueOf(priceTo.getText()));
        }
        if (createdFrom.getValue() != null) {
            data.getPotentialDemandMessages().setCreatedFrom(createdFrom.getValue());
        }
        if (createdTo.getValue() != null) {
            data.getPotentialDemandMessages().setCreatedTo(createdTo.getValue());
        }
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
        if (data.getPotentialDemandMessages().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getPotentialDemandMessages().getIdFrom());
        }
        if (data.getPotentialDemandMessages().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getPotentialDemandMessages().getIdTo());
        }
        if (data.getPotentialDemandMessages().getSender() != null) {
            infoText.append("sender:");
            infoText.append(data.getPotentialDemandMessages().getSender());
        }
        if (data.getPotentialDemandMessages().getDemandTitle() != null) {
            infoText.append("title:");
            infoText.append(data.getPotentialDemandMessages().getDemandTitle());
        }
        if (data.getPotentialDemandMessages().getIsStared() != null) {
            infoText.append("star:");
            infoText.append(data.getPotentialDemandMessages().getIsStared().toString());
        }
        if (data.getPotentialDemandMessages().getUrgention() != null) {
            infoText.append("urgent:");
            infoText.append(data.getPotentialDemandMessages().getUrgention());
        }
        if (data.getPotentialDemandMessages().getCreatedFrom() != null) {
            infoText.append("createdFrom:");
            infoText.append(data.getPotentialDemandMessages().getCreatedFrom().toString());
        }
        if (data.getPotentialDemandMessages().getCreatedTo() != null) {
            infoText.append("createdTo:");
            infoText.append(data.getPotentialDemandMessages().getCreatedTo().toString());
        }
        if (data.getPotentialDemandMessages().getRatingFrom() != null) {
            infoText.append("ratingFrom:");
            infoText.append(data.getPotentialDemandMessages().getRatingFrom());
        }
        if (data.getPotentialDemandMessages().getRatingTo() != null) {
            infoText.append("ratingTo:");
            infoText.append(data.getPotentialDemandMessages().getRatingTo());
        }
        if (data.getPotentialDemandMessages().getPriceFrom() != null) {
            infoText.append("priceFrom:");
            infoText.append(data.getPotentialDemandMessages().getPriceFrom());
        }
        if (data.getPotentialDemandMessages().getPriceTo() != null) {
            infoText.append("priceTo:");
            infoText.append(data.getPotentialDemandMessages().getPriceTo());
        }
        infoHolder.setText(infoText.toString());
    }

    @UiHandler("idFrom")
    void validatePriceFrom(ChangeEvent event) {
        if (!idFrom.getText().matches("[0-9]+")) {
            idFrom.setText("");
        }
    }

    @UiHandler("idTo")
    void validatePriceTo(ChangeEvent event) {
        if (!idTo.getText().matches("[0-9]+")) {
            idTo.setText("");
        }
    }
}