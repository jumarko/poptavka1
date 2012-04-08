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
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;

public class AdminEmailActivationViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminEmailActivationViewView> {
    }
    @UiField
    TextBox idFrom, idTo, activationLink;
    @UiField
    DateBox timeoutFrom, timeoutTo;
    @UiField
    Button clearBtn;

    public AdminEmailActivationViewView() {
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
        if (!activationLink.getText().equals("")) {
            data.getFilters().add(new FilterItem("activationLink",
                    FilterItem.OPERATION_LIKE, activationLink.getText()));
        }
        if (timeoutFrom.getValue() != null) {
            data.getFilters().add(new FilterItem("timeout", FilterItem.OPERATION_FROM, timeoutFrom.getValue()));
        }
        if (timeoutTo.getValue() != null) {
            data.getFilters().add(new FilterItem("timeout", FilterItem.OPERATION_FROM, timeoutTo.getValue()));
        }
        return data;
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

    @Override
    public Widget getWidgetView() {
        return this;
    }
}