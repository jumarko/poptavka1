package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.session.Storage;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetailSuggestion;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.ValidationMessages;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.inject.Inject;

@Presenter(view = AddressSelectorView.class, multiple = true)
public class AddressSelectorPresenter
        extends LazyPresenter<AddressSelectorPresenter.AddressSelectorInterface, RootEventBus> {

    @Inject
    private LocalityRPCServiceAsync locService;

    public LocalityRPCServiceAsync getLocalityService() {
        return locService;
    }
    //riesit inak, je jedno ci inject tu alebo v handleri? - nieco na sposob metody: provideLocalityService()
    //a responseLocalityService -> nasetovalo atribut localityService v prezenteri
    //-- len aby nemuselo byt Inject, ak to nie je vporiadku

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface AddressSelectorInterface extends LazyView {

        //Setters
        void setState(String state);

        void setCity(String city);

        //Getters
        @Ignore
        SuggestBox getCitySuggestBox();

        @Ignore
        Label getCityErrorLabel();

        boolean isValid();

        AddressDetail createAddress();

        void eraseAddressBoxes();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    public static final ValidationMessages MSGSV = GWT.create(ValidationMessages.class);
    //in case of removeing letters in suggestbox, popup is closed at each time -> dont't call
//    private String regionSuggestSelected = "";
    private String citySuggestSelected = null;

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
//        addRegionHandlers();
        addCityHandlers();
    }

    /**************************************************************************/
    /* BIND HELPER METHODS                                                    */
    /**************************************************************************/
//    private void addRegionHandlers() {
//        /** FOCUS. **/
//        view.getRegionSuggestBox().addDomHandler(new FocusHandler() {
//            @Override
//            public void onFocus(FocusEvent event) {
//                view.getRegionSuggestBox().showSuggestionList();
//            }
//        }, FocusEvent.getType());
//        /** CLOSE POPUP. **/
//        MySuggestDisplay regionPopup = ((MySuggestDisplay) view.getRegionSuggestBox().getSuggestionDisplay());
//        regionPopup.getPopupPanel().addHandler(new CloseHandler<PopupPanel>() {
//            @Override
//            public void onClose(CloseEvent event) {
//                if (!regionSuggestSelected.isEmpty()
//                        && !regionSuggestSelected.equals(view.getRegionSuggestBox().getText())) {
//                    view.getRegionSuggestBox().setStyleName(Storage.RSCS.common().errorField());
//                    view.getRegionErrorLabel().setText(MSGSV.regionNotMatch());
//                }
//            }
//        }, CloseEvent.getType());
//        /** SELECTION. **/
//        view.getRegionSuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//            @Override
//            public void onSelection(SelectionEvent<Suggestion> event) {
//                if (!view.getRegionSuggestBox().getText().isEmpty()) {
//                    regionSuggestSelected = event.getSelectedItem().getDisplayString();
//                    view.eraseAddressBoxes(AddressSelectorView.REGION);
//                    //get Oracle
//                    CitySuggestOracle cityOracle =
//                            (CitySuggestOracle) view.getCitySuggestBox().getSuggestOracle();
//                    //get Suggestion
//                    LocalityDetailSuggestion suggestion =
//                            (LocalityDetailSuggestion) event.getSelectedItem();
//                    cityOracle.setLocalityCodeForNextSuggestions(suggestion.getLocalityDetail().getCode());
////                    eventBus.getChildLocalities(LocalityType.CITY, suggestion.getLocalityDetail().getCode(), null);
//                    view.getRegionSuggestBox().setStyleName(Storage.RSCS.common().emptyStyle());
//                    view.getRegionErrorLabel().setText("");
//                }
//            }
//        });
//    }
    public MySuggestDisplay getCitySuggestionPopup() {
        return ((MySuggestDisplay) view.getCitySuggestBox().getSuggestionDisplay());
    }

    private void addCityHandlers() {
        /** FOCUS. **/
        view.getCitySuggestBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                //show actual suggest list and remove error style if any
                view.getCitySuggestBox().showSuggestionList();
                view.getCitySuggestBox().setStyleName(Storage.RSCS.common().emptyStyle());
                view.getCityErrorLabel().setText("");
            }
        }, FocusEvent.getType());
        /** VALUE CHANGE. **/
        view.getCitySuggestBox().addHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                //if suggestbox content changed, reset flag to force user through validation
                //to select some locality again
                citySuggestSelected = null;
            }
        }, ValueChangeEvent.getType());
        /** CLOSE POPUP. **/
        getCitySuggestionPopup().getPopupPanel().addHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent event) {
                //validate suggest box if suggest list popup is closed
                if (citySuggestSelected == null
                        || !citySuggestSelected.equals(view.getCitySuggestBox().getText())) {
                    view.getCitySuggestBox().setStyleName(Storage.RSCS.common().errorField());
                    view.getCityErrorLabel().setText(MSGSV.cityNotMatch());
                }
            }
        }, CloseEvent.getType());
        /** SELECTION. **/
        view.getCitySuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<Suggestion> event) {
                //set locality after selecting item from suggest list popup
                if (!view.getCitySuggestBox().getText().isEmpty()) {
                    view.eraseAddressBoxes();
                    LocalityDetailSuggestion suggestion = (LocalityDetailSuggestion) event.getSelectedItem();
                    citySuggestSelected = suggestion.getDisplayString();
                    view.setState(suggestion.getStateName());
                    view.setCity(suggestion.getCityName());
                    view.getCitySuggestBox().setStyleName(Storage.RSCS.common().emptyStyle());
                    view.getCityErrorLabel().setText("");
                }
            }
        });
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initAddressWidget(SimplePanel embedWidget) {
        embedWidget.setWidget(view.getWidgetView());
    }
}
