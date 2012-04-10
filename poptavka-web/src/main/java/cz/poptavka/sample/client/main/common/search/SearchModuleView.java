package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter.SearchModulesViewInterface;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

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
    //Holds data
    private SearchModuleDataHolder filters = new SearchModuleDataHolder();
    //0 - searchContect
    //1 - searchCategory
    //2 - searchLocality
    //3 - advSearchButton
    private int action = -1;

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
     * SEARCH CONTENT
     */
    @UiHandler("searchContent")
    void handleListBoxClick(ClickEvent event) {
        action = 0;
    }

    /*
     * CLICK HANDLERS
     *
     * To define what action was made by user. Have to know because of acquiring
     * data from appropiate view loaded in popup window. See handlerPopupPanelCloserEvent methods.
     */
    @UiHandler("searchContent")
    void handleSearchContentClick(ClickEvent event) {
        action = 1;
    }

    @UiHandler("searchCategory")
    void handleSearchCategoryClick(ClickEvent event) {
        action = 1;
    }

    @UiHandler("searchLocality")
    void handleSearchLocalityClick(ClickEvent event) {
        action = 2;
    }

    @UiHandler("advSearchBtn")
    void handleAdvSearchBtnClick(ClickEvent event) {
        action = 3;
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
    @UiHandler("searchContent")
    void handlerSearchContentMouserOutEvent(MouseOutEvent event) {
        toolTip.hide();
    }

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
     * When popup is closed. Appropiate filters are stored to searchModuleDataHolder.
     * Storing is according type of filtering performed - string, categories, localities, attributes.
     */
    @UiHandler("popupPanel")
    void handlerPopupPanelCloserEvent(CloseEvent<PopupPanel> event) {
        switch (action) {
            case 0://searchContext
                searchFullText();
                break;
            case 1://searchCategory
                searchCategoriesAction();
                break;
            case 2://searchLocality
                searchLocalitiesAction();
                break;
            case 3://advSearch
                searchAdvanced();
                break;
            default:
                break;
        }
    }

    /*
     * **********************  HELPER METHODS ****************************
     */
    /**
     * Creates popup window used as tooltip if user has chosen some filters.
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
     * If full text filtering was chosen, stores given string to SearchModuleDataHolder.
     */
    private void searchFullText() {
        filters.setSearchText(searchContent.getText());
    }

    /**
     * If categories filtering was chosen, CategorySelector widget is loaded in popup window.
     * This methods acquires chosen categories and store them in SearchModuleDataHolder.
     */
    private void searchCategoriesAction() {
        filters.getCategories().clear();
        CategorySelectorInterface categoryValues =
                (CategorySelectorInterface) popupPanel.getWidget();

        //categories
        for (int i = 0; i < categoryValues.getSelectedList().getItemCount(); i++) {
            filters.getCategories().add(new CategoryDetail(Long.valueOf(
                    categoryValues.getSelectedList().getValue(i)),
                    categoryValues.getSelectedList().getItemText(i)));
        }
        //Display short info
        if (!filters.getCategories().isEmpty()) {
            searchCategory.setText("filter:" + filters.getCategories().toString());
        }
    }

    /**
     * If localities filtering was chosen, CategorySelector widget is loaded in popup window.
     * This methods acquires chosen categories and store them in SearchModuleDataHolder.
     */
    private void searchLocalitiesAction() {
        filters.getLocalities().clear();
        LocalitySelectorInterface localityValues =
                (LocalitySelectorInterface) popupPanel.getWidget();

        //localities
        for (int i = 0; i < localityValues.getSelectedList().getItemCount(); i++) {
            filters.getLocalities().add(new LocalityDetail(
                    localityValues.getSelectedList().getItemText(i),
                    localityValues.getSelectedList().getValue(i)));
        }
        //Display short info
        if (!filters.getLocalities().isEmpty()) {
            searchLocality.setText("filter:" + filters.getLocalities().toString());
        }
    }

    /**
     * If attributes filtering was chosen, appropiate widget of currently loaded view is loaded in popup window.
     * This methods acquires attributes and theirs values that are chosen to be filtered and store them
     * in SearchModuleDataHolder.
     */
    private void searchAdvanced() {
        filters.getAttibutes().clear();
        SearchModulesViewInterface filtersValues =
                (SearchModulesViewInterface) popupPanel.getWidget();
        filters.getAttibutes().addAll(filtersValues.getFilter());
        //Display short info
        if (!filters.getAttibutes().isEmpty()) {
            searchContent.setText("filter:" + filters.getAttibutes().toString());
        }
    }
}