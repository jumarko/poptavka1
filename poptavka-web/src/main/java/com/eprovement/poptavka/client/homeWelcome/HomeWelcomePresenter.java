package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import com.eprovement.poptavka.client.homeWelcome.texts.HowItWorks;
import com.eprovement.poptavka.client.homeWelcome.texts.HowItWorks.HowItWorksViews;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import java.util.ArrayList;

@Presenter(view = HomeWelcomeView.class)
public class HomeWelcomePresenter extends LazyPresenter<IHomeWelcomeView, HomeWelcomeEventBus> implements
        IHomeWelcomePresenter, NavigationConfirmationInterface {

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        //nothing
    }

    public void onForward() {
        eventBus.setUpSearchBar(null);
        eventBus.menuStyleChange(Constants.HOME_WELCOME_MODULE);
        view.getCategorySelectionModel().clear();
        runBanner();
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToHomeWelcomeModule() {
        eventBus.getRootCategories();
    }

    @Override
    public void bindView() {
        /** ANCHOR. **/
        addSuppliersBtnClickHandler();
        addDemandsBtnClickHandler();
        addHowItWorksSupplierBtnClickHandler();
        addHowItWorksDemandBtnClickHandler();

        /** BUTTONS. **/
        addRegisterSupplierBtnClickHandler();
        addRegisterDemandBtnClickHandler();

        /** OTHERS. **/
        addCategorySelectionModelHandler();
    }

    /**************************************************************************/
    /* Bind - helper mehtods                                                  */
    /**************************************************************************/
    /** BUTTONS. **/
    private void addSuppliersBtnClickHandler() {
        view.getSuppliersBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeSuppliersModule(null);
            }
        });
    }

    private void addDemandsBtnClickHandler() {
        view.getDemandsBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeDemandsModule(null);
            }
        });
    }

    private void addHowItWorksSupplierBtnClickHandler() {
        view.getHowItWorksSupplierBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                HowItWorks howItWorks = HowItWorks.createHowItWorksSupplier();
                howItWorks.getRegisterBtn().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        eventBus.goToCreateSupplierModule();
                    }
                });
                History.newItem(Constants.PATH_TO_TOKEN_FOR_VIEWS.concat(
                        HowItWorksViews.HOW_IT_WORKS_SUPPLIER.getValue()));
                eventBus.setBody(howItWorks);
            }
        });
    }

    private void addHowItWorksDemandBtnClickHandler() {
        view.getHowItWorksDemandBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                HowItWorks howItWorks = HowItWorks.createHowItWorksDemand();
                howItWorks.getRegisterBtn().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        eventBus.goToCreateDemandModule();
                    }
                });
                History.newItem(Constants.PATH_TO_TOKEN_FOR_VIEWS.concat(
                        HowItWorksViews.HOW_IT_WORKS_DEMAND.getValue()));
                eventBus.setBody(howItWorks);
            }
        });
    }

    private void addRegisterSupplierBtnClickHandler() {
        view.getRegisterSupplierBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateSupplierModule();
            }
        });
    }

    private void addRegisterDemandBtnClickHandler() {
        view.getRegisterDemandBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateDemandModule();
            }
        });
    }

    /** OTHERS. **/
    private void addCategorySelectionModelHandler() {
        view.getCategorySelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                CategoryDetail selected = (CategoryDetail) view.getCategorySelectionModel().getSelectedObject();

                if (selected != null) {
                    int idx = view.getDataProvider().getList().indexOf(selected);
                    eventBus.goToHomeDemandsModuleFromWelcome(idx, selected);
                }
            }
        });
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    /**
     * Methods displays root categories. These categories are divided into cellLists (columns),
     * where number of columns depends on constant: COLUMNS.
     * @param rootCategories - root categories to be displayed
     */
    public void onDisplayCategories(ArrayList<CategoryDetail> rootCategories) {
        view.displayCategories(rootCategories);
    }
    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/

    /**
     * Runs banner on HTML page.
     */
    public static native void runBanner() /*-{
     $wnd.startBanner();
     *
     }-*/;

}