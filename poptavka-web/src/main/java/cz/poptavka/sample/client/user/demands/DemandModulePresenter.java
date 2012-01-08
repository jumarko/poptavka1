package cz.poptavka.sample.client.user.demands;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;

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
        extends
        BasePresenter<DemandModulePresenter.DemandsLayoutInterface, DemandModuleEventBus> {

    public interface DemandsLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        //client menu part
        Button getCliNewDemandsButton();
        Button getCliOffersButton();
        Button getCliAssignedDemandsButton();
        Button getCliCreateDemand();
        Button getAllDemands();
        Button getAllSuppliers();

        // supplier menu part
        Button getSupNewDemandsButton();
        Button getSupOffersButton();
        Button getSupAssignedButton();

        SimplePanel getContentPanel();
    }

    public void bind() {
        // MENU - CLIENT
        view.getCliNewDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initClientList();
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
        view.getCliNewDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });
        view.getAllDemands().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });
        view.getAllSuppliers().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });

        //MENU - SUPPLIER
        view.getSupNewDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initSupplierList(null);
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
    public void onInitDemandModule() {
        // hiding window for this is after succesfull Userhandler call
        Storage.showLoading(Storage.MSGS.progressDemandsLayoutInit());
//        if (user.getRoleList().contains(Role.CLIENT)) {
            // TODO execute client specific demands init methods/calls
//        }
//        if (user.getRoleList().contains(Role.SUPPLIER)) {
            // TODO using businessUserId and NOT supplier ID
            // DEBUGING popup
            // TODO Maybe do nothing
//            PopupPanel panel = new PopupPanel(true);
//            panel.getElement().setInnerHTML("<br/>Getting SupplierDemands<")
//            panel.center();
//            eventBus.getPotentialDemands(user.getId());
//        }

//        panel.setWidget(view.getWidgetView());
        view.getWidgetView().setStyleName(Storage.RSCS.common().user());
        eventBus.setBodyHolderWidget(view.getWidgetView());
        Storage.hideLoading();
//        eventBus.setTabWidget(view.getWidgetView());
//        eventBus.fireMarkedEvent();
//
//        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    public void onDisplayView(Widget content) {
        view.setContent(content);
    }

}
