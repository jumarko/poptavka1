package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.user.client.ui.ListBox;

public class SearchModuleView extends Composite implements SearchModulePresenter.SearchModuleInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, SearchModuleView> {
    }
    @UiField
    Button searchBtn, advSearchBtn;
    @UiField
    TextBox searchContent;
    @UiField
    ListBox searchWhat;
    @UiField
    PopupPanel popupPanel;
    //Pamataj, kym uzivatel neklikne AdvBtn az potom vytvor intanciu AdvanceSearchConventWidgetu
    //a nastav prislusny attributeSelector widget
    IsWidget attribureSelectorWidget = null;
    //Holds data
    private SearchModuleDataHolder filters = new SearchModuleDataHolder();

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        searchWhat.addItem(Storage.MSGS.demands());
        searchWhat.addItem(Storage.MSGS.suppliers());
        searchWhat.setSelectedIndex(0);

        popupPanel.setAutoHideEnabled(true);
        popupPanel.setAnimationEnabled(true);
        //Aby sa nam nezobrazoval taky ramcek (popup bez widgetu) pri starte modulu
        //Musi to byt takto? Neda sa to urobit krajsie? (len hide nefunguje)
        popupPanel.show();
        popupPanel.hide();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setAttributeSelectorWidget(IsWidget attributeSearchViewWidget) {
        attribureSelectorWidget = attributeSearchViewWidget;
    }

    @Override
    public IsWidget getAttributeSelectorWidget() {
        return attribureSelectorWidget;
    }

    // Buttons
    @Override
    public Button getSearchBtn() {
        return searchBtn;
    }

    @Override
    public Button getAdvSearchBtn() {
        return advSearchBtn;
    }

    @Override
    public TextBox getSearchContent() {
        return searchContent;
    }

    @Override
    public PopupPanel getPopupPanel() {
        return popupPanel;
    }

    /**
     * If full text filtering was chosen, stores given string to
     * SearchModuleDataHolder.
     */
    @Override
    public void setFilterSearchContent() {
        if (!searchContent.getText().isEmpty()
                && !searchContent.getText().equals(Storage.MSGS.searchContent())
                && filters.getAttributes().isEmpty()) {
            filters.setSearchText(searchContent.getText());
        }
    }

    /*
     * CLICK HANDLERS
     *
     * To define what action was made by user. Have to know because of acquiring
     * data from appropiate view loaded in popup window. See
     * handlerPopupPanelCloserEvent methods.
     */
    @UiHandler("searchContent")
    void handleSearchContentFocusClick(FocusEvent event) {
        if (searchContent.getText().equals(Storage.MSGS.searchContent())) {
            searchContent.setText("");
        }
    }

    @UiHandler("searchContent")
    void handleSearchContentBlurClick(BlurEvent event) {
        if (searchContent.getText().equals("")) {
            searchContent.setText(Storage.MSGS.searchContent());
        }
    }

    /*
     * POPUP CLOSE HANDLER
     */
    /**
     * When popup is closed. Appropiate filters are stored to
     * searchModuleDataHolder. Storing is according type of filtering performed
     * - categories, localities, attributes.
     */
    @UiHandler("popupPanel")
    void handlerPopupPanelCloserEvent(CloseEvent<PopupPanel> event) {
        //Change adv button icon to - selected
    }
}