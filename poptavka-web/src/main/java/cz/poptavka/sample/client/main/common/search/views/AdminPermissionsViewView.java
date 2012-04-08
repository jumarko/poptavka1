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
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;

public class AdminPermissionsViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminPermissionsViewView> {
    }
    @UiField
    TextBox idFrom, idTo, code, name, description;
    @UiField
    Button clearBtn;

    public AdminPermissionsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        if (!idFrom.getText().equals("")) {
            data.getFilters().add(new FilterItem("id", FilterItem.OPERATION_FROM, idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getFilters().add(new FilterItem("id", FilterItem.OPERATION_TO, idTo.getText()));
        }
        if (!code.getText().equals("")) {
            data.getFilters().add(new FilterItem("code", FilterItem.OPERATION_LIKE, code.getText()));
        }
        if (!name.getText().equals("")) {
            data.getFilters().add(new FilterItem("name", FilterItem.OPERATION_LIKE, name.getText()));
        }
        if (!description.getText().equals("")) {
            data.getFilters().add(new FilterItem("description", FilterItem.OPERATION_LIKE, description.getText()));
        }
        return data;
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
    public Widget getWidgetView() {
        return this;
    }
}