/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.supplierdemands.interfaces.IAbstractSupplier;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.presenter.LazyPresenter;
import java.util.Arrays;

/**
 * Common SupplierDemands' widgets functionality.
 *
 * @author Martin Slakvovsky
 */
public abstract class AbstractSupplierPresenter
    extends LazyPresenter<IAbstractSupplier.View, SupplierDemandsModuleEventBus>
    implements IAbstractSupplier.Presenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    protected SearchModuleDataHolder searchDataHolder;
    protected TableDisplayDetailModule selectedObject;
    protected FieldUpdater textFieldUpdater = new FieldUpdater<TableDisplayUserMessage, String>() {
        @Override
        public void update(int index, TableDisplayUserMessage object, String value) {
            object.setRead(true);

            MultiSelectionModel selectionModel = (MultiSelectionModel) view.getTable().getSelectionModel();
            selectionModel.clear();
            selectionModel.setSelected(object, true);
        }
    };
    protected FieldUpdater starFieldUpdater = new FieldUpdater<TableDisplayUserMessage, Boolean>() {
        @Override
        public void update(int index, TableDisplayUserMessage object, Boolean value) {
            object.setStarred(!value);
            view.getTable().redrawRow(index);
            eventBus.requestStarStatusUpdate(Arrays.asList(object.getUserMessageId()), !value);
        }
    };
    protected ValueUpdater<Boolean> checkboxHeader = new ValueUpdater<Boolean>() {
        @Override
        public void update(Boolean value) {
            for (Object row : view.getTable().getVisibleItems()) {
                ((MultiSelectionModel) view.getTable().getSelectionModel()).setSelected(row, value);
            }
        }
    };
    protected RowStyles rowStyles = new RowStyles<TableDisplayUserMessage>() {
            @Override
            public String getStyleNames(TableDisplayUserMessage row, int rowIndex) {
                if (!row.isRead()) {
                    return Storage.GRSCS.dataGridStyle().unread();
                }
                return "";
            }
        };

    /**************************************************************************/
    /* General Widget events                                                  */
    /**************************************************************************/
    /**
     * Inits table when creating presenter.
     */
    @Override
    public void createPresenter() {
        view.initTable(initTable());
    }

    /**
     * Binds table selection handler and sets footer.
     */
    @Override
    public void bindView() {
        addTableSelectionModelHandler();
    }

    /**
     * Recalculate table height if resize event occurs.
     * Usually paddings or margins changes on smaller resolutions.
     * @param actualWidth
     */
    public void onResize(int actualWidth) {
        view.getTable().resize(actualWidth);
    }

    /**
     * Constuctor for common initialization.
     */
    public void initAbstractPresenter(SearchModuleDataHolder filter, int widgetId) {
        //Must be present here. Loading data rely on this atrtibute
        Storage.setCurrentlyLoadedView(widgetId);
        eventBus.supplierMenuStyleChange(widgetId);
        eventBus.createTokenForHistory();

        eventBus.initActionBox(view.getToolbar().getActionBox(), view.getTable());
        eventBus.initDetailSection(view.getTable(), view.getDetailPanel());
        eventBus.setFooter(view.getFooterContainer());

        searchDataHolder = filter;

        eventBus.displayView(view.getWidgetView());

        if (view.getTable().getSelectionModel() != null) {
            ((MultiSelectionModel) view.getTable().getSelectionModel()).clear();
        }
        view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Protected methods                                                      */
    /**************************************************************************/
    // Detail section
    //--------------------------------------------------------------------------
    /**
     * Initialize demand, supplier and conversation tabs in Details sections.
     * If details wrapper instance doesn't exist yet, create it and in response of
     * creation initialize demand, supplier, conversation tabs.
     * If instance already exist, initialize and show tabs immediately.
     *
     * @param demandId
     */
    protected void initDetailSectionFull(TableDisplayDetailModule selectedDetail) {
        eventBus.buildDetailSectionTabs(new DetailModuleBuilder.Builder()
            .addDemandTab(selectedDetail.getDemandId())
            .addUserTab(selectedDetail.getSupplierId())
            .addConversationTab(selectedDetail.getThreadRootId(), selectedDetail.getSenderId())
            .selectTab(DetailModuleBuilder.CONVERSATION_TAB)
            .build());
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Show or Hide details section and action box.
     * Show if and only of one table row is selected.
     * Hide otherwise - not or more than one rows are selected.
     */
    private void addTableSelectionModelHandler() {
        view.getTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //  display actionBox if needed (more than one item selected)
                view.getToolbar().getActionBox().setVisible(view.getSelectedObjects().size() > 0);

                if (view.getSelectedObjects().size() == 1) {
                    //  display detail section if only one item selected
                    selectedObject =
                        (TableDisplayDetailModule) view.getSelectedObjects().iterator().next();
                    if (selectedObject != null) {
                        initDetailSectionFull(selectedObject);
                        eventBus.openDetail();
                    }
                } else {
                    //  display advertisement if more than one item selected
                    selectedObject = null;
                    eventBus.displayAdvertisement();
                }
            }
        });
    }

    /**************************************************************************/
    /* Abstract methods                                                       */
    /**************************************************************************/
    abstract UniversalAsyncGrid initTable();
}