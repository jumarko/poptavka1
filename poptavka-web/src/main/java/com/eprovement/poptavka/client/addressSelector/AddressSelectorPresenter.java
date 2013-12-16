/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.addressSelector;

import com.eprovement.poptavka.client.addressSelector.others.AddressSelectorSuggestDisplay;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.service.demand.AddressSelectorRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.selectors.addressSelector.AddressSuggestionDetail;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

/**
 * Address selector presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AddressSelectorView.class)
public class AddressSelectorPresenter
        extends LazyPresenter<AddressSelectorPresenter.AddressSelectorInterface, AddressSelectorEventBus> {

    /**************************************************************************/
    /* Inject RPC service needed for CitySuggestOracle initialization         */
    /**************************************************************************/
    @Inject
    private AddressSelectorRPCServiceAsync locService;

    public AddressSelectorRPCServiceAsync getAddressService() {
        return locService;
    }

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface AddressSelectorInterface extends LazyView {

        //Setters
        void setAddressForEditing(AddressDetail address);

        //Getters
        SuggestBox getCitySuggestBox();

        ValidationMonitor getCityMonitor();

        ValidationMonitor getStreetMonitor();

        ValidationMonitor getZipcodeMonitor();

        TextBox getStreetMonitorBox();

        TextBox getZipcodeMonitorBox();

        boolean isValid();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private AddressSuggestionDetail addressSuggestion;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        //nothing by default
    }

    public void onForward() {
        //nothing by default
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    /**
     * Binds handlers for suggestionBox onFocus and onSelection.
     */
    @Override
    public void bindView() {
        /** FOCUS. **/
        view.getCitySuggestBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                AddressSelectorSuggestDisplay display
                    = ((AddressSelectorSuggestDisplay) view.getCitySuggestBox().getSuggestionDisplay());
                display.setLoadingPopupPosition(view.getCitySuggestBox());
                //show actual suggest list and remove error style if any
                if (view.getCitySuggestBox().getText().isEmpty()) {
                    display.showShortCitiesInfo(Constants.MIN_CHARS_TO_SEARCH);
                }
                view.getCitySuggestBox().showSuggestionList();
            }
        }, FocusEvent.getType());
        /** SELECTION. **/
        view.getCitySuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<Suggestion> event) {
                addressSuggestion = (AddressSuggestionDetail) event.getSelectedItem();
            }
        });
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Initialize AddressSelector.
     * Sets widget view to given holder panel and clear view's components.
     * @param embedWidget holder for widget view
     */
    public void onInitAddressSelector(SimplePanel embedWidget) {
        embedWidget.setWidget(view.getWidgetView());
        //presenter is not multiple due to widget recycling (Google I-O 2010 - GWT testing best practices)
        //Anyway, it can be multiple, but due to keep presenter references inside AddressSelector module
        //due to codesplitting, instances must be generated from eventbus by generate anotation,
        //not by eventBus.addHandler from refereced module. This require some kind of instance manager.
        //See CatLocSelectorInstanceManager.
        view.getCityMonitor().setValue("");
        view.getZipcodeMonitor().setValue("");
        view.getStreetMonitor().setValue("");
        view.getCityMonitor().resetValidation();
        view.getZipcodeMonitor().resetValidation();
        view.getStreetMonitor().resetValidation();
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Fill given list with selected addresses only if those addresses are valid.
     * @param addresses
     */
    public void onFillAddresses(List<AddressDetail> addresses) {
        if (addresses == null) {
            addresses = new ArrayList<AddressDetail>();
        } else {
            addresses.clear();
        }
        //In future more addresses will be supported, therefore using already list of address
        addresses.add(createAddress());
    }

    /**
     * Sets list of addresses.
     * <b><i>Note:</i></b>
     * Only one address is supported so far.
     * @param addresses list of addresses
     */
    public void onSetAddresses(List<AddressDetail> addresses) {
        if (addresses != null && !addresses.isEmpty()) {
            final AddressDetail address = addresses.get(0);
            addressSuggestion = new AddressSuggestionDetail();
            addressSuggestion.setStateName(address.getRegion());
            addressSuggestion.setCityId(address.getCityId());
            addressSuggestion.setCityName(address.getCity());

            view.setAddressForEditing(address);
        }
    }

    /**
     * Validate view's compontents.
     * Stores result in given variable.
     * @param result where validation result is stored
     */
    public void onIsAddressSelectorValid(boolean result) {
        result = view.isValid();
    }

    /**
     * Get popup where choices are shown.
     * @return the AddressSelectorSuggestDisplay
     */
    public AddressSelectorSuggestDisplay getCitySuggestionPopup() {
        return ((AddressSelectorSuggestDisplay) view.getCitySuggestBox().getSuggestionDisplay());
    }

    /**
     * Creates and returns AddressDetail object.
     * @return created address detail object
     */
    private AddressDetail createAddress() {
        AddressDetail address = new AddressDetail();
        address.setCountry(Constants.COUNTRY);
        address.setRegion(addressSuggestion.getStateName());
        address.setCity(addressSuggestion.getCityName());
        address.setCityId(addressSuggestion.getCityId());
        address.setDistrict(Constants.DISTRICT);
        address.setStreet((String) view.getStreetMonitor().getValue());
        address.setZipCode((String) view.getZipcodeMonitor().getValue());

        return address;
    }
}
