package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.user.widget.LoadingDiv;

@Presenter(view = SearchModuleView.class, multiple = true)
public class SearchModulePresenter extends
        BasePresenter<SearchModulePresenter.SearchModuleInterface, SearchModuleEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface SearchModuleInterface {

        Widget getWidgetView();

        Button getSearchBtn();

        Button getAdvSearchBtn();

        PopupPanel getPopupPanel();

        TextBox getSerachContent();

        SearchModuleDataHolder getFilter();
    }

    public interface SearchModulesViewInterface { //extends LazyView {

        SearchModuleDataHolder getFilter();

        Widget getWidgetView();

        ListBox getCategoryList();

        ListBox getLocalityList();

        void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder);
    }
    private LoadingDiv loading = null;

    public void bind() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Storage.getCurrentlyLoadedView().equals("homeDemands")) {
                    eventBus.initHomeDemandsModule(view.getFilter());
                } else if (Storage.getCurrentlyLoadedView().equals("homeSuppliers")) {
                    eventBus.initHomeSupplierModule(view.getFilter());
                }
                ((SearchModulesViewInterface) view.getPopupPanel().getWidget())
                        .displayAdvSearchDataInfo(view.getFilter(), view.getSerachContent());
            }
        });
        view.getAdvSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                if (Storage.getCurrentlyLoadedView().equals("homeDemands")) {
                    eventBus.initHomeDemandView(view.getPopupPanel());
                } else if (Storage.getCurrentlyLoadedView().equals("homeSuppliers")) {
                    eventBus.initHomeSuppliersView(view.getPopupPanel());
                }
                Widget source = (Widget) event.getSource();
                int left = source.getAbsoluteLeft() - 250;
                int top = source.getAbsoluteTop() + 20;
                view.getPopupPanel().setPopupPosition(left, top);

                // Show the popup
//        popupPanel.setHeight("150px");
//        popupPanel.setWidth("150px");
                view.getPopupPanel().setAnimationEnabled(true);
                view.getPopupPanel().show();
            }
        });
    }

    public void onInitSearchModule(SimplePanel panel) {
        GWT.log("onInitSearchModule - som tu");
        //eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
//        eventBus.setTabAdminWidget(view.getWidgetView());
//        eventBus.fireMarkedEvent();
//        eventBus.setUserInteface((StyleInterface) view.getWidgetView());

        Storage.showLoading(Storage.MSGS.progressAdminLayoutInit());
        panel.setWidget(view.getWidgetView());
        Storage.hideLoading();
    }
}