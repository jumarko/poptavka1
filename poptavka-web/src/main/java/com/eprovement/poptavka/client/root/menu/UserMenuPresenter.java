package com.eprovement.poptavka.client.root.menu;

import com.eprovement.poptavka.client.common.CommonAccessRoles;
import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IUserMenuView;
import com.eprovement.poptavka.client.root.interfaces.IUserMenuView.IUserMenuPresenter;

@Presenter(view = UserMenuView.class)
public class UserMenuPresenter extends BasePresenter<IUserMenuView, RootEventBus>
        implements IUserMenuPresenter {

    public void onStart() {
    }

    /**
     * Jedina matoda, ktora nahra UserMenu pohlad do Root menu layoutu. Ak je prave
     * prihlaseny uzivatel administrator, zobrazi administatorsky tab.
     */
    public void onAtAccount() {
        GWT.log("User menu view loaded");
        //Tab visibility must be defined here, because unlike constructor in UserMenuView
        //this method is called each time user is logging in
        /* ADMIN TAB */
        if (Storage.getUser().getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
            view.setTabVisibility(Constants.USER_ADMININSTRATION_MODULE, true);
        } else {
            view.setTabVisibility(Constants.USER_ADMININSTRATION_MODULE, false);
        }
        //Odkomentovat ak Ivan dorobi setovanie businessUsera do Storage
//        /* SUPPLIER TAB */
//        if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
//                BusinessUserDetail.BusinessRole.SUPPLIER)) {
//            view.setTabVisibility(Constants.USER_CLIENT_MODULE, true);
//            view.setTabVisibility(Constants.USER_SUPPLIER_MODULE, true);
//            /* CLIENT TAB */
//        } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
//                BusinessUserDetail.BusinessRole.CLIENT)) {
//            view.setTabVisibility(Constants.USER_CLIENT_MODULE, true);
//            view.setTabVisibility(Constants.USER_SUPPLIER_MODULE, false);
//        }
        eventBus.setMenu(view);
    }

    /**************************************************************************/
    /* Navigation methods.                                                    */
    /**************************************************************************/
    @Override
    public void goToAdministration() {
        eventBus.goToAdminModule(null, Constants.NONE);
    }

    @Override
    public void goToSettings() {
        eventBus.goToSettingsModule();
    }

    @Override
    public void goToMessages() {
        eventBus.goToMessagesModule(null, Constants.MESSAGES_INBOX);
    }

    @Override
    public void goToClient() {
        eventBus.goToClientDemandsModule(null, Constants.NONE);
    }

    @Override
    public void goToSupplier() {
        eventBus.goToSupplierDemandsModule(null, Constants.NONE);
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    /**
     * Loads right styles to menu buttons.
     * @param loadedModule - use module constants from class Contants.
     */
    public void onUserMenuStyleChange(int loadedModule) {
        view.userMenuStyleChange(loadedModule);
    }
}
