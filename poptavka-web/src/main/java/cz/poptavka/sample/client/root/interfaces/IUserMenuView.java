package cz.poptavka.sample.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;

public interface IUserMenuView extends IsWidget {

    public interface IUserMenuPresenter {

        void goToAdministration();

        void goToSettings();

        void goToMessages();

        void goToDemands();
    }

    void userMenuStyleChange(int loadedModule);
}
