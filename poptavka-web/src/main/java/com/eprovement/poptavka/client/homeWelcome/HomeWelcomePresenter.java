package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import com.eprovement.poptavka.client.homeWelcome.texts.HowItWorks;
import com.eprovement.poptavka.client.service.demand.SimpleRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.detail.FeedbackPopupView;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.BasePresenter;
import java.util.ArrayList;

@Presenter(view = HomeWelcomeView.class)
public class HomeWelcomePresenter extends BasePresenter<IHomeWelcomeView, HomeWelcomeEventBus> implements
        IHomeWelcomePresenter, NavigationConfirmationInterface {

    //columns number of root chategories in parent widget
    private SimpleRPCServiceAsync simpleService;

    //TODO remove this and all relevant code, if security development is finnished
    @Inject
    void setSimpleService(SimpleRPCServiceAsync service) {
        simpleService = service;
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        //nothing
    }

    public void onForward() {
        eventBus.setFooter(view.getFooterHolder());
        eventBus.setUpSearchBar(null);
        eventBus.menuStyleChange(Constants.HOME_WELCOME_MODULE);
        view.getCategorySelectionModel().clear();
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
    public void bind() {
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

        view.getCreateDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // do something
                simpleService.getData(new SecuredAsyncCallback<String>(eventBus) {
                    public void onServiceFailure(Throwable caught) {
                        // Show the RPC error message to the user
                        DialogBox dialogBox = new DialogBox();
                        dialogBox.center();
                        dialogBox.setModal(true);
                        dialogBox.setGlassEnabled(true);
                        dialogBox.setAutoHideEnabled(true);

                        dialogBox.setText("Remote Procedure Call - Failure");
                        dialogBox.show();
                    }

                    public void onSuccess(String result) {
                        DialogBox dialogBox = new DialogBox();
                        dialogBox.center();
                        dialogBox.setModal(true);
                        dialogBox.setGlassEnabled(true);
                        dialogBox.setAutoHideEnabled(true);

                        dialogBox.setText(result);
                        dialogBox.show();
                    }
                });
            }
        });

        view.getSecuredButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // do something
                simpleService.getSecuredData(new SecuredAsyncCallback<String>(eventBus) {
                    public void onServiceFailure(Throwable caught) {
                        // Show the RPC error message to the user
                        DialogBox dialogBox = new DialogBox();
                        dialogBox.center();
                        dialogBox.setModal(true);
                        dialogBox.setGlassEnabled(true);
                        dialogBox.setAutoHideEnabled(true);

                        dialogBox.setText("Remote Procedure Call - Failure");
                        dialogBox.show();
                    }

                    public void onSuccess(String result) {
                        DialogBox dialogBox = new DialogBox();
                        dialogBox.center();
                        dialogBox.setModal(true);
                        dialogBox.setGlassEnabled(true);
                        dialogBox.setAutoHideEnabled(true);

                        dialogBox.setText(result);
                        dialogBox.show();
                    }
                });
            }
        });

        view.getSendUsEmailButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //TODO Martin - use for invoking variety popups for development
//                eventBus.sendUsEmail(Constants.SUBJECT_GENERAL_QUESTION, "");
//                SecurityDialogBoxes.showAlertBox(eventBus, Storage.MSGS.errorTipTryWaiting());
//                SecurityDialogBoxes.showAccessDeniedBox(eventBus);
                new FeedbackPopupView(FeedbackPopupView.CLIENT);
//                ExceptionUtils.showErrorDialog("error", "errorMessage");
            }
        });

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
                eventBus.setBody(HowItWorks.createHowItWorksSupplier());
            }
        });
    }

    private void addHowItWorksDemandBtnClickHandler() {
        view.getHowItWorksDemandBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.setBody(HowItWorks.createHowItWorksDemand());
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
}
