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

public class AdminPreferencesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminPreferencesViewView> {
    }
    @UiField
    TextBox idFrom, idTo, key, value, description;
    @UiField
    Button clearBtn;

    @Override
    public void createView() {
//    public AdminPreferencesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminPreferences();
        if (!idFrom.getText().equals("")) {
            data.getAdminPreferences().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminPreferences().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!key.getText().equals("")) {
            data.getAdminPreferences().setKey(key.getText());
        }
        if (!value.getText().equals("")) {
            data.getAdminPreferences().setValue(value.getText());
        }
        if (!description.getText().equals("")) {
            data.getAdminPreferences().setDescription(description.getText());
        }
        return data;
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminPreferences().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminPreferences().getIdFrom());
        }
        if (data.getAdminPreferences().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminPreferences().getIdTo());
        }
        if (data.getAdminPreferences().getKey() != null) {
            infoText.append("key:");
            infoText.append(data.getAdminPreferences().getKey());
        }
        if (data.getAdminPreferences().getValue() != null) {
            infoText.append("value:");
            infoText.append(data.getAdminPreferences().getValue());
        }
        if (data.getAdminPreferences().getDescription() != null) {
            infoText.append("description:");
            infoText.append(data.getAdminPreferences().getDescription());
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
        key.setText("");
        value.setText("");
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