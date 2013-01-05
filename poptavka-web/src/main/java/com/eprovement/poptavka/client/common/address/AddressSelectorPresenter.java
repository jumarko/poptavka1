package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.LocalitySuggestionDetail;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.TextBox;
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

        void setCityId(Long cityId);

        void eraseAddressBoxes();

        //Getters
        SuggestBox getCitySuggestBox();

        TextBox getStreetTextBox();

        TextBox getZipCodeTextBox();

        Label getCityErrorLabel();

        boolean isValid();

        AddressDetail createAddress();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    public static final ValidationMessages MSGSV = GWT.create(ValidationMessages.class);
    //in case of removeing letters in suggestbox, popup is closed at each time -> dont't call
    private String citySuggestSelected = null;

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        addCityHandlers();
    }

    /**************************************************************************/
    /* BIND HELPER METHODS                                                    */
    /**************************************************************************/
    public MySuggestDisplay getCitySuggestionPopup() {
        return ((MySuggestDisplay) view.getCitySuggestBox().getSuggestionDisplay());
    }

    private void addCityHandlers() {
        /** FOCUS. **/
        view.getCitySuggestBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                citySuggestSelected = view.getCitySuggestBox().getText();
                MySuggestDisplay display = ((MySuggestDisplay) view.getCitySuggestBox().getSuggestionDisplay());
                display.setLoadingPopupPosition(view.getCitySuggestBox());
                //show actual suggest list and remove error style if any
                if (view.getCitySuggestBox().getText().isEmpty()) {
                    display.showShortCitiesInfo(Constants.MIN_CHARS_TO_SEARCH);
                }
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
                    LocalitySuggestionDetail suggestion = (LocalitySuggestionDetail) event.getSelectedItem();
                    citySuggestSelected = suggestion.getDisplayString();
                    view.setState(suggestion.getStateName());
                    view.setCity(suggestion.getCityName());
                    view.setCityId(suggestion.getCityId());
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
