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
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
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
import cz.poptavka.sample.shared.domain.adminModule.PaymentMethodDetail;
import java.util.List;
import java.util.Map;

/*
 * Musi byt mulitple = true, inak advance search views sa nebudu zobrazovat (bude vyhadzovat chybu)
 * -- len v pripade, ze budu dedit SearchModulesViewInterface, co chceme, aby voly Lazy
 */
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

    //Neviem zatial preco, ale nemoze to byt lazy, pretoze sa neinicializuci advace
    //search views.
    public interface SearchModulesViewInterface {

        SearchModuleDataHolder getFilter();

        Widget getWidgetView();
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
    public void onClearSearchContent() {
        view.getSearchContent().setText(Storage.MSGS.searchContent());
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
    private int advSearchViewInitialized = -1;

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
        if (advSearchViewInitialized != Storage.getCurrentlyLoadedView()) {
            advSearchViewInitialized = Storage.getCurrentlyLoadedView();
            switch (Storage.getCurrentlyLoadedView()) {
                case Constants.HOME_DEMANDS:
                    view.getPopupPanel().setWidget(new HomeDemandViewView());
                    break;
                case Constants.HOME_SUPPLIERS:
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
                    view.getPopupPanel().setWidget(new AdminAccessRolesViewView());
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

    /**
     * Constucts info of given filters , that will be applied and place them into given textBox.
     *
     * @param type - 0...constucts info for advance search view,
     *               1...constucts info for categories,
     *               2...constucts info for localities.
     * @param textBox - given textBox for holding info string.
     * @param searchModuleDataHolder - given filters, selected by user.
     */
    private void displayShortInfo(int type, TextBox textBox, SearchModuleDataHolder searchModuleDataHolder) {
        StringBuilder infoStr = new StringBuilder();
        switch (type) {
            case 0:
                for (FilterItem item : searchModuleDataHolder.getFilters()) {
                    infoStr.append(item.getItem());
                    infoStr.append(":");
                    switch (item.getOperation()) {
                        case FilterItem.OPERATION_EQUALS:
                            infoStr.append("=");
                            break;
                        case FilterItem.OPERATION_LIKE:
                            infoStr.append("~");
                            break;
                        case FilterItem.OPERATION_FROM:
                            infoStr.append(">");
                            break;
                        case FilterItem.OPERATION_TO:
                            infoStr.append("<");
                            break;
                        case FilterItem.OPERATION_IN:
                            infoStr.append(" in ");
                            break;
                        default:
                            break;
                    }
                    infoStr.append(item.getValue());
                }
                break;
            case 1:
                infoStr.append(searchModuleDataHolder.getCategories().toString());
                break;
            case 2:
                infoStr.append(searchModuleDataHolder.getLocalities().toString());
                break;
            default:
                break;
        }
    }
}