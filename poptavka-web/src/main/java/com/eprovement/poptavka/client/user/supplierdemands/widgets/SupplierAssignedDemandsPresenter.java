/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.actionBox.ActionBoxView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.detail.FeedbackPopupView;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;

@Presenter(view = SupplierAssignedDemandsView.class, multiple = true)
public class SupplierAssignedDemandsPresenter extends LazyPresenter<
        SupplierAssignedDemandsPresenter.SupplierAssignedDemandsLayoutInterface, SupplierDemandsModuleEventBus> {

    public interface SupplierAssignedDemandsLayoutInterface extends LazyView, IsWidget {

        UniversalTableGrid getDataGrid();

        SimplePager getPager();

        Button getFinnishBtn();

        SimplePanel getDetailPanel();

        SimplePanel getActionBox();

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater;
    private IUniversalDetail selectedObject = null;
    private long lastOpenedAssignedDemand = -1;
    private long selectedSupplierAssignedDemandId = -1;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Table Handlers
        addTableSelectionModelClickHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addColumnFieldUpdaters();
        // Buttons handlers
        addFinnishButtonHandler();
        // Row styles
        addGridRowStyles();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierAssignedDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_ASSIGNED_DEMANDS);

        initWidget(filter);
    }

    public void onInitSupplierClosedDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_CLOSED_DEMANDS);

        initWidget(filter);
    }

    /**************************************************************************/
    /* Details Wrapper                                                        */
    /**************************************************************************/
    /**
     * Response method to requesting details wrapper instance.
     * Some additional actions can be added here.
     * @param detailSection Details wrapper instance.
     */
    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        if (detailSection != null) {
            detailSection.initDetailWrapper(view.getDataGrid(), view.getDetailPanel());
            this.detailSection = detailSection;
            if (selectedObject != null) {
                initDetailSection(selectedObject);
            }
        }
    }

    /**
     * Initialize demand & conversation tabs in detail section.
     * If details wrapper instance doesn't exist yet, create it and in response of
     * creation initialize requested tabs.
     * If instance already exist, initialize and show requested tabs immediately.
     *
     * @param demandId
     */
    private void initDetailSection(IUniversalDetail demandDetail) {
        if (detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        } else {
            view.getDetailPanel().setVisible(true);
            detailSection.initDetails(
                    demandDetail.getDemandId(),
                    demandDetail.getThreadRootId());
        }
    }

    public void onResponseFinnishOffer() {
        final FeedbackPopupView ratePopup =
                new FeedbackPopupView(FeedbackPopupView.CLIENT);
        ratePopup.setClientName(selectedObject.getClientName());
        ratePopup.getRateBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestRateClient(selectedObject.getDemandId(), Integer.valueOf(ratePopup.getRating()),
                        ratePopup.getComment());
            }
        });
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierAssignedDemands(List<IUniversalDetail> data) {
        GWT.log("++ onResponseSuppliersAssignedDemands");

        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), data);

        if (selectedSupplierAssignedDemandId != -1) {
            eventBus.getSupplierAssignedDemand(selectedSupplierAssignedDemandId);
        }
    }

    public void onSelectSupplierAssignedDemand(SupplierOffersDetail detail) {
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
        eventBus.setUpSearchBar(new Label("Supplier's assigned/closed projects attibure's selector will be here."));
        eventBus.activateSupplierAssignedDemands();
        eventBus.createTokenForHistory();
        searchDataHolder = filter;
        eventBus.initActionBox(view.getActionBox(), view.getDataGrid());

        eventBus.displayView(view.getWidgetView());
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
        view.getDataGrid().getStarColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, Boolean>() {
                @Override
                public void update(int index, IUniversalDetail object, Boolean value) {
                    object.setIsStarred(!value);
                    view.getDataGrid().redrawRow(index);
                    ((ActionBoxView) view.getActionBox().getWidget()).getActionStar().getScheduledCommand().execute();
                }
            });
    }

    public void addTableSelectionModelClickHandler() {
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //set actionBox visibility
                view.getActionBox().setVisible(view.getDataGrid().getSelectedUserMessageIds().size() > 0);
                //init details
                if (view.getDataGrid().getSelectedUserMessageIds().size() == 1) {
                    view.getFinnishBtn().setVisible(true);
                    selectedObject = view.getDataGrid().getSelectedObjects().get(0);
                    initDetailSection(selectedObject);
                } else {
                    view.getFinnishBtn().setVisible(false);
                    view.getDetailPanel().setVisible(false);
                }
            }
        });
    }

    public void addColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<SupplierOffersDetail, String>() {
            @Override
            public void update(int index, SupplierOffersDetail object, String value) {
                object.setIsRead(true);
                MultiSelectionModel selectionModel = view.getDataGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
            }
        };
        view.getDataGrid().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getFinnishDateColumn().setFieldUpdater(textFieldUpdater);
    }

    /** RowStyles. **/
    private void addGridRowStyles() {
        view.getDataGrid().setRowStyles(new RowStyles<IUniversalDetail>() {
            @Override
            public String getStyleNames(IUniversalDetail row, int rowIndex) {
                if (!row.isRead()) {
                    return Storage.RSCS.grid().unread();
                }
                return "";
            }
        });
    }

    private void addFinnishButtonHandler() {
        view.getFinnishBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //Finnish button is always awaylable of single selections, threfore can use 'get(0)'.
                eventBus.requestFinishOffer(view.getDataGrid().getSelectedObjects().get(0).getOfferId());
            }
        });
    }
}