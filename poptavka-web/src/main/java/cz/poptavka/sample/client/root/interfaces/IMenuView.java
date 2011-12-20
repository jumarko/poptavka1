package cz.poptavka.sample.client.root.interfaces;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;

public interface IMenuView extends IsWidget {
    public interface IMenuPresenter {

    }

    HasClickHandlers getDemandsButton();

    HasClickHandlers getSuppliersButton();

    HasClickHandlers getCreateSupplierButton();

    HasClickHandlers getCreateDemandButton();

    void setHomeToken(String token);

}
