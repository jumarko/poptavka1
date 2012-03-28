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
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.demand.DemandType.Type;

public class AdminDemandsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminDemandsViewView> {
    }
    @UiField
    TextBox demandIdFrom, demandIdTo, clientIdFrom, clientIdTo, demandTitle;
    @UiField
    DateBox expirationDateFrom, expirationDateTo, endDateFrom, endDateTo;
    @UiField
    ListBox demandType, demandStatus;
    @UiField
    Button clearBtn;

    @Override
    public void createView() {
//    public AdminDemandsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        demandType.addItem(Storage.MSGS.select());
        for (Type type : DemandType.Type.values()) {
            demandType.addItem(type.name());
        }
        demandStatus.addItem(Storage.MSGS.select());
        for (DemandStatus status : DemandStatus.values()) {
            demandStatus.addItem(status.name());
        }
    }

    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminDemands();
        if (!demandIdFrom.getText().equals("")) {
            data.getAdminDemands().setDemandIdFrom(Long.valueOf(demandIdFrom.getText()));
        }
        if (!demandIdTo.getText().equals("")) {
            data.getAdminDemands().setDemandIdTo(Long.valueOf(demandIdTo.getText()));
        }
        if (!clientIdFrom.getText().equals("")) {
            data.getAdminDemands().setClientIdFrom(Long.valueOf(clientIdFrom.getText()));
        }
        if (!clientIdTo.getText().equals("")) {
            data.getAdminDemands().setClientIdTo(Long.valueOf(clientIdTo.getText()));
        }
        if (!demandTitle.getText().equals("")) {
            data.getAdminDemands().setDemandTitle(demandTitle.getText());
        }
        if (expirationDateFrom.getValue() != null) {
            data.getAdminDemands().setExpirationDateFrom(expirationDateFrom.getValue());
        }
        if (expirationDateTo.getValue() != null) {
            data.getAdminDemands().setExpirationDateTo(expirationDateTo.getValue());
        }
        if (endDateFrom.getValue() != null) {
            data.getAdminDemands().setEndDateFrom(endDateFrom.getValue());
        }
        if (endDateTo.getValue() != null) {
            data.getAdminDemands().setEndDateFrom(endDateTo.getValue());
        }
        if (demandType.getSelectedIndex() != 0) {
            data.getAdminDemands().setDemandType(demandType.getItemText(demandType.getSelectedIndex()));
        }
        if (demandStatus.getSelectedIndex() != 0) {
            data.getAdminDemands().setDemandStatus(demandStatus.getItemText(demandStatus.getSelectedIndex()));
        }
        return data;
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminDemands().getDemandIdFrom() != null) {
            infoText.append("demandIdFrom:");
            infoText.append(data.getAdminDemands().getDemandIdFrom());
        }
        if (data.getAdminDemands().getDemandIdTo() != null) {
            infoText.append("demandIdTo:");
            infoText.append(data.getAdminDemands().getDemandIdTo());
        }
        if (data.getAdminDemands().getClientIdFrom() != null) {
            infoText.append("clientIdFrom:");
            infoText.append(data.getAdminDemands().getClientIdFrom());
        }
        if (data.getAdminDemands().getClientIdTo() != null) {
            infoText.append("clientIdTo:");
            infoText.append(data.getAdminDemands().getClientIdTo());
        }
        if (data.getAdminDemands().getDemandTitle() != null) {
            infoText.append("title:");
            infoText.append(data.getAdminDemands().getDemandTitle());
        }
        if (data.getAdminDemands().getDemandType() != null) {
            infoText.append("type:");
            infoText.append(data.getAdminDemands().getDemandType());
        }
        if (data.getAdminDemands().getDemandStatus() != null) {
            infoText.append("status:");
            infoText.append(data.getAdminDemands().getDemandStatus());
        }
        if (data.getAdminDemands().getExpirationDateFrom() != null) {
            infoText.append("expFrom:");
            infoText.append(data.getAdminDemands().getExpirationDateFrom());
        }
        if (data.getAdminDemands().getExpirationDateTo() != null) {
            infoText.append("expTo:");
            infoText.append(data.getAdminDemands().getExpirationDateTo());
        }
        if (data.getAdminDemands().getEndDateFrom() != null) {
            infoText.append("endDateFrom:");
            infoText.append(data.getAdminDemands().getEndDateFrom());
        }
        if (data.getAdminDemands().getEndDateTo() != null) {
            infoText.append("endDateTo:");
            infoText.append(data.getAdminDemands().getEndDateTo());
        }
        infoHolder.setText(infoText.toString());
    }

    @UiHandler("demandIdFrom")
    void validatePriceFrom(ChangeEvent event) {
        if (!demandIdFrom.getText().matches("[0-9]+")) {
            demandIdFrom.setText("");
        }
    }

    @UiHandler("demandIdTo")
    void validatePriceTo(ChangeEvent event) {
        if (!demandIdTo.getText().matches("[0-9]+")) {
            demandIdTo.setText("");
        }
    }

    @UiHandler("clientIdFrom")
    void validateRatingFrom(ChangeEvent event) {
        if (!clientIdFrom.getText().matches("[0-9]+")) {
            clientIdFrom.setText("");
        }
    }

    @UiHandler("clientIdTo")
    void validateratingTo(ChangeEvent event) {
        if (!clientIdTo.getText().matches("[0-9]+")) {
            clientIdTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        demandIdFrom.setText("");
        demandIdTo.setText("");
        clientIdFrom.setText("");
        clientIdTo.setText("");
        demandTitle.setText("");
        expirationDateFrom.setValue(null);
        expirationDateTo.setValue(null);
        endDateFrom.setValue(null);
        endDateTo.setValue(null);
        demandType.setSelectedIndex(0);
        demandStatus.setSelectedIndex(0);
    }

    @Override
    public ListBox getCategoryList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListBox getLocalityList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}