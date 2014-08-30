package com.eprovement.poptavka.client.homeWelcome.interfaces;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ILesserCatLocDetail;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

/**
 * Home Welcome view interface
 * @author Martin Slavkovsky
 */
public interface IHomeWelcomeView extends IsWidget, LazyView {

    /**
     * Home Welcome presenter interface
     */
    public interface IHomeWelcomePresenter {
    }

    SingleSelectionModel<ILesserCatLocDetail> getCategorySelectionModel();

    ListDataProvider getDataProvider();

    void displayCategories(ArrayList<ILesserCatLocDetail> categories);

    SimplePanel getFooterContainer();

    Widget getWidgetView();

    /** ANCHOR. **/
    Button getSuppliersBtn();

    Button getDemandsBtn();

    Button getHowItWorksSupplierBtn();

    Button getHowItWorksDemandBtn();

    /** BUTTONS. **/
    Button getRegisterSupplierBtn();

    Button getRegisterDemandBtn();
}