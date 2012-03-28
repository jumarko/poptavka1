package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
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
import java.util.HashMap;
import java.util.Map;

public class SearchModuleView extends Composite implements SearchModulePresenter.SearchModuleInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private Map<Long, String> categories = new HashMap<Long, String>();
    private Map<Long, String> localities = new HashMap<Long, String>();

    @Override
    public PopupPanel getPopupPanel() {
        return popupPanel;
    }

    interface SearchModulViewUiBinder extends UiBinder<Widget, SearchModuleView> {
    }
    @UiField
    Button searchBtn, advSearchBtn;
    @UiField
    TextBox searchContent, searchCategory, searchLocality;
    @UiField
    PopupPanel popupPanel;
    private PopupPanel categoryTooltip = new PopupPanel();
    private PopupPanel localityTooltip = new PopupPanel();

//    public SearchModuleView() {
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
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = ((SearchModulesViewInterface) popupPanel.getWidget()).getFilter();
        return data;
    }

    @Override
    public Map<Long, String> getFilterCategories() {
        return categories;
    }

    @Override
    public Map<Long, String> getFilterLocalities() {
        return localities;
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
//    @Override
//    public void setCategories(Map<Long, String> categories) {
//        this.categories = categories;
//    }
//
//    @Override
//    public void setLocalities(Map<Long, String> localities) {
//        this.localities = localities;
//    }
    // Handlers
//    @UiHandler("advSearchBtn")
//    void handleSearchContentClick(ClickEvent event) {
//
//        if (Storage.getCurrentlyLoadedView().equals("homeDemands")) {
//            popupPanel.setWidget(new HomeDemandViewView());
//        } else if (Storage.getCurrentlyLoadedView().equals("homeSuppliers")) {
//            popupPanel.setWidget(new HomeSuppliersViewView());
//        }
//        Widget source = (Widget) event.getSource();
//        int left = source.getAbsoluteLeft();
//        int top = source.getAbsoluteTop()+15;
//        popupPanel.setPopupPosition(left, top);
//
//        // Show the popup
////        popupPanel.setHeight("150px");
////        popupPanel.setWidth("150px");
//        popupPanel.setAnimationEnabled(true);
//        popupPanel.show();
//    }
//    @UiHandler("popupPanel")
//    void handlePopupClickElse(BrowserEvent event) {
//        if (popupPanel.isShowing()) {
//            popupPanel.hide();
//        } else {
//            popupPanel.show();
//        }
//    }
    //0 - searchContect
    //1 - searchCategory
    //2 - searchLocality
    //3 - advSearchButton
    private int action = -1;

    /**
     * SEARCH CONTENT
     */
    @UiHandler("searchContent")
    void handleListBoxClick(ClickEvent event) {
        action = 0;
    }

    /**
     * SEARCH CATEGORY
     */
    @UiHandler("searchCategory")
    void handleSearchCategoryClick(ClickEvent event) {
        action = 1;
    }

    @UiHandler("searchCategory")
    void handlerSearchCategoryMouserOverEvent(MouseOverEvent event) {
        if (categories.isEmpty()) {
            return;
        }
        Widget source = (Widget) event.getSource();
        int left = source.getAbsoluteLeft();
        int top = source.getAbsoluteTop();
        categoryTooltip.setPopupPosition(left + 30, top + 30);

        VerticalPanel list = new VerticalPanel();
        list.add(new Label("Filter categories:"));
        for (String cat : categories.values()) {
            list.add(new Label(cat));
        }
        categoryTooltip.setWidget(list);
        categoryTooltip.show();
    }

    @UiHandler("searchCategory")
    void handlerSearchCategoryMouserOutEvent(MouseOutEvent event) {
        categoryTooltip.hide();
    }

    /**
     * SEARCH LOCALITY
     */
    @UiHandler("searchLocality")
    void handleSearchLocalityClick(ClickEvent event) {
        action = 2;
    }

    @UiHandler("searchLocality")
    void handlerSearchLocalityMouserOverEvent(MouseOverEvent event) {
        if (localities.isEmpty()) {
            return;
        }
        Widget source = (Widget) event.getSource();
        int left = source.getAbsoluteLeft();
        int top = source.getAbsoluteTop();
        localityTooltip.setPopupPosition(left + 30, top + 30);

        VerticalPanel list = new VerticalPanel();
        list.add(new Label("Filter localities:"));
        for (String cat : localities.values()) {
            list.add(new Label(cat));
        }
        localityTooltip.setWidget(list);
        localityTooltip.show();
    }

    @UiHandler("searchLocality")
    void handlerSearchLocalityMouserOutEvent(MouseOutEvent event) {
        localityTooltip.hide();
    }

    /**
     * BUTTONS
     */
    @UiHandler("advSearchBtn")
    void handleAdvSearchBtnClick(ClickEvent event) {
        action = 3;
    }

    /**
     * POPUP
     */
    @UiHandler("popupPanel")
    void handlerPopupPanelCloserEvent(CloseEvent<PopupPanel> event) {
        switch (action) {
            case 0://searchContext
                break;
            case 1://searchCategory
                searchCategoriesAction();
                break;
            case 2://searchLocality
                searchLocalitiesAction();
                break;
            case 3://advSearch
                break;
            default:
                break;
        }

    }

    private void searchCategoriesAction() {
        categories.clear();
        CategorySelectorInterface categoryValues =
                (CategorySelectorInterface) popupPanel.getWidget();

        //categories
        for (int i = 0; i < categoryValues.getSelectedList().getItemCount(); i++) {
            categories.put(Long.valueOf(
                    categoryValues.getSelectedList().getValue(i)),
                    //                    categorySelector.getSelectedCategoryCodes().get(i)),
                    categoryValues.getSelectedList().getItemText(i));

        }
        if (!categories.isEmpty()) {
            searchCategory.setText("filter:" + categories.values().toString());
        }
    }

    private void searchLocalitiesAction() {
        localities.clear();
        LocalitySelectorInterface localityValues =
                (LocalitySelectorInterface) popupPanel.getWidget();

        //localities
        for (int i = 0; i < localityValues.getSelectedList().getItemCount(); i++) {
            localities.put(Long.valueOf(
                    localityValues.getSelectedList().getValue(i)),
                    //                    locality.getSelectedLocalityCodes().get(i)),
                    localityValues.getSelectedList().getItemText(i));

        }
        if (!localities.isEmpty()) {
            searchLocality.setText("filter:" + localities.values().toString());
        }
    }
}