package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.LocalitySuggestionDetail;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

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

        void setChangeHandler(ChangeHandler changeHandler);

        void setChangeMonitorsEnabled(boolean enabled);

        //Getters
        SuggestBox getCitySuggestBox();

        ChangeMonitor getCityMonitor();

        ChangeMonitor getStreetMonitor();

        ChangeMonitor getZipcodeMonitor();

        boolean isValid();

        boolean isAddressChanged();

        AddressDetail createAddress();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //history of changes
    private ArrayList<ChangeDetail> updatedFields = new ArrayList<ChangeDetail>();

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        addCitySuggestBoxHandler();
        addMonitorsHandlers();
    }

    /**************************************************************************/
    /* BIND HELPER METHODS                                                    */
    /**************************************************************************/
    public MySuggestDisplay getCitySuggestionPopup() {
        return ((MySuggestDisplay) view.getCitySuggestBox().getSuggestionDisplay());
    }

    private void addCitySuggestBoxHandler() {
        /** FOCUS. **/
        view.getCitySuggestBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                MySuggestDisplay display = ((MySuggestDisplay) view.getCitySuggestBox().getSuggestionDisplay());
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
                //set locality after selecting item from suggest list popup
                if (!view.getCitySuggestBox().getText().isEmpty()) {
                    LocalitySuggestionDetail suggestion = (LocalitySuggestionDetail) event.getSelectedItem();
                    view.setState(suggestion.getStateName());
                    view.setCity(suggestion.getCityName());
                    view.setCityId(suggestion.getCityId());
                }
            }
        });
    }

    private void addMonitorsHandlers() {
        view.getCityMonitor().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                view.getZipcodeMonitor().setValue("");
                view.getStreetMonitor().setValue("");
                fireEvent(view.getZipcodeMonitor());
                fireEvent(view.getStreetMonitor());
            }
        });
        view.getZipcodeMonitor().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                view.getStreetMonitor().setValue("");
                fireEvent(view.getStreetMonitor());
            }
        });
        view.setChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                ChangeMonitor source = (ChangeMonitor) event.getSource();
                source.getChangeDetail().setValue(source.getValue());
                if (source.isModified()) {
                    //if contains already - remove before adding new
                    if (updatedFields.contains(source.getChangeDetail())) {
                        updatedFields.remove(source.getChangeDetail());
                    }
                    updatedFields.add(source.getChangeDetail());
                } else {
                    updatedFields.remove(source.getChangeDetail());
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

    /**************************************************************************/
    /* HELPER METHODS                                                         */
    /**************************************************************************/
    private void fireEvent(ChangeMonitor changeMonitor) {
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), changeMonitor);
    }
}
