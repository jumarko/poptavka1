package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;

public interface IUserMenuView extends IsWidget {

    public interface IUserMenuPresenter {

        void goToClient();

        void goToSupplier();

        void goToMessages();

        void goToDemands();

        void goToCreateDemands();

        void goToSuppliers();

        void goToCreateSupplier();

        void goToAdministration();
    }

    void userMenuStyleChange(int loadedModule);

    void setTabVisibility(int module, boolean visible);
}
