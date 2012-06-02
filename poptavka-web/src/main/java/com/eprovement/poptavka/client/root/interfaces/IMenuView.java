package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;

public interface IMenuView extends IsWidget {

    public interface IMenuPresenter {

        void goToHomeWelcome();

        void goToHomeDemands();

        void goToHomeSuppliers();

        void goToCreateSupplier();

        void goToCreateDemand();
    }

    void menuStyleChange(int loadedModule);
}
