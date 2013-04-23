/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.actionBox.ActionBoxView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.smallPopups.ThankYouPopup;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;

@Presenter(view = ClientOffersView.class, multiple = true)
public class ClientOffersPresenter
        extends LazyPresenter<ClientOffersPresenter.ClientOffersLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientOffersLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalAsyncGrid<ClientDemandDetail> getDemandGrid();

        UniversalTableGrid getOfferGrid();

        //Pager
        SimplePager getDemandPager();

        SimplePager getOfferPager();

        //Buttons
        Button getBackBtn();

        Button getAcceptBtn();

        //Other
        SimplePanel getDetailPanel();

        SimplePanel getActionBox();

        HorizontalPanel getOfferToolBar();

        //Others
        IsWidget getWidgetView();

        //Setter
        void setDemandTableVisible(boolean visible);

        void setOfferTableVisible(boolean visible);

        void setOfferTitleLabel(String text);
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private DetailsWrapperPresenter detailSection;
    private SearchModuleDataHolder searchDataHolder;
    private ClientDemandDetail selectedDemandObject;
    private IUniversalDetail selectedOfferedDemandOfferObject;
    private FieldUpdater textFieldUpdater;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Selection Handlers
        addDemandTableSelectionHandler();
        addOfferGridSelectionModelHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Buttons Actions
        addBackButtonHandler();
        addAcceptOfferButtonHandler();
        // Row styles
        addDemandGridRowStyles();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientOffers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
        eventBus.clientDemandsMenuStyleChange(Constants.CLIENT_OFFERED_DEMANDS);
        eventBus.initActionBox(view.getActionBox(), view.getOfferGrid());

        eventBus.setUpSearchBar(new Label("Client's contests attibure's selector will be here."));
        searchDataHolder = filter;
        eventBus.createTokenForHistory();

        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
        //request data to demand(parent) table
        if (view.getDemandGrid().isVisible()) {
            view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        } else {
            backBtnClickHandlerInner();
        }
    }

    /**************************************************************************/
    /* Details Wrapper                                                        */
    /**************************************************************************/
    /**
     * Response method to requesting details wrapper instance.
     * Some additional actions can be added here.
     * @param detailSection Details wrapper instance.
     */
    public void onResponseDetailWrapperPresenter(final DetailsWrapperPresenter detailSection) {
        if (detailSection != null) {
            detailSection.initDetailWrapper(view.getOfferGrid(), view.getDetailPanel());

            this.detailSection = detailSection;

            if (selectedOfferedDemandOfferObject != null) {
                initDetailSection(selectedOfferedDemandOfferObject);
            } else if (selectedDemandObject != null) {
                initDetailSection(selectedDemandObject);
            }
        }
    }

    /**
     * Initialize demand tab in Details sections.
     * If details wrapper instance doesn't exist yet, create it and in response of
     * creation initialize demand tab.
     * If instance already exist, initialize and show demand tab immediately.
     *
     * @param demandId
     */
    private void initDetailSection(ClientDemandDetail demandDetail) {
        if (detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        } else {
            view.getDetailPanel().setVisible(true);
            detailSection.initDetails(demandDetail.getDemandId());
        }
    }

    /**
     * Initialize demand, supplier and conversation tabs in Details sections.
     * If details wrapper instance doesn't exist yet, create it and in response of
     * creation initialize demand, supplier, conversation tabs.
     * If instance already exist, initialize and show tabs immediately.
     *
     * @param demandId
     */
    private void initDetailSection(IUniversalDetail conversationDetail) {
        if (detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        } else {
            view.getDetailPanel().setVisible(true);
            detailSection.initDetails(
                    conversationDetail.getDemandId(),
                    conversationDetail.getSupplierId(),
                    conversationDetail.getThreadRootId(),
                    conversationDetail.getSenderId());
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientOfferedDemands(List<ClientDemandDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemands");

        view.getDemandGrid().getDataProvider().updateRowData(
                view.getDemandGrid().getStart(), data);
    }

    public void onDisplayClientOfferedDemandOffers(List<IUniversalDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemandOffers");

        if (!data.isEmpty()) {
            view.setDemandTableVisible(false);
            view.setOfferTableVisible(true);
            view.getOfferPager().startLoading();

            view.getOfferGrid().getDataProvider().updateRowData(
                    view.getOfferGrid().getStart(), data);
        }
    }

    public void onResponseAcceptOffer() {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_ASSIGNED_DEMANDS);
            }
        };
        ThankYouPopup.create(Storage.MSGS.thankYouAcceptOffer(), additionalAction);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    /** SelectionHandlers. **/
    //--------------------------------------------------------------------------
    /**
     * Show child table and fire event for getting its data.
     * Hides detail section if no row is selected.
     */
    private void addDemandTableSelectionHandler() {
        view.getDemandGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMAND_OFFERS);
                ClientDemandDetail selected = (ClientDemandDetail) ((SingleSelectionModel) view.getDemandGrid()
                        .getSelectionModel()).getSelectedObject();
                if (selected != null) {
                    selectedDemandObject = selected;
                    initDetailSection(selected);
                    Storage.setDemandId(selected.getDemandId());
                    Storage.setThreadRootId(selected.getThreadRootId());
                    view.setOfferTitleLabel(selected.getDemandTitle());
                    view.getOfferGrid().getDataCount(eventBus, null);
                }
            }
        });
    }

    /**
     * Show or Hide details section and action box.
     * Show if and only of one table row is selected.
     * Hide otherwise - not or more than one rows are selected.
     */
    public void addOfferGridSelectionModelHandler() {
        view.getOfferGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //tool bar items
                view.getActionBox().setVisible(view.getOfferGrid().getSelectedUserMessageIds().size() > 0);
                view.getOfferToolBar().setVisible(view.getOfferGrid().getSelectedUserMessageIds().size() == 1);
                //init details
                if (view.getOfferGrid().getSelectedUserMessageIds().size() == 1) {
                    selectedOfferedDemandOfferObject = view.getOfferGrid().getSelectedObjects().get(0);
                    initDetailSection(selectedOfferedDemandOfferObject);
                } else {
                    view.getDetailPanel().setVisible(false);
                }
            }
        });
    }

    /** Field updater. **/
    //--------------------------------------------------------------------------
    /**
     * Show and loads detail section. Show after clicking on certain columns that
     * have defined this fieldUpdater.
     */
    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<ClientOfferedDemandOffersDetail, String>() {
            @Override
            public void update(int index, ClientOfferedDemandOffersDetail object, String value) {
                object.setIsRead(true);
                view.setDemandTableVisible(false);
                view.setOfferTableVisible(true);

                MultiSelectionModel selectionModel = view.getOfferGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
            }
        };
        view.getOfferGrid().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getFinnishDateColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Field Updaters
    public void addCheckHeaderUpdater() {
        view.getOfferGrid().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<IUniversalDetail> rows = view.getOfferGrid().getVisibleItems();
                for (IUniversalDetail row : rows) {
                    ((MultiSelectionModel) view.getOfferGrid()
                            .getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getOfferGrid().getStarColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, Boolean>() {
                @Override
                public void update(int index, IUniversalDetail object, Boolean value) {
                    object.setIsStarred(!value);
                    view.getOfferGrid().redrawRow(index);
                    ((ActionBoxView) view.getActionBox().getWidget()).updateStar(object.getUserMessageId(), !value);
                }
            });
    }

    // Buttons
    private void addBackButtonHandler() {
        view.getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                backBtnClickHandlerInner();
            }
        });
    }

    private void backBtnClickHandlerInner() {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
        if (detailSection != null) {
            view.getDetailPanel().setVisible(false);
        }
        selectedDemandObject = null;
        selectedOfferedDemandOfferObject = null;
        view.getOfferGrid().getSelectionModel().clear();
        view.getDemandPager().startLoading();
        view.setOfferTableVisible(false);
        view.setDemandTableVisible(true);
        view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    private void addAcceptOfferButtonHandler() {
        view.getAcceptBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestAcceptOffer(
                        selectedOfferedDemandOfferObject.getOfferId());
            }
        });
    }

    /** Row Styles. **/
    private void addDemandGridRowStyles() {
        view.getDemandGrid().setRowStyles(new RowStyles<ClientDemandDetail>() {
            @Override
            public String getStyleNames(ClientDemandDetail row, int rowIndex) {
                if (row.getUnreadSubmessagesCount() > 0) {
                    return Storage.RSCS.grid().unread();
                }
                return "";
            }
        });
    }
}
