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
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;

public class AdminAccessRolesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminAccessRolesViewView> {
    }
    @UiField
    TextBox idFrom, idTo, code, roleName, roleDescription, permissions;

//    @Override
//    public void createView() {
    public AdminAccessRolesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminAccessRoles();
        if (!idFrom.getText().equals("")) {
            data.getAdminAccessRoles().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminAccessRoles().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!code.getText().equals("")) {
            data.getAdminAccessRoles().setCode(code.getText());
        }
        if (!roleName.getText().equals("")) {
            data.getAdminAccessRoles().setRoleName(roleName.getText());
        }
        if (!roleDescription.getText().equals("")) {
            data.getAdminAccessRoles().setRoleDescription(roleDescription.getText());
        }
        if (!permissions.getText().equals("")) {
            data.getAdminAccessRoles().setPermisstions(permissions.getText().split(";"));
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
        if (data.getAdminAccessRoles().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminAccessRoles().getIdFrom());
        }
        if (data.getAdminAccessRoles().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminAccessRoles().getIdTo());
        }
        if (data.getAdminAccessRoles().getCode() != null) {
            infoText.append("code:");
            infoText.append(data.getAdminAccessRoles().getCode());
        }
        if (data.getAdminAccessRoles().getRoleName() != null) {
            infoText.append("roleName:");
            infoText.append(data.getAdminAccessRoles().getRoleName());
        }
        if (data.getAdminAccessRoles().getRoleDescription() != null) {
            infoText.append("roleDescription:");
            infoText.append(data.getAdminAccessRoles().getRoleDescription());
        }
        if (data.getAdminAccessRoles().getPermisstions() != null) {
            infoText.append("permissions:");
            infoText.append(data.getAdminAccessRoles().getPermisstions().toString());
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