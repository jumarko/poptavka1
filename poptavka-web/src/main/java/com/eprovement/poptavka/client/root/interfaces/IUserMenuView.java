package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;

public interface IUserMenuView extends IsWidget {

    public interface IUserMenuPresenter {

        void goToAdministration();

        void goToSettings();

        void goToMessages();

        void goToDemands();

        void goToClient();

        void goToSupplier();
    }

    void userMenuStyleChange(int loadedModule);

    void setAdminTabVisibility(boolean visible);
}
