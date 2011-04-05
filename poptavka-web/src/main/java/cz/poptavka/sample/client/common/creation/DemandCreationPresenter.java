package cz.poptavka.sample.client.common.creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.client.common.creation.DemandCreationView.TopPanel;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;

@Presenter(view = DemandCreationView.class)
public class DemandCreationPresenter
    extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, CommonEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DemandCreationPresenter");
    private int nextStep = 0;

    private DemandDetail newDemand = new DemandDetail();

    /** View interface methods. **/
    public interface CreationViewInterface extends LazyView {

        //click handlers
        HasClickHandlers oneNextButton();
        HasClickHandlers twoBackButton();
        HasClickHandlers twoNextButton();
        HasClickHandlers threeBackButton();
        HasClickHandlers threeNextButton();
        HasClickHandlers fourBackButton();
        HasClickHandlers fourNextButton();
        HasClickHandlers fiveBackButton();
        HasClickHandlers fiveCreateButton();

        //part1
//        DateBox getFinishDatePicker();
//        DateBox getExpireDatePicker();

        //nav methods
        void toggleVisiblePanel(TopPanel newPanel);

        /** holder panels **/
        SimplePanel getBasicInfoHolder();
        SimplePanel getLocalityHolder();
        SimplePanel getCategoryHolder();
        SimplePanel getAdvInfoHolder();

        Widget getWidgetView();

        //test
        VerticalPanel getVerticalPanel();
        void cascadeTogglePanel(TopPanel second);

        /** Login, registration section **/

        SimplePanel getUserFormHolder();
    }

    private boolean initLocality = true;
    private boolean initCategory = true;
    private boolean initAdvInfo = true;
    private ClickHandler backBtnHanlder;

    /** Init presenter. **/
    @Override
    public void createPresenter() {
        initBackButtonHanlder();
    }

    public void bindView() {
        view.oneNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.validateDemandBasicForm();
            }
        });

        view.twoNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.formNextStep();
            }
        });

        view.threeNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.formNextStep();
            }
        });

        view.fourNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.validateDemandAdvForm();
            }
        });

        view.twoBackButton().addClickHandler(backBtnHanlder);
        view.threeBackButton().addClickHandler(backBtnHanlder);
        view.fourBackButton().addClickHandler(backBtnHanlder);
        view.fiveBackButton().addClickHandler(backBtnHanlder);

        view.fiveCreateButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                submitNewDemand();
            }
        });



        /** TEST **/
        HTML x = (HTML) view.getVerticalPanel().getWidget(0);
        x.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.cascadeTogglePanel(TopPanel.SECOND);
            }
        });
        /** END TEST **/
    }

    private void initBackButtonHanlder() {
        this.backBtnHanlder = new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.formBackStep();
            }
        };
    }

    public void onAtCreateDemand(boolean homeSection) {
        LOGGER.info("Initializing Demand Creation View Widget ... ");
        eventBus.setAnchorWidget(homeSection, AnchorEnum.THIRD, view.getWidgetView(), true);
        //init parts
        eventBus.initDemandBasicForm(view.getBasicInfoHolder());
        eventBus.initFormLogin(view.getUserFormHolder());
    }

    public void onFormNextStep() {
        switch (nextStep) {
            case 0:
                view.toggleVisiblePanel(TopPanel.SECOND);
                if (initCategory) {
                    eventBus.initCategoryWidget(view.getCategoryHolder());
//                    initCategory = false;
                }
                break;
            case 1:
                view.toggleVisiblePanel(TopPanel.THIRD);
                if (initLocality) {
                    eventBus.initLocalityWidget(view.getLocalityHolder());
//                    initLocality = false;
                }
                break;
            case 2:
                view.toggleVisiblePanel(TopPanel.FOURTH);
                if (initAdvInfo) {
                    eventBus.initDemandAdvForm(view.getAdvInfoHolder());
//                    initAdvInfo = false;
                }
                break;
            case 3:
                view.toggleVisiblePanel(TopPanel.FIFTH);
                break;
            default:
                break;
        }
        nextStep++;
    }

    public void onFormBackStep() {
        view.toggleVisiblePanel(TopPanel.REMOVE);
        nextStep--;
    }


    public void onPushBasicInfoValues(HashMap<String, Object> basicValues) {
        newDemand.setBasicInfo(basicValues);
        eventBus.getSelectedCategoryCodes();
    }

    public void onPushSelectedCategoryCodes(ArrayList<String> categories) {
        newDemand.setCategories(categories);
        eventBus.getSelectedLocalityCodes();
    }

    public void onPushSelectedLocalityCodes(ArrayList<String> localities) {
        newDemand.setLocalities(localities);
        eventBus.getAdvInfoValues();
    }

    public void onPushAdvInfoValues(HashMap<String, Object> advValues) {
        newDemand.setAdvInfo(advValues);
        submitNewDemand();
    }

    private void submitNewDemand() {
        LOGGER.fine("submiting new demand");
        ClientDetail newClient = new ClientDetail("email@domena.cz");

        eventBus.createDemand(newDemand, new Long(1));

    }

}
