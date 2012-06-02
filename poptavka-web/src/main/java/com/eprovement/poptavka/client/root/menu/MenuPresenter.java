package com.eprovement.poptavka.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IMenuView;
import com.eprovement.poptavka.client.root.interfaces.IMenuView.IMenuPresenter;

@Presenter(view = MenuView.class)
public class MenuPresenter extends BasePresenter<IMenuView, RootEventBus>
        implements IMenuPresenter {

    public void onStart() {
        GWT.log("Menu presenter loaded");
        eventBus.setMenu(view);
    }

    /**
     * This method will set up home menu after user is logged out.
     */
    public void onAtHome() {
        GWT.log("set Home menu after user's logout");
        eventBus.setMenu(view);
    }

    // TODO praso - zistit ako mam nastavit SearchFilter pre moduly. A vyvtovirt konstanty "home"
    /**************************************************************************/
    /* Navigation methods.                                                    */
    /**************************************************************************/
    @Override
    public void goToHomeWelcome() {
        eventBus.goToHomeWelcomeModule(null);
    }

    @Override
    public void goToHomeDemands() {
        eventBus.goToHomeDemandsModule(null);
    }

    @Override
    public void goToHomeSuppliers() {
        eventBus.goToHomeSuppliersModule(null);
    }

    @Override
    public void goToCreateSupplier() {
        eventBus.goToCreateSupplierModule();
    }

    @Override
    public void goToCreateDemand() {
        eventBus.goToCreateDemandModule();
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
