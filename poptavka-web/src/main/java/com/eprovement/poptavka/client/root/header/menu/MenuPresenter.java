package com.eprovement.poptavka.client.root.header.menu;

import com.eprovement.poptavka.client.common.CommonAccessRoles;
import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IMenuView;
import com.eprovement.poptavka.client.root.interfaces.IMenuView.IUserMenuPresenter;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mvp4g.client.SingleSplitter;

@Presenter(view = MenuView.class, async = SingleSplitter.class)
public class MenuPresenter extends BasePresenter<IMenuView, RootEventBus>
        implements IUserMenuPresenter {

    public void onStart() {
        eventBus.setMenu(view);
    }

    public void onAtHome() {
        view.getHome().setVisible(true);
        view.getClient().setVisible(false);
        view.getSupplier().setVisible(false);
        view.getDemands().setVisible(true);
        view.getSuppliers().setVisible(true);
        view.getCreateDemand().setVisible(true);
        view.getCreateSupplier().setVisible(true);
        view.getInbox().setVisible(false);
        view.getAdministration().setVisible(false);
    }
    /**
     * Jedina matoda, ktora nahra UserMenu pohlad do Root menu layoutu. Ak je prave
     * prihlaseny uzivatel administrator, zobrazi administatorsky tab.
     */
    public void onAtAccount() {
        GWT.log("User menu view loaded");
        view.getHome().setVisible(false);
        view.getInbox().setVisible(true);
        //Tab visibility must be defined here, because unlike constructor in UserMenuView
        //this method is called each time user is logging in
        /* ADMIN TAB */
        if (Storage.getUser().getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
            view.getClient().setVisible(false);
            view.getSupplier().setVisible(false);
            view.getDemands().setVisible(false);
            view.getSuppliers().setVisible(false);
            view.getCreateDemand().setVisible(false);
            view.getCreateSupplier().setVisible(false);
            view.getAdministration().setVisible(true);
            view.menuStyleChange(Constants.USER_ADMININSTRATION_MODULE);
        } else {
            /* SUPPLIER TAB */
            if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                    BusinessUserDetail.BusinessRole.SUPPLIER)) {
                view.getClient().setVisible(true);
                view.getSupplier().setVisible(true);
                view.getDemands().setVisible(true);
                view.getSuppliers().setVisible(true);
                view.getCreateDemand().setVisible(true);
                view.getCreateSupplier().setVisible(false);
                view.getAdministration().setVisible(false);
                view.menuStyleChange(Constants.USER_SUPPLIER_MODULE);
                view.setSupplierButtonVerticalNoLine(true);
                /* CLIENT TAB */
            } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                    BusinessUserDetail.BusinessRole.CLIENT)) {
                view.getClient().setVisible(true);
                view.getSupplier().setVisible(false);
                view.getDemands().setVisible(true);
                view.getSuppliers().setVisible(true);
                view.getCreateDemand().setVisible(true);
                view.getCreateSupplier().setVisible(true);
                view.getAdministration().setVisible(false);
                view.menuStyleChange(Constants.USER_CLIENT_MODULE);
                view.setSupplierButtonVerticalNoLine(false);
            }
        }
    }

    /**************************************************************************/
    /* Bind methods.                                                          */
    /**************************************************************************/
    @Override
    public void bind() {
        view.getHome().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeWelcomeModule();
            }
        });
        view.getClient().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.NONE);
            }
        });
        view.getSupplier().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.NONE);
            }
        });
        view.getInbox().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToMessagesModule(null, Constants.MESSAGES_INBOX);
            }
        });
        view.getAdministration().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.NONE);
            }
        });
        view.getDemands().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeDemandsModule(null);
            }
        });
        view.getSuppliers().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeSuppliersModule(null);
            }
        });
        view.getCreateDemand().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateDemandModule();
            }
        });
        view.getCreateSupplier().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateSupplierModule();
            }
        });
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    /**
     * Loads right styles to menu buttons.
     * @param loadedModule - use module constants from class Contants.
     */
    public void onMenuStyleChange(int loadedModule) {
        view.menuStyleChange(loadedModule);
    }
}
