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
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import java.util.ArrayList;

/*
 * Nemoze byt Lazy pretoze sa advance search views neinicializuju.
 * Neviem zatial preco, ale nemoze byt lazy view, ktory inicializuje dalsi lazy view.
 */
public class AdminAccessRolesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminAccessRolesViewView> {
    }
    @UiField
    TextBox idFrom, idTo, code, roleName, roleDescription, permissions;
    @UiField
    Button clearBtn;

    public AdminAccessRolesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!idFrom.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_FROM, idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_TO, idTo.getText()));
        }
        if (!code.getText().equals("")) {
            filters.add(new FilterItem("code", FilterItem.OPERATION_EQUALS, code.getText()));
        }
        if (!roleName.getText().equals("")) {
            filters.add(new FilterItem("name", FilterItem.OPERATION_LIKE, roleName.getText()));
        }
        if (!roleDescription.getText().equals("")) {
            filters.add(new FilterItem("description", FilterItem.OPERATION_LIKE, roleDescription.getText()));
        }
        if (!permissions.getText().equals("")) {
            //split permissions by ';'
            filters.add(new FilterItem("permissions", FilterItem.OPERATION_IN, permissions.getText()));
        }
        return filters;
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
        code.setText("");
        roleName.setText("");
        roleDescription.setText("");
        permissions.setText("");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}