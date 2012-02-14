/*
 * SupplierCreationEventBus servers all events for module SupplierCreationModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> WF zatial chyba
 */
package cz.poptavka.sample.client.home.supplier;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter;
import cz.poptavka.sample.shared.domain.UserDetail;

/**
 *
 * @author ivan.vlcek
 */
@Events(startView = SupplierCreationView.class, module = SupplierCreationModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface SupplierCreationEventBus extends EventBus {

    @Start
    @Event(handlers = SupplierCreationPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = SupplierCreationPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events. */
    /**
     * The only entry point to this module due to code-splitting and exclusive fragment.
     */
    @Event(handlers = SupplierCreationPresenter.class)
    void goToCreateSupplier(String location);

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
    @Event(handlers = SupplierCreationPresenter.class)
    void initServiceForm(SimplePanel serviceHolder);

    @Event(handlers = SupplierCreationPresenter.class)
    void initSupplierForm(SimplePanel supplierInfoHolder);

    @Event(handlers = SupplierInfoPresenter.class)
    void checkFreeEmailResponse(Boolean result);
    /* Business events handled by Handlers. */

    @Event(handlers = SupplierCreationHandler.class)
    void registerSupplier(UserDetail newSupplier);

    @Event(handlers = SupplierCreationHandler.class)
    void checkFreeEmail(String value);
}