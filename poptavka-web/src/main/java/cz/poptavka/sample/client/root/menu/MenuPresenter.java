package cz.poptavka.sample.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.root.RootEventBus;
import cz.poptavka.sample.client.root.interfaces.IMenuView;
import cz.poptavka.sample.client.root.interfaces.IMenuView.IMenuPresenter;

@Presenter(view = MenuView.class)
public class MenuPresenter extends BasePresenter<IMenuView, RootEventBus>
        implements IMenuPresenter {

    public void onStart() {
        GWT.log("Menu presenter loaded");
        eventBus.setMenu(view);
    }

//    // TODO tuto metodu mozeme odstranit. Nahradil som ju metodou onAtHome tj.
//    // po odhlaseni sa nastavi znovu Home menu
//    public void onSetHomeMenu() {
//        // TODO tuto metodu zrejme budeme volat ked budeme chciet nastavit home menu
//        // ked sa uzivatel ohlasi z user casti
//        GWT.log("set Home menu after user's logout");
//        eventBus.setMenu(view);
//    }
    /**
     * This method will set up home menu after user is logged out.
     */
    public void onAtHome() {
        GWT.log("set Home menu after user's logout");
        eventBus.setMenu(view);
    }

    // TODO praso - zistit ako mam nastavit SearchFilter pre moduly. A vyvtovirt konstanty "home"
    @Override
    public void goToHomeWelcome() {
        eventBus.goToHomeWelcomeModule(null);
    }

    @Override
    public void goToHomeDemands() {
        eventBus.goToHomeDemandsModule(null, "home");
    }

    @Override
    public void goToHomeSuppliers() {
        eventBus.goToHomeSuppliersModule(null, "home");
    }

    @Override
    public void goToCreateSupplier() {
        eventBus.goToCreateSupplierModule("home");
    }

    @Override
    public void goToCreateDemand() {
        eventBus.goToCreateDemandModule("home");
    }

}
