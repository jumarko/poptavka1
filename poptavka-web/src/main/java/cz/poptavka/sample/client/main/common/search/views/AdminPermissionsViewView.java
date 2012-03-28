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

public class AdminPermissionsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminPermissionsViewView> {
    }
    @UiField
    TextBox idFrom, idTo, code, name, description;
    @UiField
    Button clearBtn;

    @Override
    public void createView() {
//    public AdminPermissionsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminPermissions();
        if (!idFrom.getText().equals("")) {
            data.getAdminPermissions().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminPermissions().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!code.getText().equals("")) {
            data.getAdminPermissions().setCode(code.getText());
        }
        if (!name.getText().equals("")) {
            data.getAdminPermissions().setName(name.getText());
        }
        if (!description.getText().equals("")) {
            data.getAdminPermissions().setDescription(description.getText());
        }
        return data;
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminPermissions().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminPermissions().getIdFrom());
        }
        if (data.getAdminPermissions().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminPermissions().getIdTo());
        }
        if (data.getAdminPermissions().getName() != null) {
            infoText.append("name:");
            infoText.append(data.getAdminPermissions().getName());
        }
        if (data.getAdminPermissions().getCode() != null) {
            infoText.append("code:");
            infoText.append(data.getAdminPermissions().getCode());
        }
        if (data.getAdminPermissions().getDescription() != null) {
            infoText.append("description:");
            infoText.append(data.getAdminPermissions().getDescription());
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
        code.setText("");
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