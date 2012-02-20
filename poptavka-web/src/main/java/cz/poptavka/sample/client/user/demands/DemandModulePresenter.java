package cz.poptavka.sample.client.user.demands;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

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
                // TODO Auto-generated method stub
            }
        });
        view.getCliOffersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });
        view.getCliAssignedDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });
        view.getCliCreateDemand().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCreateDemandModule("user");
            }
        });
        view.getCliCreateSupplier().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCreateSupplierModule("user");
            }
        });
        view.getAllDemands().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initHomeDemandsModule(null, "user");
            }
        });
        view.getAllSuppliers().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initHomeSuppliersModule(null, "user");
            }
        });

        //MENU - SUPPLIER
        view.getSupNewDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initDemandModule(null, "potentialDemands");
            }
        });
        view.getSupOffersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });
        view.getSupAssignedButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
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

    //TODO
    //later add UserDetail as parameter
    public void onInitDemandModule(SearchModuleDataHolder filter, String loadWidget) {
        Storage.setCurrentlyLoadedModule("demands");
        // hiding window for this is after succesfull Userhandler call
        Storage.showLoading(Storage.MSGS.progressDemandsLayoutInit());
//        if (user.getRoleList().contains(Role.CLIENT)) {
        // TODO execute client specific demands init methods/calls
//        }
        /*
         * InitDemandModule is just one entry point for initialization because of history.
         * Therefore need to specify, which widget to load.
         */
        //Client
        if (loadWidget.equals("myDemands")) {
            //
        } else if (loadWidget.equals("clientOffers")) {
            //
        } else if (loadWidget.equals("clientAssignedDemands")) {
            //
        //Supplier
        } else if (loadWidget.equals("potentialDemands")) {
            eventBus.initSupplierList(filter);
        } else if (loadWidget.equals("supplierOffers")) {
            //
        } else if (loadWidget.equals("supplierAssignedDemands")) {
            //
        } else { // welcome
            view.setContent(new DemandsModuleWelcomeView());
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

        eventBus.setHomeBodyHolderWidget(view.getWidgetView());
        Storage.hideLoading();
//        eventBus.setTabWidget(view.getWidgetView());
//        eventBus.fireMarkedEvent();
//
//        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    public void onDisplayView(Widget content) {
        view.setContent(content);
    }

    public void onSetUserBodyHolderWidget(IsWidget body) {
//        view.getContentPanel().add(body);
        view.setContent(body);
//        view.getContentPanel().add(new Label("TA COCO CO"));
    }
}
