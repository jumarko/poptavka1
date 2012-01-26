package cz.poptavka.sample.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.common.LoadingPopup;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.root.interfaces.IRootView;
import cz.poptavka.sample.client.root.interfaces.IRootView.IRootPresenter;

@Presenter(view = RootView.class)
public class RootPresenter extends BasePresenter<IRootView, RootEventBus>
        implements IRootPresenter {

    private PopupPanel popup = null;
    private CategorySelectorPresenter categorySelector = null;
    private LocalitySelectorPresenter localitySelector = null;

    public void onSetMenu(IsWidget menu) {
        GWT.log("Menu widget set");
        view.setMenu(menu);
    }

    public void onSetSearchBar(IsWidget searchBar) {
        GWT.log("Search bar widget set");
        view.setSearchBar(searchBar);
    }

    public void onSetBody(IsWidget body) {
        GWT.log("Body widget set");
        view.setBody(body);
    }

    public void onSetFooter(IsWidget footer) {
        GWT.log("Footer widget set");
        view.setFooter(footer);
    }

    public void onSetHeader(IsWidget header) {
        GWT.log("Header widget set");
        view.setHeader(header);
    }

    public void onStart() {
        GWT.log("Root module loaded");
        eventBus.initHomeWelcomeModule(null);
        eventBus.initSearchModule(view.getSearchBar());
    }

    public void onSetBodyHolderWidget(IsWidget content) {
        view.setBody(content);
    }

    public void onLoadingShow(String loadingMessage) {
        if (!(popup == null)) {
            LoadingPopup popupContent = (LoadingPopup) popup.getWidget();
            popupContent.setMessage(loadingMessage);
        } else {
            createLoadingPopup(loadingMessage);
        }
    }

    public void onLoadingHide() {
        if (popup != null) {
            popup.hide();
            popup = null;
        }
    }

    public void onInitDemandAdvForm(SimplePanel holderWidget) {
    }

    public void onLoadingShowWithAnchor(String loadingMessage, Widget anchor) {
        if (popup != null) {
            LoadingPopup popupContent = (LoadingPopup) popup.getWidget();
            popupContent.setMessage(loadingMessage);
        } else {
            createLoadingPopup(loadingMessage, anchor);
        }
    }

    /** multiple presenters handling methods **/
    public void onInitCategoryWidget(SimplePanel holderPanel) {
        if (categorySelector != null) {
            eventBus.removeHandler(categorySelector);
        }
        categorySelector = eventBus.addHandler(CategorySelectorPresenter.class);
        categorySelector.initCategoryWidget(holderPanel);
    }

    public void onInitLocalityWidget(SimplePanel holderPanel) {
        if (localitySelector != null) {
            eventBus.removeHandler(localitySelector);
        }
        localitySelector = eventBus.addHandler(LocalitySelectorPresenter.class);
        localitySelector.initLocalityWidget(holderPanel);
    }

    public void onInitDemandBasicForm(SimplePanel holderWidget) {
    }
    private static final int OFFSET_X = 60;
    private static final int OFFSET_Y = 35;

    private void createLoadingPopup(String loadingMessage, Widget anchor) {
        popup = new PopupPanel(false, false);
        popup.setStylePrimaryName(StyleResource.INSTANCE.common().loadingPopup());
        popup.setWidget(new LoadingPopup(loadingMessage));
        int top = anchor.getAbsoluteTop() + (anchor.getOffsetHeight() / 2);
        int left = anchor.getAbsoluteLeft() + (anchor.getOffsetWidth() / 2)
                - OFFSET_X;
        popup.showRelativeTo(anchor);
        GWT.log("AbsoluteLeft: " + anchor.getAbsoluteLeft() + " OffsetWidth: "
                + (anchor.getOffsetWidth()));
        GWT.log("AbsoluteTop: " + anchor.getAbsoluteTop() + " Offsetheight: "
                + (anchor.getOffsetHeight()));

        GWT.log("L: " + left + " T: " + top);

        popup.show();
    }

    private void createLoadingPopup(String loadingMessage) {
        popup = new PopupPanel(false, false);
        popup.setStylePrimaryName(StyleResource.INSTANCE.common().loadingPopup());
        popup.setWidget(new LoadingPopup(loadingMessage));
        popup.setPopupPosition((Window.getClientWidth() / 2) - OFFSET_X,
                (Window.getClientHeight() / 2) - OFFSET_Y);
        popup.show();
    }

    public void onDisplayMenu() {
        eventBus.setPublicLayout();
        view.getMenu().getElement().getStyle().setDisplay(Display.BLOCK);
        view.getSearchBar().getElement().getStyle().setDisplay(Display.BLOCK);
    }

    public void onAtAccount() {
        eventBus.initDemandModule();
    }
}
