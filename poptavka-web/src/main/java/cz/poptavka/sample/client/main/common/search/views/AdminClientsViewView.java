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

public class AdminClientsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminClientsViewView> {
    }
    @UiField
    TextBox idFrom, idTo, companyName, firstName, lastName, ratingFrom, ratingTo;
    @UiField
    Button clearBtn;

//    @Override
//    public void createView() {
    public AdminClientsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminClients();
        if (!idFrom.getText().equals("")) {
            data.getAdminClients().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminClients().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!companyName.getText().equals("")) {
            data.getAdminClients().setCompanyName(companyName.getText());
        }
        if (!firstName.getText().equals("")) {
            data.getAdminClients().setFirstName(firstName.getText());
        }
        if (!lastName.getText().equals("")) {
            data.getAdminClients().setLastName(lastName.getText());
        }
        if (!ratingFrom.getText().equals("0")) {
            data.getAdminClients().setRatingFrom(Integer.valueOf(ratingFrom.getText()));
        }
        if (!ratingTo.getText().equals("100")) {
            data.getAdminClients().setRatingTo(Integer.valueOf(ratingTo.getText()));
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
        if (data.getAdminClients().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminClients().getIdFrom());
        }
        if (data.getAdminClients().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminClients().getIdTo());
        }
        if (data.getAdminClients().getCompanyName() != null) {
            infoText.append("companyName:");
            infoText.append(data.getAdminClients().getCompanyName());
        }
        if (data.getAdminClients().getFirstName() != null) {
            infoText.append("firstName:");
            infoText.append(data.getAdminClients().getFirstName());
        }
        if (data.getAdminClients().getLastName() != null) {
            infoText.append("lastName:");
            infoText.append(data.getAdminClients().getLastName());
        }
        if (data.getAdminClients().getRatingFrom() != null) {
            infoText.append("ratingFrom:");
            infoText.append(data.getAdminClients().getRatingFrom().toString());
        }
        if (data.getAdminClients().getRatingTo() != null) {
            infoText.append("ratingTo:");
            infoText.append(data.getAdminClients().getRatingTo().toString());
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

    @UiHandler("ratingFrom")
    void validateRatingFrom(ChangeEvent event) {
        if (!ratingFrom.getText().matches("[0-9]+")) {
            ratingFrom.setText("0");
        }
    }

    @UiHandler("ratingTo")
    void validateratingTo(ChangeEvent event) {
        if (!ratingTo.getText().matches("[0-9]+")) {
            ratingTo.setText("100");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        idFrom.setText("");
        idTo.setText("");
        companyName.setText("");
        firstName.setText("");
        lastName.setText("");
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }
}