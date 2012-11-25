package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.session.Storage;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.ValidationMessages;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import java.util.List;

@Presenter(view = AddressSelectorView.class, multiple = true)
public class AddressSelectorPresenter
        extends LazyPresenter<AddressSelectorPresenter.AddressSelectorInterface, RootEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface AddressSelectorInterface extends LazyView {

        //Setters
        void setSuggestBoxOracleData(LocalityType localityType, List<LocalityDetail> data);

        //Getters
        @Ignore SuggestBox getCountrySuggestBox();

        @Ignore SuggestBox getRegionSuggestBox();

        @Ignore SuggestBox getCitySuggestBox();

//        ListBox getDistrict();

        @Ignore Label getCountryErrorLabel();

        @Ignore Label getRegionErrorLabel();

        @Ignore Label getCityErrorLabel();

        boolean isValid();

        AddressDetail createAddress();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    public static final ValidationMessages MSGSV = GWT.create(ValidationMessages.class);
    private boolean regionMatch = false;

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        addCountryHandlers();
        addRegionHandlers();
        addCityHandlers();
    }

    /**************************************************************************/
    /* BIND HELPER METHODS                                                    */
    /**************************************************************************/
    private void addCountryHandlers() {
        /** FOCUS. **/
        view.getCountrySuggestBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                regionMatch = false;
                view.getCountrySuggestBox().removeStyleName(Storage.RSCS.common().errorField());
                view.getCountryErrorLabel().setText("");
            }
        }, FocusEvent.getType());
        /** BLUR. **/
        view.getCountrySuggestBox().addDomHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (!regionMatch) {
                    view.getCountrySuggestBox().setStyleName(Storage.RSCS.common().errorField());
                    view.getCountryErrorLabel().setText(MSGSV.countryNotMatch());
                }
            }
        }, BlurEvent.getType());
        /** SELECTION. **/
        view.getCountrySuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<Suggestion> event) {
                if (!view.getCountrySuggestBox().getText().isEmpty()) {
                    regionMatch = true;
                    LocalityDetailMultiWordSuggestion suggestion =
                            (LocalityDetailMultiWordSuggestion) event.getSelectedItem();
                    eventBus.getChildLocalities(LocalityType.REGION, suggestion.getLocalityDetail().getCode(), null);
                }
            }
        });
    }

    private void addRegionHandlers() {
        /** FOCUS. **/
        view.getRegionSuggestBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                regionMatch = false;
                view.getRegionSuggestBox().removeStyleName(Storage.RSCS.common().errorField());
                view.getRegionErrorLabel().setText("");
            }
        }, FocusEvent.getType());
        /** BLUR. **/
        view.getRegionSuggestBox().addDomHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (!regionMatch) {
                    view.getRegionSuggestBox().setStyleName(Storage.RSCS.common().errorField());
                    view.getRegionErrorLabel().setText(MSGSV.regionNotMatch());
                }
            }
        }, BlurEvent.getType());
        /** SELECTION. **/
        view.getRegionSuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<Suggestion> event) {
                if (!view.getRegionSuggestBox().getText().isEmpty()) {
                    regionMatch = true;
                    LocalityDetailMultiWordSuggestion suggestion =
                            (LocalityDetailMultiWordSuggestion) event.getSelectedItem();
                    eventBus.getChildLocalities(LocalityType.CITY, suggestion.getLocalityDetail().getCode(), null);
                }
            }
        });
    }

    private void addCityHandlers() {
        /** FOCUS. **/
        view.getCitySuggestBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                regionMatch = false;
                view.getCitySuggestBox().removeStyleName(Storage.RSCS.common().errorField());
                view.getCityErrorLabel().setText("");
            }
        }, FocusEvent.getType());
        /** BLUR. **/
        view.getCitySuggestBox().addDomHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (!regionMatch) {
                    view.getCitySuggestBox().setStyleName(Storage.RSCS.common().errorField());
                    view.getCityErrorLabel().setText(MSGSV.cityNotMatch());
                }
            }
        }, BlurEvent.getType());
        /** SELECTION. **/
//        view.getCity().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//            @Override
//            public void onSelection(SelectionEvent<Suggestion> event) {
//                if (!view.getCity().getText().isEmpty()) {
//                    regionMatch = true;
//                    LocalityDetailMultiWordSuggestion suggestion =
//                            (LocalityDetailMultiWordSuggestion) event.getSelectedItem();
//                    eventBus.getChildLocalities(LocalityType.DISTRICT,
//        suggestion.getLocalityDetail().getCode(), null);
//                }
//            }
//        });
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initAddressWidget(SimplePanel embedWidget) {
        eventBus.getLocalities(LocalityType.COUNTRY, null);
        embedWidget.setWidget(view.getWidgetView());
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    public void onSetLocalityData(LocalityType localityType, List<LocalityDetail> list) {
        view.setSuggestBoxOracleData(localityType, list);
    }
}
