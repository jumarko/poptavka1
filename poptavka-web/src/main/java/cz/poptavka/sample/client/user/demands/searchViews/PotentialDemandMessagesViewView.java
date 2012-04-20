package cz.poptavka.sample.client.user.demands.searchViews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import java.util.ArrayList;

public class PotentialDemandMessagesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, PotentialDemandMessagesViewView> {
    }
    @UiField
    TextBox sender, title, ratingFrom, ratingTo, priceFrom, priceTo;
    @UiField
    CheckBox isStar;
    @UiField
    ListBox urgent;
    @UiField
    DateBox createdFrom, createdTo;
    @UiField
    Button clearBtn;

    public PotentialDemandMessagesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        urgent.addItem("select urgention");
        urgent.addItem("less normal");
        urgent.addItem("normal");
        urgent.addItem("less urgent");
        urgent.addItem("urgent");
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!sender.getText().equals("")) {
            filters.add(new FilterItem("companyName", FilterItem.OPERATION_LIKE, sender.getText()));
        }
        if (!title.getText().equals("")) {
            filters.add(new FilterItem("title", FilterItem.OPERATION_LIKE, title.getText()));
        }
        if (!isStar.getText().equals("")) {
            filters.add(new FilterItem("star", FilterItem.OPERATION_EQUALS, isStar.getValue()));
        }
        if (!ratingFrom.getText().equals("")) {
            //rating vs overalRating
            filters.add(new FilterItem("rating", FilterItem.OPERATION_FROM, ratingFrom.getText()));
        }
        if (!ratingTo.getText().equals("")) {
            filters.add(new FilterItem("rating", FilterItem.OPERATION_TO, ratingTo.getText()));
        }
        if (!priceFrom.getText().equals("")) {
            filters.add(new FilterItem("price", FilterItem.OPERATION_FROM, priceFrom.getText()));
        }
        if (!priceTo.getText().equals("")) {
            filters.add(new FilterItem("price", FilterItem.OPERATION_TO, priceTo.getText()));
        }
        if (createdFrom.getValue() != null) {
            filters.add(new FilterItem("created", FilterItem.OPERATION_FROM, createdFrom.getValue()));
        }
        if (createdTo.getValue() != null) {
            filters.add(new FilterItem("created", FilterItem.OPERATION_TO, createdTo.getValue()));
        }
        if (urgent.getSelectedIndex() != 0) {
            filters.add(new FilterItem("urgent", FilterItem.OPERATION_EQUALS, urgent.getSelectedIndex()));
        }
        return filters;
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        sender.setText("");
        title.setText("");
        isStar.setText("");
        ratingFrom.setText("");
        ratingTo.setText("");
        priceFrom.setText("");
        priceTo.setText("");
        createdFrom.setValue(null);
        createdTo.setValue(null);
        urgent.setSelectedIndex(0);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}