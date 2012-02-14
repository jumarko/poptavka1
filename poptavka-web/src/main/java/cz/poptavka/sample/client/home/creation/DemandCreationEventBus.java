/*
 * DemandCreationEventBus servers all events for module DemandCreationModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> WF zatial chyba
 */
package cz.poptavka.sample.client.home.creation;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

/**
 *
 * @author ivan.vlcek
 */
@Events(startView = DemandCreationView.class, module = DemandCreationModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface DemandCreationEventBus extends EventBus {

    @Start
    @Event(handlers = DemandCreationPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = DemandCreationPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events. */
    /**
     * The only entry point to this module due to code-splitting and exclusive fragment.
     */
    @Event(handlers = DemandCreationPresenter.class)
    void goToCreateDemand(String location);

    @Event(forwardToParent = true)
    void setHomeBodyHolderWidget(IsWidget body);

    @Event(forwardToParent = true)
    void setUserBodyHolderWidget(Widget body);

    /**************************************************************************/
    /* Parent events. */
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget);

    /**************************************************************************/
    /* Business events. */
    /* Business events handled by Presenters. */
    @Event(handlers = DemandCreationPresenter.class)
    void initDemandBasicForm(SimplePanel holderWidget);

    @Event(handlers = DemandCreationPresenter.class)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(handlers = DemandCreationPresenter.class)
    void toggleLoginRegistration();

    @Event(handlers = FormUserRegistrationPresenter.class)
    void initRegistrationForm(SimplePanel holderWidget);

    @Event(handlers = FormLoginPresenter.class)
    void initLoginForm(SimplePanel holderWidget);

    @Event(handlers = DemandCreationPresenter.class)
    void loginError();

    @Event(handlers = DemandCreationPresenter.class)
    void prepareNewDemandForNewClient(UserDetail client);

//    @Event(handlers = SupplierCreationPresenter.class)
//    void initServiceForm(SimplePanel serviceHolder);
//
//    @Event(handlers = SupplierCreationPresenter.class)
//    void initSupplierForm(SimplePanel supplierInfoHolder);
    @Event(handlers = FormUserRegistrationPresenter.class)
    void checkFreeEmailResponse(Boolean result);

    /* Business events handled by Handlers. */
    @Event(handlers = DemandCreationHandler.class)
    void registerNewClient(UserDetail newClient);

    // TODO praso - FullDemandDetail will obviosuly fell inside the left-over fragment
    // if it is used in another module. And I think it is bacause I saw it in left-over
    @Event(handlers = DemandCreationHandler.class)
    void createDemand(FullDemandDetail detail, Long clientId);

    // TODO praso - check if this one is used in suppliserCreationModule. we shouln't have duplicates
    @Event(handlers = DemandCreationHandler.class)
    void verifyExistingClient(UserDetail client);
//    @Event(handlers = SupplierCreationHandler.class)
//    void checkFreeEmail(String value);

    @Event(handlers = DemandCreationHandler.class)
    void checkFreeEmail(String value);
}