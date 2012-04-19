package cz.poptavka.sample.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.root.RootEventBus;
import cz.poptavka.sample.client.root.interfaces.IUserMenuView;
import cz.poptavka.sample.client.root.interfaces.IUserMenuView.IUserMenuPresenter;

@Presenter(view = UserMenuView.class)
public class UserMenuPresenter extends BasePresenter<IUserMenuView, RootEventBus>
        implements IUserMenuPresenter {

    public void onStart() {
    }

    /**
     * Jedina matoda, ktora nahra UserMenu pohlad do Root menu layoutu.
     */
    public void onAtAccount() {
        GWT.log("User menu view loaded");
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
