package com.eprovement.poptavka.client.homeWelcome.interfaces;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;

public interface IHomeWelcomeView extends IsWidget {

    public interface IHomeWelcomePresenter {
    }

    FlowPanel getCategorySection();

    SingleSelectionModel<CategoryDetail> getCategorySelectionModel();

    void displayCategories(int columns, ArrayList<CategoryDetail> categories);

    Widget getWidgetView();

    /** ANCHOR. **/
    Button getSuppliersBtn();

    Button getDemandsBtn();

    Button getHowItWorksSupplierBtn();

    Button getHowItWorksDemandBtn();

    /** BUTTONS. **/
    Button getRegisterSupplierBtn();

    Button getRegisterDemandBtn();

    HasClickHandlers getCreateDemandButton();

    HasClickHandlers getSecuredButton();

    HasClickHandlers getSendUsEmailButton();
}
