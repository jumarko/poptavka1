package com.eprovement.poptavka.client.homeWelcome.interfaces;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

public interface IHomeWelcomeView extends IsWidget, LazyView {

    public interface IHomeWelcomePresenter {
    }

    FlowPanel getCategorySection();

    SingleSelectionModel<CategoryDetail> getCategorySelectionModel();

    ListDataProvider getDataProvider();

    void displayCategories(ArrayList<CategoryDetail> categories);

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
