package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

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

    Button getMenuOpenButton();

    Label getUserLabel();

    Button getUserSettings();

    Button getUserLogout();

    HTMLPanel getUserMenuPanel();

    void setMenuPanelVisibility();

    void resetMenuVisbilityFlag();

    void hideMenu();

    void menuStyleChange(int loadedModule);

    void setSupplierButtonVerticalNoLine(boolean noLine);
}
