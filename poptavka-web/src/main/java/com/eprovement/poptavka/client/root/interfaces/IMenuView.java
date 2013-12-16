package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

public interface IMenuView extends IsWidget {

    public interface IUserMenuPresenter {
    }

    Button getHome();

    Button getClient();

    Button getSupplier();

    Button getDemands();

    Button getCreateDemand();

    Button getSuppliers();

    Button getCreateSupplier();

    Button getInbox();

    Button getAdministration();

    void menuStyleChange(int loadedModule);

    void setSupplierButtonVerticalNoLine(boolean noLine);
}
