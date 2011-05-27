package cz.poptavka.sample.client.home.creation;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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

import cz.poptavka.sample.client.home.HomeEventBus;
import cz.poptavka.sample.client.home.creation.FormUserRegistrationPresenter.FormRegistrationInterface;
import cz.poptavka.sample.client.main.common.StatusIconLabel;
import cz.poptavka.sample.client.main.common.StatusIconLabel.State;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import cz.poptavka.sample.client.main.common.creation.FormDemandAdvPresenter.FormDemandAdvViewInterface;
import cz.poptavka.sample.client.main.common.creation.FormDemandBasicPresenter.FormDemandBasicInterface;
import cz.poptavka.sample.client.main.common.creation.ProvidesValidate;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

@Presenter(view = DemandCreationView.class)
public class DemandCreationPresenter
    extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, HomeEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DemandCreationPresenter");

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public interface CreationViewInterface extends LazyView {

        StackLayoutPanel getMainPanel();

        Widget getWidgetView();

        void toggleLoginRegistration();

        StatusIconLabel getStatusLabel(int order);

        SimplePanel getHolderPanel(int order);

        HasClickHandlers getCreateDemandButton();
    }

    public void bindView() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (view.getMainPanel().getVisibleIndex() < eventItem) {
                    boolean result = canContinue(eventItem);
                    if (!result) {
                        // TODO change to global status changer eventBus call
                        event.cancel();
                    } else {
                        // TODO change to global status changer eventBus call
                        view.getStatusLabel(eventItem).setPassedSmall(result);
                    }
                }
            }
        });
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
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
    public void onAtCreateDemand() {
        LOGGER.info("  INIT DemandCreation Widget");
        view.getMainPanel().showWidget(0);
        eventBus.initDemandBasicForm(view.getHolderPanel(BASIC));
        eventBus.initCategoryWidget(view.getHolderPanel(CATEGORY));

        eventBus.setBodyWidget(view.getWidgetView());

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.initLocalityWidget(view.getHolderPanel(LOCALITY));
                eventBus.initDemandAdvForm(view.getHolderPanel(ADVANCED));
                eventBus.initLoginForm(view.getHolderPanel(LOGIN));
            }
        });
    }

    private void registerNewCient() {
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterClient());
        // ClientDetail instance
        FormRegistrationInterface registerWidget = (FormRegistrationInterface) view.getHolderPanel(LOGIN).getWidget();
        UserDetail newClient = registerWidget.getNewClient();
        eventBus.registerNewClient(newClient);

    }

    /**
     * Called from HomeHandler after successful login/registration.
     *
     * @param id id of newly created client/id of logged user
     */
    public void onPrepareNewDemandForNewClient(UserDetail client) {
        eventBus.loadingShow(MSGS.progressGettingDemandData());

        FormDemandBasicInterface basicValues =
            (FormDemandBasicInterface) view.getHolderPanel(BASIC).getWidget();
        CategorySelectorInterface categoryValues =
            (CategorySelectorInterface) view.getHolderPanel(CATEGORY).getWidget();
        LocalitySelectorInterface localityValues =
            (LocalitySelectorInterface) view.getHolderPanel(LOCALITY).getWidget();
        FormDemandAdvViewInterface advValues =
            (FormDemandAdvViewInterface) view.getHolderPanel(ADVANCED).getWidget();

        DemandDetail demand = new DemandDetail();
        demand.setBasicInfo(basicValues.getValues());
        demand.setCategories(categoryValues.getSelectedCategoryCodes());
        demand.setLocalities(localityValues.getSelectedLocalityCodes());
        demand.setAdvInfo(advValues.getValues());

        eventBus.createDemand(demand, client.getClientId());
        eventBus.loadingShow(MSGS.progressCreatingDemand());

    }

    /** Done automatically in step five, when option: register new client is selected. **/
    //DO NOT EDIT
    public void onToggleLoginRegistration() {
        view.toggleLoginRegistration();
    }

    /** showing error after login failure. **/
    public void onLoginError() {
        // TODO change to global status changer eventBus call
        view.getStatusLabel(LOGIN).setStyleState(StyleResource.INSTANCE.common().errorMessage(), State.ERROR_16);
        view.getStatusLabel(LOGIN).setTexts(MSGS.wrongLoginMessage(), MSGS.wrongLoginDescription());
    }

    private static final int BASIC = 1;
    private static final int CATEGORY = 2;
    private static final int LOCALITY = 3;
    private static final int ADVANCED = 4;
    private static final int LOGIN = 5;

    private boolean canContinue(int step) {
        ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
        LOGGER.fine(widget.getClass().getName());
        return widget.isValid();
    }
}
