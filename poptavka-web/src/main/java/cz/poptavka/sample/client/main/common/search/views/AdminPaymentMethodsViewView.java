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

public class AdminPaymentMethodsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminPaymentMethodsViewView> {
    }
    @UiField
    TextBox idFrom, idTo, name, description;
    @UiField
    Button clearBtn;

    @Override
    public void createView() {
//    public AdminPaymentMethodsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminPaymentMethods();
        if (!idFrom.getText().equals("")) {
            data.getAdminPaymentMethods().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminPaymentMethods().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!name.getText().equals("")) {
            data.getAdminPaymentMethods().setName(name.getText());
        }
        if (!description.getText().equals("")) {
            data.getAdminPaymentMethods().setDescription(description.getText());
        }
        return data;
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminPaymentMethods().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminPaymentMethods().getIdFrom());
        }
        if (data.getAdminPaymentMethods().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminPaymentMethods().getIdTo());
        }
        if (data.getAdminPaymentMethods().getName() != null) {
            infoText.append("name:");
            infoText.append(data.getAdminPaymentMethods().getName());
        }
        if (data.getAdminPaymentMethods().getDescription() != null) {
            infoText.append("description:");
            infoText.append(data.getAdminPaymentMethods().getDescription());
        }
        infoHolder.setText(infoText.toString());
    }

    @UiHandler("idFrom")
    void validateIdFrom(ChangeEvent event) {
        if (!idFrom.getText().matches("[0-9]+")) {
            idFrom.setText("");
        }
    }

    @UiHandler("idTo")
    void validateIdTo(ChangeEvent event) {
        if (!idTo.getText().matches("[0-9]+")) {
            idTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        idFrom.setText("");
        idTo.setText("");
        name.setText("");
        description.setText("");
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