package cz.poptavka.sample.client.user.demands;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

/**
 * For every user - default tab
 *
 * Consists of left menu only and holder for demands related
 * stuff. Contains list of all demands for faster working with
 * demands.
 *
 * @author Beho
 */
@Presenter(view = DemandModuleView.class, multiple = true)
public class DemandModulePresenter
        extends BasePresenter<DemandModulePresenter.DemandsLayoutInterface, DemandModuleEventBus> {

    public interface DemandsLayoutInterface {

        Widget getWidgetView();

        void setContent(IsWidget contentWidget);

        //client menu part
        Button getCliNewDemandsButton();

        Button getCliOffersButton();

        Button getCliAssignedDemandsButton();

        Button getCliCreateDemand();

        Button getCliCreateSupplier();

        Button getAllDemands();

        Button getAllSuppliers();

        // supplier menu part
        Button getSupNewDemandsButton();

        Button getSupOffersButton();

        Button getSupAssignedButton();

        SimplePanel getContentPanel();
    }

    @Override
    public void bind() {
        // MENU - CLIENT
        view.getCliNewDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_CLIENT_MY_DEMANDS);
            }
        });
        view.getCliOffersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_CLIENT_OFFERS);
            }
        });
        view.getCliAssignedDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_CLIENT_ASSIGNED_DEMANDS);
            }
        });
        view.getCliCreateDemand().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateDemandModule();
            }
        });
        view.getCliCreateSupplier().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateSupplierModule();
            }
        });
        view.getAllDemands().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeDemandsModule(null);
            }
        });
        view.getAllSuppliers().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeSuppliersModule(null);
            }
        });

        //MENU - SUPPLIER
        view.getSupNewDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_SUPPLIER_MY_DEMANDS);
            }
        });
        view.getSupOffersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_SUPPLIER_OFFERS);
            }
        });
        view.getSupAssignedButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_SUPPLIER_ASSIGNED_DEMANDS);
            }
        });

        //DEVEl - BEHO
        /**
         * commented but can be used when developing other views
         *
        view.getSupNewDemandsButton().addClickHandler(new ClickHandler() {

        @Override
        public void onClick(ClickEvent arg0) {
        //devel code
        if (supList != null) {
        supList.develRemoveDetailWrapper();
        eventBus.removeHandler(supList);
        supList = null;
        view.getContentPanel().remove(view.getContentPanel().getWidget());
        }
        supList = eventBus.addHandler(SupplierListPresenter.class);
        supList.onInitSupplierList();

        //production code
        //                eventBus.initSupplierList();
        }
        });
         */
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    /**
     * When your application starts, you may want to automatically fire an event
     * so that actions needed at first can occur.
     */
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    //Todo Beho - later add UserDetail as parameter
    public void onGoToDemandModule(SearchModuleDataHolder filter, int loadWidget) {
        Storage.setCurrentlyLoadedModule(Constants.USER_DEMANDS_MODULE);
        // hiding window for this is after succesfull Userhandler call
        Storage.showLoading(Storage.MSGS.progressDemandsLayoutInit());
//        if (user.getRoleList().contains(Role.CLIENT)) {
        // TODO execute client specific demands init methods/calls
//        }
        /*
         * goToDemandModule is just one entry point for initialization because of history.
         * Therefore need to specify, which widget to load.
         */
        //Client
        switch (loadWidget) {
            case Constants.DEMANDS_CLIENT_MY_DEMANDS:
                eventBus.initClientList(filter);
                break;
            case Constants.DEMANDS_CLIENT_OFFERS:
                break;
            case Constants.DEMANDS_CLIENT_ASSIGNED_DEMANDS:
                break;
            case Constants.DEMANDS_SUPPLIER_MY_DEMANDS:
                eventBus.initSupplierList(filter);
                break;
            case Constants.DEMANDS_SUPPLIER_OFFERS:
                break;
            case Constants.DEMANDS_SUPPLIER_ASSIGNED_DEMANDS:
                break;
            default:
                Storage.setCurrentlyLoadedView(Constants.NONE);
                view.setContent(new DemandsModuleWelcomeView());
                break;
        }

//        if (user.getRoleList().contains(Role.SUPPLIER)) {
        // TODO using businessUserId and NOT supplier ID
        // DEBUGING popup
        // TODO Maybe do nothing
//            PopupPanel panel = new PopupPanel(true);
//            panel.getElement().setInnerHTML("<br/>Getting SupplierDemands<")
//            panel.center();
//            eventBus.getPotentialDemands(user.getId());
//        }

        // STYLE
        view.getWidgetView().setStyleName(Storage.RSCS.common().user());

        Storage.hideLoading();
//        eventBus.setTabWidget(view.getWidgetView());
//        eventBus.fireMarkedEvent();
//
//        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onDisplayView(Widget content) {
        view.setContent(content);
    }
}
