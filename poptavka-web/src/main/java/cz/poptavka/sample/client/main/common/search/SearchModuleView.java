package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter.SearchModulesViewInterface;

public class SearchModuleView extends Composite implements SearchModulePresenter.SearchModuleInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

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
    @UiField(provided = true)
//    DecoratedPopupPanel popupPanel;
    PopupPanel popupPanel = new PopupPanel(true);
//    @UiField
//    ListBox listbox;

    public SearchModuleView() {
        initWidget(uiBinder.createAndBindUi(this));
//        listbox.setSize("200px", "200px");
//        listbox.setLayoutData(new HomeDemandViewView());
//        listbox.set
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
    public TextBox getSerachContent() {
        return searchContent;
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
}