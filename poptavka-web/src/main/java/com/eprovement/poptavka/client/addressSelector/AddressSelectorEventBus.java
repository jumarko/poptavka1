/*
 * HomeDemandsEventBus servers all events for module HomeDemandsModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis Poptaviek
 */
package com.eprovement.poptavka.client.addressSelector;

import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

/**
 * @author Martin Slavkovsky
 */
@Events(startPresenter = AddressSelectorPresenter.class, module = AddressSelectorModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface AddressSelectorEventBus extends EventBusWithLookup, InfoWidgetsGateway {

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = AddressSelectorPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = AddressSelectorPresenter.class)
    void forward();

    /**************************************************************************/
    /* Business events handled by AddressSelectorPresenter                    */
    /**************************************************************************/
    @Event(handlers = AddressSelectorPresenter.class)
    void initAddressSelector(SimplePanel embedToWidget);

    @Event(handlers = AddressSelectorPresenter.class)
    void fillAddresses(List<AddressDetail> addresses);

    @Event(handlers = AddressSelectorPresenter.class)
    void setAddresses(List<AddressDetail> addresses);
}
