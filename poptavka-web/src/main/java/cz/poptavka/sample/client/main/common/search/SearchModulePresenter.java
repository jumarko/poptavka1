package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;


import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.views.AdminClientsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminDemandsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminEmailActivationViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminInvoicesViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminMessagesViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminOffersViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminPaymentMethodsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminPermissionsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminPreferencesViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminProblemsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminSuppliersViewView;
import cz.poptavka.sample.client.main.common.search.views.HomeDemandViewView;
import cz.poptavka.sample.client.main.common.search.views.HomeSuppliersViewView;
import cz.poptavka.sample.client.main.common.search.views.MessagesTabViewView;
import cz.poptavka.sample.client.user.admin.tab.AdminAccessRolesView;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.adminModule.PaymentMethodDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Presenter(view = SearchModuleView.class, multiple = true)
public class SearchModulePresenter
        extends LazyPresenter<SearchModulePresenter.SearchModuleInterface, SearchModuleEventBus> {

    public interface SearchModuleInterface extends LazyView {

        Widget getWidgetView();

        Button getSearchBtn();

        Button getAdvSearchBtn();

        PopupPanel getPopupPanel();

        TextBox getSearchContent();

        TextBox getSearchCategory();

        TextBox getSearchLocality();

        SearchModuleDataHolder getFilter();

        Map<Long, String> getFilterCategories();

        Map<Long, String> getFilterLocalities();
    }

    public interface SearchModulesViewInterface extends LazyView {

        SearchModuleDataHolder getFilter();

//        Widget getWidgetView();

        ListBox getCategoryList();

        ListBox getLocalityList();

        void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder);
    }

    @Override
    public void bindView() {
        this.addSearchBtnClickHandler();
        this.addAdvSearchBtnClickHandler();
        view.getSearchCategory().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCategoryWidget(view.getPopupPanel());
                view.getSearchCategory().setText("category");
                showPopupPanel();
            }
        });
        view.getSearchLocality().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initLocalityWidget(view.getPopupPanel());
                view.getSearchLocality().setText("locality");
                showPopupPanel();
            }
        });
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToSearchModule() {
        GWT.log("SearchModule loaded");
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onResponseLocalities(final ArrayList<LocalityDetail> list) {
        final ListBox box = ((SearchModulesViewInterface) view.getPopupPanel().getWidget()).getLocalityList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem("All localities...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getCode());
                }
                box.setSelectedIndex(0);
                GWT.log("Locality List filled");
            }
        });
    }

    public void onClearSearchContent() {
        view.getSearchContent().setText(Storage.MSGS.searchContent());
    }

    public void onResponseCategories(final ArrayList<CategoryDetail> list) {
        final ListBox box = ((SearchModulesViewInterface) view.getPopupPanel().getWidget()).getCategoryList();
        box.clear();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem(Storage.MSGS.allCategories());
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), String.valueOf(list.get(i).getId()));
                }
                box.setSelectedIndex(0);
                GWT.log("Category Lists filled");
            }
        });
    }

    public void onResponsePaymentMethods(final List<PaymentMethodDetail> list) {
        final ListBox box = ((AdminInvoicesViewView) view.getPopupPanel().getWidget()).getPaymentMethodList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem("select method...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getName());
                }
                box.setSelectedIndex(0);
                GWT.log("PaymentMethodList filled");
            }
        });
    }

    private void showPopupPanel() {
        int left = view.getSearchContent().getElement().getAbsoluteLeft();
        int top = view.getSearchContent().getElement().getAbsoluteTop() + 30;
        view.getPopupPanel().setPopupPosition(left, top);
        view.getPopupPanel().show();
    }

    /**************************************************************************/
    /* Additional events used in bind method                                  */
    /**************************************************************************/
    private void addSearchBtnClickHandler() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                switch (Storage.getCurrentlyLoadedView()) {
                    case Constants.HOME_DEMANDS:
                        eventBus.goToHomeDemandsModule(view.getFilter());
                        break;
                    case Constants.HOME_SUPPLIERS:
                        eventBus.goToHomeSuppliersModule(view.getFilter());
                        break;
                    default:
                        if (Constants.getDemandsConstants().contains(Storage.getCurrentlyLoadedView())) {
                            eventBus.goToDemandModule(view.getFilter(), Storage.getCurrentlyLoadedView());
                        }
                        if (Constants.getAdminConstants().contains(Storage.getCurrentlyLoadedView())) {
                            eventBus.goToAdminModule(view.getFilter(), Storage.getCurrentlyLoadedView());
                        }
                        if (Constants.getMessagesConstants().contains(Storage.getCurrentlyLoadedView())) {
                            eventBus.goToMessagesModule(view.getFilter(), Storage.getCurrentlyLoadedView());
                        }
                }
            }
        });
    }

    private void addAdvSearchBtnClickHandler() {
        view.getAdvSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                initAppropiateViews();
                showPopupPanel();
            }
        });
    }

    private void initAppropiateViews() {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.HOME_DEMANDS:
                eventBus.requestCategories();
                eventBus.requestLocalities();
                view.getPopupPanel().setWidget(new HomeDemandViewView());
                break;
            case Constants.HOME_SUPPLIERS:
                eventBus.requestCategories();
                eventBus.requestLocalities();
                view.getPopupPanel().setWidget(new HomeSuppliersViewView());
                break;
            case Constants.DEMANDS_CLIENT_MY_DEMANDS:
                break;
            case Constants.DEMANDS_CLIENT_OFFERS:
                break;
            case Constants.DEMANDS_CLIENT_ASSIGNED_DEMANDS:
                break;
            case Constants.DEMANDS_SUPPLIER_MY_DEMANDS:
                break;
            case Constants.DEMANDS_SUPPLIER_OFFERS:
                break;
            case Constants.DEMANDS_SUPPLIER_ASSIGNED_DEMANDS:
                break;
            case Constants.MESSAGES_INBOX:
                view.getPopupPanel().setWidget(new MessagesTabViewView());
                break;
            case Constants.MESSAGES_SENT:
                view.getPopupPanel().setWidget(new MessagesTabViewView());
                break;
            case Constants.MESSAGES_TRASH:
                view.getPopupPanel().setWidget(new MessagesTabViewView());
                break;
            case Constants.ADMIN_ACCESS_ROLE:
                view.getPopupPanel().setWidget(new AdminAccessRolesView());
                break;
            case Constants.ADMIN_CLIENTS:
                view.getPopupPanel().setWidget(new AdminClientsViewView());
                break;
            case Constants.ADMIN_DEMANDS:
                view.getPopupPanel().setWidget(new AdminDemandsViewView());
                break;
            case Constants.ADMIN_EMAILS_ACTIVATION:
                view.getPopupPanel().setWidget(new AdminEmailActivationViewView());
                break;
            case Constants.ADMIN_INVOICES:
                view.getPopupPanel().setWidget(new AdminInvoicesViewView());
                break;
            case Constants.ADMIN_MESSAGES:
                view.getPopupPanel().setWidget(new AdminMessagesViewView());
                break;
            case Constants.ADMIN_OFFERS:
                view.getPopupPanel().setWidget(new AdminOffersViewView());
                break;
            case Constants.ADMIN_PAYMENT_METHODS:
                view.getPopupPanel().setWidget(new AdminPaymentMethodsViewView());
                break;
            case Constants.ADMIN_PERMISSIONS:
                view.getPopupPanel().setWidget(new AdminPermissionsViewView());
                break;
            case Constants.ADMIN_PREFERENCES:
                view.getPopupPanel().setWidget(new AdminPreferencesViewView());
                break;
            case Constants.ADMIN_PROBLEMS:
                view.getPopupPanel().setWidget(new AdminProblemsViewView());
                break;
            case Constants.ADMIN_SUPPLIERS:
                view.getPopupPanel().setWidget(new AdminSuppliersViewView());
                break;
            default:
                view.getPopupPanel().clear();
                break;
        }
    }
}