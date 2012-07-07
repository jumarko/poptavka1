package com.eprovement.poptavka.client.main.common.search;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.main.common.category.CategorySelectorView;
import com.eprovement.poptavka.client.main.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.main.common.locality.LocalitySelectorView;
import com.eprovement.poptavka.client.main.common.search.SearchModulePresenter.SearchModulesViewInterface;
import com.eprovement.poptavka.shared.domain.LocalityDetail;

public class SearchModuleView extends Composite implements SearchModulePresenter.SearchModuleInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, SearchModuleView> {
    }
    @UiField
    Button searchBtn, advSearchBtn;
    @UiField
    TextBox searchContent, searchCategory, searchLocality;
    @UiField
    PopupPanel popupPanel;
    private PopupPanel toolTip = new PopupPanel();
    private CategorySelectorView categorySelector = null;
    private LocalitySelectorView localitySelector = null;
    private IsWidget attributeSelector = null;
    //Holds data
    private SearchModuleDataHolder filters = new SearchModuleDataHolder();
    private int action = -1;
    //Constants
    public static final int CATEGORIES = 1;
    public static final int LOCALITIES = 2;
    public static final int ATTRIBUTES = 3;



    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
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
    public IsWidget getAttributeSelector() {
        return attributeSelector;
    }

    public void setAttributeSelector(IsWidget attributeSearchViewWidget) {
        this.attributeSelector = attributeSearchViewWidget;
    }

    @Override
    public CategorySelectorView getCategorySelector() {
        return categorySelector;
    }

    @Override
    public LocalitySelectorView getLocalitySelector() {
        return localitySelector;
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
    public SearchModuleDataHolder getFilters() {
        return filters;
    }

    @Override
    public TextBox getSearchContent() {
        return searchContent;
    }

    @Override
    public TextBox getSearchCategory() {
        return searchCategory;
    }

    @Override
    public TextBox getSearchLocality() {
        return searchLocality;
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
                && getFilters().getAttibutes().isEmpty()) {
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

    @UiHandler("searchCategory")
    void handleSearchCategoryClick(ClickEvent event) {
        //action for this click = loading CategorySelectorWidget to popup window is made in presenter.
        action = CATEGORIES;
        //If not null => it was already initialized => just load it
        //If null => let presenter initialize it
        if (categorySelector != null) {
            popupPanel.setWidget(categorySelector);
        }
    }

    @UiHandler("searchLocality")
    void handleSearchLocalityClick(ClickEvent event) {
        //action for this click = loading LocalitySelectorWidget to popup window is made in presenter.
        action = LOCALITIES;
        //If not null => it was already initialized => just load it
        //If null => let presenter initialize it
        if (localitySelector != null) {
            popupPanel.setWidget(localitySelector);
        }
    }

    @UiHandler("advSearchBtn")
    void handleAdvSearchBtnClick(ClickEvent event) {
        //action for this click = loading appropiate advance search view to popup window is made in presenter.
        action = ATTRIBUTES;
        //If not null => it was already initialized => just load it
        if (attributeSelector != null) {
            popupPanel.setWidget(attributeSelector);
        }
    }

    /*
     * MOUSE OVER HANDLERS
     */
    @UiHandler("searchCategory")
    void handlerSearchCategoryMouserOverEvent(MouseOverEvent event) {
        if (filters.getCategories().isEmpty()) {
            return;
        }
        VerticalPanel list = new VerticalPanel();
        list.add(new Label("Filter categories:"));
        list.add(new Label(filters.getCategories().toString()));
        this.createToolTipPopupWindow(event, list);
    }

    @UiHandler("searchContent")
    void handlerSearchContentMouserOverEvent(MouseOverEvent event) {
        if (filters.getAttibutes().isEmpty()) {
            return;
        }
        VerticalPanel list = new VerticalPanel();
        list.add(new Label("Filter attributes:"));
        for (String attr : filters.getAttibutes().toString().split(", ")) {
            list.add(new Label(attr));
        }
        this.createToolTipPopupWindow(event, list);
    }

    @UiHandler("searchLocality")
    void handlerSearchLocalityMouserOverEvent(MouseOverEvent event) {
        if (filters.getLocalities().isEmpty()) {
            return;
        }
        VerticalPanel list = new VerticalPanel();
        list.add(new Label("Filter localities:"));
        list.add(new Label(filters.getLocalities().toString()));
        this.createToolTipPopupWindow(event, list);
    }

    /*
     * MOUNSE OUT HANDLERS
     */
    @UiHandler("searchCategory")
    void handlerSearchCategoryMouserOutEvent(MouseOutEvent event) {
        toolTip.hide();
    }

    @UiHandler("searchLocality")
    void handlerSearchLocalityMouserOutEvent(MouseOutEvent event) {
        toolTip.hide();
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
        switch (action) {
            case CATEGORIES://searchCategory
                categorySelector = (CategorySelectorView) popupPanel.getWidget();
                searchCategoriesAction();
                break;
            case LOCALITIES://searchLocality
                localitySelector = (LocalitySelectorView) popupPanel.getWidget();
                searchLocalitiesAction();
                break;
            case ATTRIBUTES://advSearch
                attributeSelector = popupPanel.getWidget();
                searchAdvanced();
                break;
            default:
                break;
        }
        displayShortInfo();
    }

    public void setSearchBarEnables(boolean category, boolean locality, boolean advanceBtn) {
        searchCategory.setEnabled(category);
        searchLocality.setEnabled(locality);
        advSearchBtn.setEnabled(advanceBtn);
    }

    /*
     * ********************** HELPER METHODS ****************************
     */
    /**
     * Creates popup window used as tooltip if user has chosen some filters.
     *
     * @param event - action - mouse over TextBox
     * @param content - widget loads to represent popup content
     */
    private void createToolTipPopupWindow(MouseOverEvent event, Widget content) {
        Widget source = (Widget) event.getSource();
        int left = source.getAbsoluteLeft();
        int top = source.getAbsoluteTop();
        toolTip.setPopupPosition(left + 30, top + 30);
        toolTip.setWidget(content);
        toolTip.show();
    }

    /**
     * If categories filtering was chosen, CategorySelector widget is loaded in
     * popup window. This methods acquires chosen categories and store them in
     * SearchModuleDataHolder.
     */
    private void searchCategoriesAction() {
        filters.getCategories().clear();
        CategorySelectorInterface categoryValues = (CategorySelectorInterface) popupPanel.getWidget();

        filters.getCategories().addAll(categoryValues.getDataProvider().getList());
    }

    /**
     * If localities filtering was chosen, CategorySelector widget is loaded in
     * popup window. This methods acquires chosen categories and store them in
     * SearchModuleDataHolder.
     */
    private void searchLocalitiesAction() {
        filters.getLocalities().clear();
        LocalitySelectorInterface localityValues =
                (LocalitySelectorInterface) popupPanel.getWidget();

        for (int i = 0; i < localityValues.getSelectedList().getItemCount(); i++) {
            filters.getLocalities().add(new LocalityDetail(
                    localityValues.getSelectedList().getItemText(i),
                    localityValues.getSelectedList().getValue(i)));
        }
    }

    /**
     * If attributes filtering was chosen, appropiate widget of currently loaded
     * view is loaded in popup window. This methods acquires attributes and
     * theirs values that are chosen to be filtered and store them in
     * SearchModuleDataHolder.
     */
    private void searchAdvanced() {
        filters.getAttibutes().clear();
        SearchModulesViewInterface filtersValues =
                (SearchModulesViewInterface) popupPanel.getWidget();
        filters.getAttibutes().addAll(filtersValues.getFilter());
    }

    /**
     * Constucts info of given filters , that will be applied and place them
     * into given textBox. There are 3 options according to <b>action</b>
     * attribute, which holds used choice:
     *
     * Action values: 1...constucts info for <b>categories</b>, 2...constucts
     * info for <b>localities</b>. 3...constucts info for advance search view
     * <b>attributes</b>,
     *
     */
    private void displayShortInfo() {
        switch (action) {
            case CATEGORIES:
                if (filters.getCategories().isEmpty()) {
                    searchCategory.setText(Storage.MSGS.category());
                } else {
                    searchCategory.setText("filter:" + filters.getCategories().toString());
                }
                break;
            case LOCALITIES:
                if (filters.getLocalities().isEmpty()) {
                    searchLocality.setText(Storage.MSGS.locality());
                } else {
                    searchLocality.setText("filter:" + filters.getLocalities().toString());
                }
                break;
            case ATTRIBUTES:
                if (filters.getAttibutes().isEmpty()) {
                    searchContent.setText(Storage.MSGS.searchContent());
                } else {
                    searchContent.setText("filter:" + filters.getAttibutes().toString());
                }
                break;
            default:
                break;
        }
    }
}