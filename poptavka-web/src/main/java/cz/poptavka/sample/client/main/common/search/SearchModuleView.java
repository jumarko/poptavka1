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
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter.SearchModulesViewInterface;
import java.util.HashMap;
import java.util.Map;

public class SearchModuleView extends Composite implements SearchModulePresenter.SearchModuleInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private Map<Long, String> categories = new HashMap<Long, String>();

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

    public SearchModuleView() {
        initWidget(uiBinder.createAndBindUi(this));
        popupPanel.setAutoHideEnabled(true);
        popupPanel.setAnimationEnabled(true);
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

//    @Override
//    public Button getSearchAdvBtn() {
//        return searchAdvBtn;
//    }
    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = ((SearchModulesViewInterface) popupPanel.getWidget()).getFilter();
        return data;
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
    public void setCategories(Map<Long, String> categories) {
        this.categories = categories;
    }

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
    @UiHandler("searchContent")
    void handleListBoxClick(ClickEvent event) {
    }
    private PopupPanel categoryTooltip = new PopupPanel();

    @UiHandler("searchCategory")
    void handlerSearchContentMouserOverEvent(MouseOverEvent event) {
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
    void handlerSearchContentMouserOutEvent(MouseOutEvent event) {
        categoryTooltip.hide();
    }

    @UiHandler("popupPanel")
    void handlerPopupPanelCloserEvent(CloseEvent<PopupPanel> event) {
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
}