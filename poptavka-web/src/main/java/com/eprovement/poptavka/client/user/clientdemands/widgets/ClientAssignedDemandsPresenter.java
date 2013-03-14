/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.detail.FeedbackPopupView;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;

@Presenter(view = ClientAssignedDemandsView.class, multiple = true)
public class ClientAssignedDemandsPresenter extends LazyPresenter<
        ClientAssignedDemandsPresenter.ClientAssignedDemandsLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientAssignedDemandsLayoutInterface extends LazyView, IsWidget {

        UniversalTableGrid getDataGrid();

        SimplePager getPager();

        //Action box actions
        DropdownButton getActionBox();

        NavLink getActionRead();

        NavLink getActionUnread();

        NavLink getActionStar();

        NavLink getActionUnstar();

        Button getCloseBtn();

        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Constants. **/
    private static final int THANK_YOU_POPUP_DISPLAY_TIME = 3000;
    /** Class Attributes. **/
    private DetailsWrapperPresenter detailSection;
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater;
    private IUniversalDetail selectedObject;
    private FeedbackPopupView ratePopup;
    private long selectedClientAssignedDemandId = -1;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Table handlers
        addTableSelectionModelClickHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Listbox actions
        addActionBoxChoiceHandlers();
        // buttons handlers
        addCloseButtonHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientAssignedDemands(SearchModuleDataHolder filter) {
        //Must be present here. Loading data rely on this atrtibute
        Storage.setCurrentlyLoadedView(Constants.CLIENT_ASSIGNED_DEMANDS);

        initWidget(filter);
    }

    public void onInitClientClosedDemands(SearchModuleDataHolder filter) {
        //Must be present here. Loading data rely on this atrtibute
        Storage.setCurrentlyLoadedView(Constants.CLIENT_CLOSED_DEMANDS);

        initWidget(filter);
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        if (this.detailSection == null) {
            this.detailSection = detailSection;
            this.detailSection.initDetailWrapper(view.getDataGrid(), view.getWrapperPanel());
            if (selectedObject != null) {
                this.detailSection.initDetails(
                        selectedObject.getDemandId(),
                        selectedObject.getSupplierId(),
                        selectedObject.getThreadRootId());
            }
        }
    }

    public void onResponseFeedback() {
        ratePopup.getPopupThankYou().show();
        Timer togglebuttonTimer = new Timer() {
            public void run() {
                ratePopup.getPopupThankYou().hide();
            }
        };
        togglebuttonTimer.schedule(THANK_YOU_POPUP_DISPLAY_TIME);
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientAssignedDemands(List<IUniversalDetail> data) {
        GWT.log("++ onResponseClientsAssignedDemands");

        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), data);

        if (selectedClientAssignedDemandId != -1) {
            eventBus.getClientAssignedDemand(selectedClientAssignedDemandId);
        }
    }

    public void onSelectClientAssignedDemand(ClientOfferedDemandOffersDetail detail) {
//        view.getDataGrid().getSelectionModel().setSelected(detail, true);
        eventBus.setHistoryStoredForNextOne(false); //don't create token
        //nestaci oznacit v modeli, pretoze ten je viazany na checkboxy a akcie, musim
        //nejak vytvorit event na upadatefieldoch
        //Dolezite je len detail, ostatne atributy sa nepouzivaju
        textFieldUpdater.update(-1, detail, null);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void initWidget(SearchModuleDataHolder filter) {
        eventBus.setUpSearchBar(new Label("Client's closed projects attibure's selector will be here."));
        searchDataHolder = filter;
        eventBus.createTokenForHistory();

        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
        //init wrapper widget
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    // Field Updaters
    public void addCheckHeaderUpdater() {
        view.getDataGrid().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<IUniversalDetail> rows = view.getDataGrid().getVisibleItems();
                for (IUniversalDetail row : rows) {
                    ((MultiSelectionModel) view.getDataGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getDataGrid().getStarColumn().setFieldUpdater(new FieldUpdater<IUniversalDetail, Boolean>() {
            @Override
            public void update(int index, IUniversalDetail object, Boolean value) {
                object.setIsStarred(!value);
                view.getDataGrid().redraw();
                Long[] item = new Long[]{object.getUserMessageId()};
                eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
            }
        });
    }

    public void addTableSelectionModelClickHandler() {
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //set actionBox visibility
                if (view.getDataGrid().getSelectedUserMessageIds().size() > 0) {
                    view.getActionBox().setVisible(true);
                } else {
                    view.getActionBox().setVisible(false);
                }
                //init details
                if (view.getDataGrid().getSelectedUserMessageIds().size() > 1) {
                    view.getCloseBtn().setVisible(false);
                    detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.NONE);
                } else {
                    view.getCloseBtn().setVisible(true);
                    IUniversalDetail selected = view.getDataGrid().getSelectedObjects().get(0);
                    selectedObject = selected;
                    if (detailSection == null) {
                        eventBus.requestDetailWrapperPresenter();
                    } else {
                        detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.BLOCK);
                        detailSection.initDetails(
                                selected.getDemandId(),
                                selected.getSupplierId(),
                                selected.getThreadRootId());
                    }
                }
            }
        });
    }

    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<ClientOfferedDemandOffersDetail, String>() {
            @Override
            public void update(int index, ClientOfferedDemandOffersDetail object, String value) {
                object.setIsRead(true);
                MultiSelectionModel selectionModel = view.getDataGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
            }
        };
        view.getDataGrid().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getFinnishDateColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Widget action handlers
    private void addActionBoxChoiceHandlers() {
        view.getActionRead().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestReadStatusUpdate(view.getDataGrid().getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnread().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestReadStatusUpdate(view.getDataGrid().getSelectedUserMessageIds(), false);
            }
        });
        view.getActionStar().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestStarStatusUpdate(view.getDataGrid().getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnstar().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestStarStatusUpdate(view.getDataGrid().getSelectedUserMessageIds(), false);
            }
        });
    }

    private void addCloseButtonHandler() {
        view.getCloseBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ratePopup = new FeedbackPopupView(FeedbackPopupView.SUPPLIER);
                ratePopup.setSupplierName(selectedObject.getSupplierName());
                ratePopup.getRateBtn().addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        eventBus.requestCloseAndRateSupplier(selectedObject.getDemandId(), selectedObject.getOfferId(),
                                Integer.valueOf(ratePopup.getRating()), ratePopup.getComment());
                    }
                });
            }
        });
    }
}
