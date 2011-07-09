package cz.poptavka.sample.client.home.suppliers;


import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

/**
 *
 * @author Martin Slavkovsky
 *
 */
@Events(startView = DisplaySuppliersView.class, module = DisplaySuppliersModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface DisplaySuppliersEventBus extends EventBus {

    /**
     * Initialize demands presenter.
     */
    @Event(handlers = DisplaySuppliersPresenter.class)
    void atSuppliers();

    @Event(forwardToParent = true)
    void setBodyWidget(Widget content);
}
