package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

@Presenter(view = AbstractClientView.class)
public class ClientOffersPresenter extends AbstractClientPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private ClientOfferedDemandOffersDetail selectedOfferedDemandOfferObject;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        super.bindView();
        // Toolbar handlers
        addBackButtonHandler();
        addAcceptOfferButtonHandler();
        // Selection Handlers
        addDemandTableSelectionHandler();
        addOfferGridSelectionModelHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientOffers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);

        eventBus.clientDemandsMenuStyleChange(Constants.CLIENT_OFFERED_DEMANDS);
        eventBus.initActionBox(this.view.getToolbar().getActionBox(), view.getParentTable());
        eventBus.initDetailSection(view.getParentTable(), view.getDetailPanel());
        //Set visibility
        setChildTableVisible(false);
        setParentTableVisible(true);

        eventBus.resetSearchBar(new Label("Client's contests attibure's selector will be here."));
        searchDataHolder = filter;
        eventBus.createTokenForHistory();

        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
        //request data to demand(parent) table
        if (view.getParentTable().isVisible()) {
            view.getParentTable().getDataCount(eventBus, new SearchDefinition(
                0, this.view.getToolbar().getPager().getPageSize(), searchDataHolder,
                view.getParentTable().getSort().getSortOrder()));
        } else {
            backBtnClickHandlerInner();
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientOfferedDemands(List<ClientDemandDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemands");

        view.getParentTable().getDataProvider().updateRowData(
            view.getParentTable().getStart(), data);
    }

    public void onDisplayClientOfferedDemandOffers(List<ClientOfferedDemandOffersDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemandOffers");

        if (!data.isEmpty()) {
            setParentTableVisible(false);
            setChildTableVisible(true);
            view.getToolbar().getPager().getPager().startLoading();

            view.getChildTable().getDataProvider().updateRowData(
                view.getChildTable().getStart(), data);
        }
    }

    public void onResponseAcceptOffer() {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_ASSIGNED_DEMANDS);
            }
        };
        eventBus.showThankYouPopup(Storage.MSGS.thankYouAcceptOffer(), additionalAction);
    }

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
        view.getParentTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMAND_OFFERS);
            }
        });
    }

    /**
     * Show or Hide details section and action box.
     * Show if and only of one table row is selected.
     * Hide otherwise - not or more than one rows are selected.
     */
    private void addOfferGridSelectionModelHandler() {
        view.getChildTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //tool bar items
                view.getToolbar().getAcceptBtn().setVisible(view.getChildTableSelectedUserMessageIds().size() == 1);
            }
        });
    }

    // Buttons
    private void addBackButtonHandler() {
        view.getToolbar().getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                backBtnClickHandlerInner();
            }
        });
    }

    private void backBtnClickHandlerInner() {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
        eventBus.displayAdvertisement();
        selectedOfferedDemandOfferObject = null;
        view.getToolbar().getPager().getPager().startLoading();
        setChildTableVisible(false);
        setParentTableVisible(true);
        view.getParentTable().getDataCount(eventBus, new SearchDefinition(
            0, view.getToolbar().getPager().getPageSize(), searchDataHolder,
            view.getParentTable().getSort().getSortOrder()));
    }

    private void addAcceptOfferButtonHandler() {
        view.getToolbar().getAcceptBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestAcceptOffer(
                    selectedOfferedDemandOfferObject.getOfferId());
            }
        });
    }

    /**
     * Client demands user case - parent table - demands table
     */
    @Override
    public UniversalAsyncGrid initParentTable() {
        return new UniversalGridFactory.Builder<ClientDemandDetail>()
            .addColumnDemandTitle(null)
            .addColumnPrice(null)
            .addColumnEndDate(null)
            .addColumnUrgency()
            .addDefaultSort(Arrays.asList(new SortPair(FullDemandDetail.DemandField.VALID_TO)))
            .addSelectionModel(new SingleSelectionModel(), ClientDemandDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }

    /**
     * Client demands user case - child table - conversation table
     */
    @Override
    public UniversalAsyncGrid initChildTable() {
        return new UniversalGridFactory.Builder<ClientOfferedDemandOffersDetail>()
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnDisplayName(textFieldUpdater)
            .addColumnPrice(textFieldUpdater)
            .addColumnOfferReceivedDate(textFieldUpdater)
            .addColumnFinishDate(textFieldUpdater)
            .addDefaultSort(Arrays.asList(new SortPair(OfferDetail.OfferField.CREATED)))
            .addSelectionModel(new MultiSelectionModel(), ClientOfferedDemandOffersDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }
}
