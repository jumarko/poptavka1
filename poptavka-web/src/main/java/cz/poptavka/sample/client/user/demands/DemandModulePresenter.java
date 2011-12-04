package cz.poptavka.sample.client.user.demands;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
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
        Anchor getCliNewDemandsAnchor();
        Anchor getCliOffersAnchor();
        Anchor getCliAssignedDemandsAnchor();
        Anchor getCliCreateDemand();
        Anchor getAllDemands();
        Anchor getAllSuppliers();

        // supplier menu part
        Anchor getSupNewDemandsAnchor();
        Anchor getSupOffersAnchor();
        Anchor getSupAssignedAnchor();

        SimplePanel getContentPanel();
    }

    public void bind() {
        // MENU - CLIENT
        view.getCliNewDemandsAnchor().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initClientList();
            }
        });
        view.getCliOffersAnchor().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });
        view.getCliAssignedDemandsAnchor().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });
        view.getCliNewDemandsAnchor().addClickHandler(new ClickHandler() {
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
        view.getSupNewDemandsAnchor().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initSupplierList();
            }
        });
        view.getSupOffersAnchor().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });
        view.getSupAssignedAnchor().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            }
        });

        //DEVEl - BEHO
        /**
         * commented but can be used when developing other views
         *
        view.getSupNewDemandsAnchor().addClickHandler(new ClickHandler() {

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
    public void onInitDemandModule(SimplePanel panel) {
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

        panel.setWidget(view.getWidgetView());
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
