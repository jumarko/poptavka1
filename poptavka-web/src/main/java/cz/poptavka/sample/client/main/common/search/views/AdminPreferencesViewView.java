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

public class AdminPreferencesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminPreferencesViewView> {
    }
    @UiField
    TextBox idFrom, idTo, key, value, description;
    @UiField
    Button clearBtn;

    public AdminPreferencesViewView() {
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
        if (!key.getText().equals("")) {
            data.getFilters().add(new FilterItem("key", FilterItem.OPERATION_LIKE, key.getText()));
        }
        if (!value.getText().equals("")) {
            data.getFilters().add(new FilterItem("value", FilterItem.OPERATION_LIKE, value.getText()));
        }
        if (!description.getText().equals("")) {
            data.getFilters().add(new FilterItem("description", FilterItem.OPERATION_IN, description.getText()));
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
        key.setText("");
        value.setText("");
        description.setText("");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}