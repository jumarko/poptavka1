package cz.poptavka.sample.client.common.creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.shared.domain.DemandDetail;

@Presenter(view = DemandCreationView.class)
public class DemandCreationPresenter
    extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, CommonEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DemandCreationPresenter");

    public interface CreationViewInterface extends LazyView {

        SimplePanel getBasicInfoHolder();

        SimplePanel getLocalityHolder();

        SimplePanel getCategoryHolder();

        SimplePanel getAdvInfoHolder();

        SimplePanel getUserFormHolder();

        HasClickHandlers getCreateDemandButton();

        Widget getWidgetView();

        void toggleRegAndCreateButton();
    }

    private DemandDetail newDemand = new DemandDetail();

    private boolean loggedUser = true;
    private long clientId;

    public void bindView() {
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.submitUserForm();
            }
        });
    }


    /**
     * Init method call.
     *
     * @param homeSection
     */
    public void onAtCreateDemand(boolean homeSection) {
        LOGGER.info("Initializing Demand Creation View Widget ... ");
        eventBus.setAnchorWidget(homeSection, AnchorEnum.THIRD, view.getWidgetView(), true);
//        //init parts
        LOGGER.info(" -> Basic Form");
        eventBus.initDemandBasicForm(view.getBasicInfoHolder());
        LOGGER.info(" -> Category Widget");
        eventBus.initCategoryWidget(view.getCategoryHolder());
        LOGGER.info(" -> Locality Widget");
        eventBus.initLocalityWidget(view.getLocalityHolder());
        LOGGER.info(" -> Advanced Form");
        eventBus.initDemandAdvForm(view.getAdvInfoHolder());
        LOGGER.info(" -> User Holder Form");
        eventBus.initFormLogin(view.getUserFormHolder());
    }

    public void onPushBasicInfoValues(HashMap<String, Object> basicValues) {
        LOGGER.info("setting basic info");
        newDemand.setBasicInfo(basicValues);
        eventBus.getSelectedCategoryCodes();
        LOGGER.info("After eventBus call");
    }

    public void onPushSelectedCategoryCodes(ArrayList<String> categories) {
        LOGGER.info("setting categories");
        newDemand.setCategories(categories);
        eventBus.getSelectedLocalityCodes();
    }

    public void onPushSelectedLocalityCodes(ArrayList<String> localities) {
        LOGGER.info("setting localities");
        newDemand.setLocalities(localities);
        eventBus.getAdvInfoValues();
    }

    public void onPushAdvInfoValues(HashMap<String, Object> advValues) {
        LOGGER.info("setting advanced info");
        newDemand.setAdvInfo(advValues);
        onSubmitNewDemand();
    }

    public void onSubmitNewDemand() {
        LOGGER.fine("submiting new demand");
        eventBus.createDemand(newDemand, clientId);
    }

    public void onSetClientId(long id) {
        this.clientId = id;
        LOGGER.info("Current User's Id is: " + clientId);
    }

    /** Done automatically in step five, when option: register new client is selected. **/
    //DO NOT EDIT
    public void onToggleCreateAndRegButton() {
        view.toggleRegAndCreateButton();
    }

}
