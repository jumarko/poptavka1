package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

public class AdminEmailActivationViewView extends Composite {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminEmailActivationViewView> {
    }
    @UiField
    TextBox idFrom, idTo, activationLink;
    @UiField
    DateBox timeoutFrom, timeoutTo;
    @UiField
    Button clearBtn;

//    @Override
//    public void createView() {
    public AdminEmailActivationViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminEmailActivation();
        if (!idFrom.getText().equals("")) {
            data.getAdminEmailActivation().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminEmailActivation().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!activationLink.getText().equals("")) {
            data.getAdminEmailActivation().setActivationLink(activationLink.getText());
        }
        if (timeoutFrom.getValue() != null) {
            data.getAdminEmailActivation().setTimeoutFrom(timeoutFrom.getValue());
        }
        if (timeoutTo.getValue() != null) {
            data.getAdminEmailActivation().setTimeoutTo(timeoutTo.getValue());
        }
        return data;
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminEmailActivation().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminEmailActivation().getIdFrom());
        }
        if (data.getAdminEmailActivation().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminEmailActivation().getIdTo());
        }
        if (data.getAdminEmailActivation().getActivationLink() != null) {
            infoText.append("activationLink:");
            infoText.append(data.getAdminEmailActivation().getActivationLink());
        }
        if (data.getAdminEmailActivation().getTimeoutFrom() != null) {
            infoText.append("timeoutFrom:");
            infoText.append(data.getAdminEmailActivation().getTimeoutFrom());
        }
        if (data.getAdminEmailActivation().getTimeoutTo() != null) {
            infoText.append("timeoutTo:");
            infoText.append(data.getAdminEmailActivation().getTimeoutTo());
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

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        idFrom.setText("");
        idTo.setText("");
        activationLink.setText("");
        timeoutFrom.setValue(null);
        timeoutTo.setValue(null);
    }
}