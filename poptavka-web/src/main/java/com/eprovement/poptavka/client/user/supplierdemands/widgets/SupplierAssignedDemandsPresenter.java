package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.detail.FeedbackPopupView;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.client.user.widget.grid.columns.DisplayNameColumn.TableDisplayDisplayName;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
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
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

@Presenter(view = AbstractSupplierView.class)
public class SupplierAssignedDemandsPresenter extends AbstractSupplierPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private FeedbackPopupView ratePopup;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        super.bindView();
        // Toolbar buttons handlers
        addFinnishButtonHandler();
        // Table Handlers
        addTableSelectionModelClickHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierAssignedDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_ASSIGNED_DEMANDS);

        eventBus.supplierMenuStyleChange(Constants.SUPPLIER_ASSIGNED_DEMANDS);
        eventBus.initDetailSection(view.getTable(), view.getDetailPanel());

        initWidget(filter);
    }

    public void onInitSupplierClosedDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_CLOSED_DEMANDS);

        eventBus.supplierMenuStyleChange(Constants.SUPPLIER_CLOSED_DEMANDS);
        eventBus.initDetailSection(view.getTable(), view.getDetailPanel());

        initWidget(filter);
    }

    public void onResponseFeedback() {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_ASSIGNED_DEMANDS);
            }
        };
        eventBus.showThankYouPopup(Storage.MSGS.thankYouFinishDemand(), additionalAction);
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierAssignedDemands(List<IUniversalDetail> data) {
        GWT.log("++ onResponseSuppliersAssignedDemands");

        view.getTable().getDataProvider().updateRowData(view.getTable().getStart(), data);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void initWidget(SearchModuleDataHolder filter) {
        eventBus.resetSearchBar(new Label("Supplier's assigned/closed projects attibure's selector will be here."));
        eventBus.createTokenForHistory();
        searchDataHolder = filter;
        eventBus.initActionBox(view.getToolbar().getActionBox(), view.getTable());

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    public void addTableSelectionModelClickHandler() {
        view.getTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //init details
                if (view.getSelectedUserMessageIds().size() == 1) {
                    view.getToolbar().getFinishBtn().setVisible(
                        Storage.getCurrentlyLoadedView() == Constants.SUPPLIER_ASSIGNED_DEMANDS ? true : false);
                } else {
                    view.getToolbar().getFinishBtn().setVisible(false);
                }
            }
        });
    }

    private void addFinnishButtonHandler() {
        view.getToolbar().getFinishBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ratePopup = new FeedbackPopupView(FeedbackPopupView.SUPPLIER);
                ratePopup.setDisplayName(((TableDisplayDisplayName) selectedObject).getDisplayName());
                ratePopup.getRateBtn().addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        eventBus.requestFinishAndRateClient(
                            selectedObject.getDemandId(),
                            ((IUniversalDetail) selectedObject).getOfferId(),
                            Integer.valueOf(ratePopup.getRating()), ratePopup.getComment());
                    }
                });
            }
        });
    }

    /**
     * Create supplier offers table.
     */
    @Override
    public UniversalAsyncGrid initTable() {
        return new UniversalGridFactory.Builder<SupplierOffersDetail>(view.getToolbar().getPager().getPageSize())
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnDemandTitle(textFieldUpdater)
            .addColumnPrice(textFieldUpdater)
            .addColumnClientRating(textFieldUpdater) //TODO rename to rating
            .addColumnOfferReceivedDate(textFieldUpdater)
            .addColumnFinishDate(textFieldUpdater)
            .addDefaultSort(Arrays.asList(new SortPair(OfferDetail.OfferField.FINISH_DATE)))
            .addSelectionModel(new MultiSelectionModel(), SupplierOffersDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }
}
