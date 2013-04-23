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
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;

@Presenter(view = SupplierDemandsView.class, multiple = true)
public class SupplierDemandsPresenter extends LazyPresenter<
        SupplierDemandsPresenter.SupplierDemandsLayoutInterface, SupplierDemandsModuleEventBus> {

    public interface SupplierDemandsLayoutInterface extends LazyView, IsWidget {

        UniversalTableGrid getDataGrid();

        SimplePager getPager();

        SimplePanel getDetailPanel();

        SimplePanel getActionBox();

        void loadingDivShow(Widget holderWidget);

        void loadingDivHide(Widget holderWidget);

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater;
    private IUniversalDetail selectedObject = null;

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
        // Table Change Handlers
        addTableSelectionModelClickHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addColumnFieldUpdaters();
        // Row styles
        addGridRowStyles();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        eventBus.supplierMenuStyleChange(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        eventBus.createTokenForHistory();
        eventBus.initActionBox(view.getActionBox(), view.getDataGrid());

        eventBus.setUpSearchBar(new Label("Supplier's projects attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
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
            detailSection.getView().getReplyHolder().allowSendingOffer();
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
                    demandDetail.getThreadRootId(),
                    demandDetail.getSenderId());
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierDemands(List<IUniversalDetail> data) {
        GWT.log("++ onResponseSupplierPotentialDemands");

        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), data);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    // TableWidget handlers
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
                view.getDataGrid().redrawRow(index);
                ((ActionBoxView) view.getActionBox().getWidget()).updateStar(object.getUserMessageId(), !value);
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
                    selectedObject = view.getDataGrid().getSelectedObjects().get(0);
                    initDetailSection(selectedObject);
                } else {
                    view.getDetailPanel().setVisible(false);
                }
            }
        });
    }

    public void addColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<SupplierPotentialDemandDetail, Object>() {
            @Override
            public void update(int index, SupplierPotentialDemandDetail object, Object value) {
                object.setIsRead(true);
                MultiSelectionModel selectionModel = view.getDataGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
            }
        };
        view.getDataGrid().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getUrgencyColumn().setFieldUpdater(textFieldUpdater);
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
}