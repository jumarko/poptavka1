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
        if (Storage.getUser().getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
            view.setTabVisibility(Constants.USER_ADMININSTRATION_MODULE, true);
        } else {
            view.setTabVisibility(Constants.USER_ADMININSTRATION_MODULE, false);
        }
        //TODO Martin - pridat podmienky pre zobrazovanie client vs supplier tabu
        //podla business role uzivatela - viem to zistit v tejto fazi?
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
    public void goToDemands() {
        eventBus.goToDemandModule(null, Constants.NONE);
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
