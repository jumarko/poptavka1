package cz.poptavka.sample.client.common.creation;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import cz.poptavka.sample.client.common.creation.widget.FormDemandAdvPresenter.FormDemandAdvViewInterface;
import cz.poptavka.sample.client.common.creation.widget.FormDemandBasicPresenter.FormDemandBasicInterface;
import cz.poptavka.sample.client.common.creation.widget.FormUserRegistrationPresenter.FormRegistrationInterface;
import cz.poptavka.sample.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import cz.poptavka.sample.client.common.widget.ProvidesValidate;
import cz.poptavka.sample.client.common.widget.StatusIconLabel;
import cz.poptavka.sample.client.common.widget.StatusIconLabel.State;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;

@Presenter(view = DemandCreationView.class)
public class DemandCreationPresenter
    extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, CommonEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DemandCreationPresenter");

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public interface CreationViewInterface extends LazyView {

        StackLayoutPanel getMainPanel();

        SimplePanel getBasicInfoHolder();

        SimplePanel getLocalityHolder();

        SimplePanel getCategoryHolder();

        SimplePanel getAdvInfoHolder();

        SimplePanel getUserFormHolder();

        HasClickHandlers getCreateDemandButton();

        Widget getWidgetView();

        void toggleRegAndCreateButton();

        StatusIconLabel getStatusLabel(int order);
    }

    private DemandDetail newDemand = new DemandDetail();

    private boolean loggedUser = true;
    private long clientId;

    public void bindView() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (view.getMainPanel().getVisibleIndex() < eventItem) {
                    boolean result = canContinue(eventItem);
                    view.getStatusLabel(eventItem).setPassedSmall(result);
                    if (!canContinue(eventItem)) {
                        event.cancel();
                    }
                }
            }
        });
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                if (canContinue(LOGIN)) {
                    registerNewCient();
                }
            }
        });
    }

    /**
     * Init method call. TODO decide when other parts should be built.
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
        eventBus.initLoginForm(view.getUserFormHolder());
    }

    private void registerNewCient() {
        FormRegistrationInterface registerWidget = (FormRegistrationInterface) view.getUserFormHolder().getWidget();
        ClientDetail newClient = registerWidget.getNewClient();
        eventBus.registerNewClient(newClient);
        //signal event
        eventBus.displayLoadingPopup(MSGS.progressRegisterClient());
    }

    /**
     * Called from Commonhandler after successful login/registration
     *
     * @param clientId id of newly created client/id of logged user
     */
    public void onPrepareNewDemandForNewClient(Long clientId) {
        eventBus.changeLoadingMessage(MSGS.progressGettingDemandData());
        this.clientId = clientId;

        FormDemandBasicInterface basicValues = (FormDemandBasicInterface) view.getBasicInfoHolder().getWidget();
        CategorySelectorInterface categoryValues = (CategorySelectorInterface) view.getCategoryHolder().getWidget();
        LocalitySelectorInterface localityValues = (LocalitySelectorInterface) view.getLocalityHolder().getWidget();
        FormDemandAdvViewInterface advValues = (FormDemandAdvViewInterface) view.getAdvInfoHolder().getWidget();

        DemandDetail demand = new DemandDetail();
        demand.setBasicInfo(basicValues.getValues());
        demand.setCategories(categoryValues.getSelectedCategoryCodes());
        demand.setLocalities(localityValues.getSelectedLocalityCodes());
        demand.setAdvInfo(advValues.getValues());

        eventBus.createDemand(demand, clientId);
        eventBus.changeLoadingMessage(MSGS.progressCreatingDemand());
    }

    /** Done automatically in step five, when option: register new client is selected. **/
    //DO NOT EDIT
    public void onToggleCreateAndRegButton() {
        view.toggleRegAndCreateButton();
    }

    /** showing errof after login failure **/
    public void onShowLoginError() {
        view.getStatusLabel(LOGIN).setStyleState(StyleResource.INSTANCE.common().errorMessage(), State.ERROR_16);
        view.getStatusLabel(LOGIN).setTexts(MSGS.wrongLoginMessage(), MSGS.wrongLoginDescription());
    }

    private static final int BASIC = 1;
    private static final int CATEGORY = 2;
    private static final int LOCALITY = 3;
    private static final int ADVANCED = 4;
    private static final int LOGIN = 5;

    private boolean canContinue(int step) {
        if (step == BASIC) {
            FormDemandBasicInterface widget =
                (FormDemandBasicInterface) view.getBasicInfoHolder().getWidget();
            return widget.isValid();
        }
        if (step == CATEGORY) {
            CategorySelectorInterface widget =
                (CategorySelectorInterface) view.getCategoryHolder().getWidget();
            return widget.isValid();
        }
        if (step == LOCALITY) {
            LocalitySelectorInterface widget =
                (LocalitySelectorInterface) view.getLocalityHolder().getWidget();
            return widget.isValid();
        }
        if (step == ADVANCED) {
            FormDemandAdvViewInterface widget =
                (FormDemandAdvViewInterface) view.getAdvInfoHolder().getWidget();
            return widget.isValid();
        }
        if (step == LOGIN) {
            ProvidesValidate widget = (ProvidesValidate) view.getUserFormHolder().getWidget();
            return widget.isValid();
        }
        //can't reach
        return false;
    }
}
