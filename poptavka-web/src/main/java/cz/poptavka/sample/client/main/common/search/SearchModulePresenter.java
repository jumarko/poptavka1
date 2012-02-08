package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import cz.poptavka.sample.client.main.common.search.views.AdminAccessRolesViewView;
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
import cz.poptavka.sample.client.main.common.search.views.PotentialDemandMessagesViewView;
import cz.poptavka.sample.client.user.widget.LoadingDiv;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.PaymentMethodDetail;
import java.util.ArrayList;
import java.util.List;

@Presenter(view = SearchModuleView.class, multiple = true)
public class SearchModulePresenter
        extends BasePresenter<SearchModulePresenter.SearchModuleInterface, SearchModuleEventBus> {

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

    @Override
    public void bind() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Storage.getCurrentlyLoadedView().equals("homeDemands")) {
                    eventBus.initHomeDemandsModule(view.getFilter(), "home");
                } else if (Storage.getCurrentlyLoadedView().equals("userDemands")) {
                    eventBus.initHomeDemandsModule(view.getFilter(), "user");
                } else if (Storage.getCurrentlyLoadedView().equals("homeSuppliers")) {
                    eventBus.initHomeSuppliersModule(view.getFilter(), "home");
                } else if (Storage.getCurrentlyLoadedView().equals("userSuppliers")) {
                    eventBus.initHomeSuppliersModule(view.getFilter(), "user");
//                } else if (Storage.getCurrentlyLoadedView().equals("adminAccessRoles")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminClients")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminDemands")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminEmailsActivation")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminInvoices")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminMessages")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminOffers")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminPaymentMethods")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminPermissions")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminPreferences")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminProblems")) {
//                } else if (Storage.getCurrentlyLoadedView().equals("adminSuppliers")) {
                } else if (Storage.getCurrentlyLoadedView().equals("potentialDemandMessages")) {
//                    eventBus.initDemandsTabModule(view.getFilter());
                } else if (Storage.getCurrentlyLoadedView().equals("messagesTabInbox")) {
                    eventBus.initMessagesTabModuleInbox(view.getFilter());
                } else if (Storage.getCurrentlyLoadedView().equals("messagesTabSent")) {
                    eventBus.initMessagesTabModuleSent(view.getFilter());
                } else if (Storage.getCurrentlyLoadedView().equals("messagesTabTrash")) {
                    eventBus.initMessagesTabModuleTrash(view.getFilter());
                } else { //Admin...whatever
                    eventBus.initAdminModule(view.getFilter());
                }

                ((SearchModulesViewInterface) view.getPopupPanel().getWidget()).displayAdvSearchDataInfo(
                        view.getFilter(), view.getSerachContent());
            }
        });
        view.getAdvSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Storage.getCurrentlyLoadedView().equals("homeDemands")) {
                    initHomeDemandView();
                } else if (Storage.getCurrentlyLoadedView().equals("homeSuppliers")) {
                    initHomeSuppliersView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminAccessRoles")) {
                    initAdminAccessRolesView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminClients")) {
                    initAdminClientsRolesView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminDemands")) {
                    initAdminDemandsView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminEmailsActivation")) {
                    initAdminEmailActivationView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminInvoices")) {
                    initAdminInvoicesView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminMessages")) {
                    initAdminMessagesView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminOffers")) {
                    initAdminOffersView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminPaymentMethods")) {
                    initAdminPaymentMethodsView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminPermissions")) {
                    initAdminPermissionsView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminPreferences")) {
                    initAdminPreferencesView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminProblems")) {
                    initAdminProblemsView();
                } else if (Storage.getCurrentlyLoadedView().equals("adminSuppliers")) {
                    initAdminSuppliersView();
                } else if (Storage.getCurrentlyLoadedView().equals("potentialDemandMessages")) {
                    initPotentialDemandMessagesView();
                } else if (Storage.getCurrentlyLoadedView().equals("messagesTabInbox")
                        || Storage.getCurrentlyLoadedView().equals("messagesTabSent")
                        || Storage.getCurrentlyLoadedView().equals("messagesTabTrash")) {
                    initMessagesTabView();
                } else {
                    return;
                }
                Widget source = (Widget) event.getSource();
                int left = source.getAbsoluteLeft() - 250;
                int top = source.getAbsoluteTop() + 20;
                view.getPopupPanel().setPopupPosition(left, top);

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

    public void onClearSearchContent() {
        view.getSerachContent().setText(Storage.MSGS.searchContent());
    }

    /**
     * HOME DEMAND VIEW
     */
    public void initHomeDemandView() {
        eventBus.requestCategories();
        eventBus.requestLocalities();
        view.getPopupPanel().setWidget(new HomeDemandViewView());
    }

    /**
     * HOME SUPPLIERS VIEW
     */
    public void initHomeSuppliersView() {
        eventBus.requestCategories();
        eventBus.requestLocalities();
        view.getPopupPanel().setWidget(new HomeSuppliersViewView());
    }

    /**
     * ADMIN ACCESS ROLES VIEW
     */
    public void initAdminAccessRolesView() {
        view.getPopupPanel().setWidget(new AdminAccessRolesViewView());
    }

    /**
     * ADMIN CLIENTS ROLES VIEW
     */
    public void initAdminClientsRolesView() {
        view.getPopupPanel().setWidget(new AdminClientsViewView());
    }

    /**
     * ADMIN DEMANDS VIEW
     */
    public void initAdminDemandsView() {
        view.getPopupPanel().setWidget(new AdminDemandsViewView());
    }

    /**
     * ADMIN EMAIL ACTIVATION VIEW
     */
    public void initAdminEmailActivationView() {
        view.getPopupPanel().setWidget(new AdminEmailActivationViewView());
    }

    /**
     * ADMIN INVOICES VIEW
     */
    public void initAdminInvoicesView() {
        view.getPopupPanel().setWidget(new AdminInvoicesViewView());
    }

    /**
     * ADMIN MESSAGES VIEW
     */
    public void initAdminMessagesView() {
        view.getPopupPanel().setWidget(new AdminMessagesViewView());
    }

    /**
     * ADMIN OFFERS VIEW
     */
    public void initAdminOffersView() {
        view.getPopupPanel().setWidget(new AdminOffersViewView());
    }

    /**
     * ADMIN PAYMENT METHODS VIEW
     */
    public void initAdminPaymentMethodsView() {
        view.getPopupPanel().setWidget(new AdminPaymentMethodsViewView());
    }

    /**
     * ADMIN PERMISSIONS VIEW
     */
    public void initAdminPermissionsView() {
        view.getPopupPanel().setWidget(new AdminPermissionsViewView());
    }

    /**
     * ADMIN PREFERENCES VIEW
     */
    public void initAdminPreferencesView() {
        view.getPopupPanel().setWidget(new AdminPreferencesViewView());
    }

    /**
     * ADMIN PROBLEMS VIEW
     */
    public void initAdminProblemsView() {
        view.getPopupPanel().setWidget(new AdminProblemsViewView());
    }

    /**
     * ADMIN SUPPLIERS VIEW
     */
    public void initAdminSuppliersView() {
        view.getPopupPanel().setWidget(new AdminSuppliersViewView());
    }

    /**
     * POTEBTIAL DEMAND MESSAGES VIEW
     */
    public void initPotentialDemandMessagesView() {
        view.getPopupPanel().setWidget(new PotentialDemandMessagesViewView());
    }

    /**
     * MESSAGES TAB VIEW
     */
    public void initMessagesTabView() {
        view.getPopupPanel().setWidget(new MessagesTabViewView());
    }

    /**
     * COMMON METHODS
     */
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

    public void onResponseCategories(final ArrayList<CategoryDetail> list) {
        final ListBox box = ((SearchModulesViewInterface) view.getPopupPanel().getWidget()).getCategoryList();
        box.clear();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem(MSGS.allCategories());
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
}