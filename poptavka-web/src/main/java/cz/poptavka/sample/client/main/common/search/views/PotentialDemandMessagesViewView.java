package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

public class PotentialDemandMessagesViewView extends Composite {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, PotentialDemandMessagesViewView> {
    }
    @UiField
    TextBox sender, title, ratingFrom, ratingTo, priceFrom, priceTo;
    @UiField
    CheckBox isStar;
    @UiField
    ListBox urgent;
    @UiField
    DateBox createdFrom, createdTo;
    @UiField
    Button clearBtn;

    public PotentialDemandMessagesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        urgent.addItem("select urgention");
        urgent.addItem("less normal");
        urgent.addItem("normal");
        urgent.addItem("less urgent");
        urgent.addItem("urgent");
    }

    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initPotentialDemandMessages();
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
        if (urgent.getSelectedIndex() != 0) {
            data.getPotentialDemandMessages().setUrgention(urgent.getSelectedIndex());
        }
        return data;
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
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
            infoText.append(urgent.getItemText(data.getPotentialDemandMessages().getUrgention()));
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

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        sender.setText("");
        title.setText("");
        isStar.setText("");
        ratingFrom.setText("");
        ratingTo.setText("");
        priceFrom.setText("");
        priceTo.setText("");
        createdFrom.setValue(null);
        createdTo.setValue(null);
        urgent.setSelectedIndex(0);
    }
}