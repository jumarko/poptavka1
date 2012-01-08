package cz.poptavka.sample.client.user.demands;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

public interface IDemandModuleView { //extends IsWidget {

//    public interface IDemandModulePresenter {
//    }
    Widget getWidgetView();

    void setContent(Widget contentWidget);

    //client menu part
    Button getCliNewDemandsButton();

    Button getCliOffersButton();

    Button getCliAssignedDemandsButton();

    Button getCliCreateDemand();

    Button getAllDemands();

    Button getAllSuppliers();

    // supplier menu part
    Button getSupNewDemandsButton();

    Button getSupOffersButton();

    Button getSupAssignedButton();

    SimplePanel getContentPanel();

    void setRoleInterface(Role role);
}
