package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class SearchView extends Composite implements SearchPresenter.SearchViewInterface {

    private static AdvancedSearchViewUiBinder uiBinder = GWT.create(AdvancedSearchViewUiBinder.class);
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    interface AdvancedSearchViewUiBinder extends UiBinder<Widget, SearchView> {
    }
    @UiField
    UListElement searchList;
    @UiField
    ListBox category, locality, where;
    @UiField
    Button searchBtn, searchAdvBtn;
    @UiField
    TextBox content;

    public SearchView() {
        initWidget(uiBinder.createAndBindUi(this));

        category.addItem(MSGS.allCategories());
        locality.addItem(MSGS.allLocalities());
        where.addItem(MSGS.searchInDemands());
        where.addItem(MSGS.searchInSuppliers());
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    // Buttons
    @Override
    public Button getSearchBtn() {
        return searchBtn;
    }

    @Override
    public Button getSearchAdvBtn() {
        return searchAdvBtn;
    }

    // Items
    @Override
    public String getContent() {
        if (content.getText().equals(MSGS.searchContent())) {
            return "";
        } else {
            return content.getText();
        }
    }

    @Override
    public ListBox getWhere() {
        return where;
    }

    @Override
    public ListBox getCategory() {
        return category;
    }

    @Override
    public ListBox getLocality() {
        return locality;
    }

    @Override
    public SearchDataHolder getFilter() {
        SearchDataHolder data = new SearchDataHolder();

        data.setText(this.getContent());
        data.setWhere(where.getSelectedIndex());
        if (category.getSelectedIndex() != 0) {
            data.setCategory(new CategoryDetail(
                    Long.valueOf(category.getValue(category.getSelectedIndex())),
                    category.getItemText(category.getSelectedIndex())));
        }
        if (locality.getSelectedIndex() != 0) {
            data.setLocality(new LocalityDetail(
                    null,
                    category.getItemText(category.getSelectedIndex()),
                    locality.getValue(locality.getSelectedIndex())));
        }
        data.setAddition(false);
        return data;
    }

    // Handlers
    @UiHandler("content")
    void handleContentClick(ClickEvent event) {
        if (content.getText().equals(MSGS.searchContent())) {
            content.setText("");
        }
    }

    @UiHandler("content")
    void handleContentDrag(MouseOutEvent event) {
        if (content.getText().equals("")) {
            content.setText(MSGS.searchContent());
        }
    }
}