package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
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

public class MessagesTabViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, MessagesTabViewView> {
    }
    @UiField
    TextBox sender, subject, body;
    @UiField
    Button clearBtn;

    public MessagesTabViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        if (!sender.getText().equals("")) {
            data.getFilters().add(new FilterItem("companyName", FilterItem.OPERATION_LIKE, sender.getText()));
        }
        if (!subject.getText().equals("")) {
            data.getFilters().add(new FilterItem("subject", FilterItem.OPERATION_LIKE, subject.getText()));
        }
        if (!body.getText().equals("")) {
            data.getFilters().add(new FilterItem("body", FilterItem.OPERATION_LIKE, body.getText()));
        }
        return data;
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        sender.setText("");
        subject.setText("");
        body.setText("");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}