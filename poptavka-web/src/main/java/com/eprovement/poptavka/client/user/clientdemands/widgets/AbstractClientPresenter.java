/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.clientdemands.interfaces.IAbstractClient;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.detail.interfaces.TableDisplayDetailModuleSupplier;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.presenter.LazyPresenter;
import java.util.Arrays;

/**
 * Common logic for ClientDemands module.
 *
 * @author Martin Slakvovsky
 */
public abstract class AbstractClientPresenter
    extends LazyPresenter<IAbstractClient.View, ClientDemandsModuleEventBus>
    implements IAbstractClient.Presenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    protected SearchModuleDataHolder searchDataHolder;
    protected TableDisplayDetailModuleSupplier selectedParentObject;
    protected TableDisplayDetailModuleSupplier selectedChildObject;
    protected FieldUpdater textFieldUpdater = new FieldUpdater<TableDisplayUserMessage, String>() {
        @Override
        public void update(int index, TableDisplayUserMessage object, String value) {
            object.setRead(true);

            SetSelectionModel selectionModel = (SetSelectionModel) view.getChildTable().getSelectionModel();
            selectionModel.clear();
            selectionModel.setSelected(object, true);
        }
    };
    protected FieldUpdater starFieldUpdater = new FieldUpdater<TableDisplayUserMessage, Boolean>() {
        @Override
        public void update(int index, TableDisplayUserMessage object, Boolean value) {
            object.setStarred(!value);
            view.getChildTable().redrawRow(index);
            eventBus.requestStarStatusUpdate(Arrays.asList(object.getUserMessageId()), !value);
        }
    };
    protected ValueUpdater<Boolean> checkboxHeader = new ValueUpdater<Boolean>() {
        @Override
        public void update(Boolean value) {
            for (Object row : view.getChildTable().getVisibleItems()) {
                ((SetSelectionModel) view.getChildTable().getSelectionModel()).setSelected(row, value);
            }
        }
    };
    protected RowStyles rowStyles = new RowStyles() {
        @Override
        public String getStyleNames(Object row, int rowIndex) {
            boolean unread = false;
            if (row instanceof TableDisplayUserMessage) {
                unread = !((TableDisplayUserMessage) row).isRead();
            } else if (row instanceof TableDisplayDemandTitle) {
                unread = ((TableDisplayDemandTitle) row).getUnreadMessagesCount() > 0;
            }
            if (unread) {
                return Storage.GRSCS.dataGridStyle().unread();
            }
            return "";
        }
    };
    protected boolean isInitializing;

    /**************************************************************************/
    /* General Widget events                                                  */
    /**************************************************************************/
    /**
     * Inits table when creating presenter.
     */
    @Override
    public void createPresenter() {
        view.initTables(initParentTable(), initChildTable());
    }

    /**
     * Binds tables handlers.
     */
    @Override
    public void bindView() {
        addParentTableSelectionHandler();
        addChildTableSelectionModelHandler();
    }

    /**
     * Recalculate table height if resize event occurs.
     * Usually paddings or margins changes on smaller resolutions.
     * @param actualWidth
     */
    public void onResize(int actualWidth) {
        view.getParentTable().resize(actualWidth);
        view.getChildTable().resize(actualWidth);
    }

    /**
     * Constuctor for common initialization.
     */
    public void initAbstractPresenter(SearchModuleDataHolder filter, int widgetId) {
        //Must be present here. Loading data rely on this atrtibute
        Storage.setCurrentlyLoadedView(widgetId);
        eventBus.clientDemandsMenuStyleChange(widgetId);
        eventBus.createTokenForHistory();

        eventBus.setFooter(view.getFooterContainer());

        searchDataHolder = filter;

        eventBus.loadingDivHide();
        eventBus.displayView(view.getWidgetView());
    }

    /**************************************************************************/
    /* Protected methods                                                      */
    /**************************************************************************/
    // Table visibility
    //--------------------------------------------------------------------------
    /**
     * Sets parent table visibility.
     * @param visible true to show, false to hide
     */
    protected void setParentTableVisible(boolean visible) {
        if (visible) {
            view.getToolbar().bindPager(view.getParentTable());
            view.getToolbar().getPager().getPager().startLoading();
        }
        view.setParentTableVisible(visible);
    }

    /**
     * Sets child table visibility.
     * @param visible true to show, false to hide
     */
    protected void setChildTableVisible(boolean visible) {
        if (visible) {
            view.getToolbar().bindPager(view.getChildTable());
            view.getToolbar().getPager().getPager().startLoading();
        }
        view.setChildTableVisible(visible);
        view.getToolbar().getBackBtn().setVisible(visible);
    }

    // Detail section
    //--------------------------------------------------------------------------
    /**
     * Initialize demand tab in Details sections.
     * If details wrapper instance doesn't exist yet, create it and in response of
     * creation initialize demand tab.
     * If instance already exist, initialize and show demand tab immediately.
     *
     * @param demandId
     */
    protected void initDetailSectionDemand(TableDisplayDetailModuleSupplier selectedDetail) {
        eventBus.buildDetailSectionTabs(new DetailModuleBuilder.Builder()
            .addDemandTab(selectedDetail.getDemandId())
            .selectTab(DetailModuleBuilder.DEMAND_DETAIL_TAB)
            .build());
        view.getToolbar().setEditDemandBtnsVisibility(true);
    }

    /**
     * Initialize demand, supplier and conversation tabs in Details sections.
     * If details wrapper instance doesn't exist yet, create it and in response of
     * creation initialize demand, supplier, conversation tabs.
     * If instance already exist, initialize and show tabs immediately.
     *
     * @param demandId
     */
    protected void initDetailSectionFull(TableDisplayDetailModuleSupplier selectedDetail) {
        eventBus.buildDetailSectionTabs(new DetailModuleBuilder.Builder()
            .addDemandTab(selectedDetail.getDemandId())
            .addSupplierTab(selectedDetail.getSupplierId(), false)
            .addConversationTab(selectedDetail.getThreadRootId(), selectedDetail.getSenderId())
            .selectTab(DetailModuleBuilder.CONVERSATION_TAB)
            .build());
        view.getToolbar().setEditDemandBtnsVisibility(false);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Binds parent table selection handler.
     * Inits detail section and displays child table if needed.
     */
    private void addParentTableSelectionHandler() {
        if (view.getParentTable().getColumnCount() != 0) {
            view.getParentTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                @Override
                public void onSelectionChange(SelectionChangeEvent event) {
                    if (!isInitializing) {
                        switch(Storage.getCurrentlyLoadedView()) {
                            case Constants.CLIENT_OFFERED_DEMANDS:
                                Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMAND_OFFERS);
                                break;
                            case Constants.CLIENT_DEMANDS:
                                Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMAND_DISCUSSIONS);
                                break;
                            default:
                                break;
                        }
                    }
                    isInitializing = false;
                    selectedParentObject = (TableDisplayDetailModuleSupplier)
                        ((SingleSelectionModel) view.getParentTable().getSelectionModel()).getSelectedObject();
                    if (selectedParentObject != null) {
                        //  display detail section
                        initDetailSectionDemand(selectedParentObject);
                        //  set flags
                        Storage.setDemandId(selectedParentObject.getDemandId());
                        Storage.setThreadRootId(selectedParentObject.getThreadRootId());
                        //  display child table if any data found
                        view.setDemandTitleLabel(((TableDisplayDemandTitle) selectedParentObject).getDemandTitle());
                        view.getChildTable().getDataCount(eventBus, null);
                    }
                }
            });
        }
    }

    /**
     * Show or Hide details section and action box.
     * Show if and only of one table row is selected.
     * Hide otherwise - not or more than one rows are selected.
     */
    private void addChildTableSelectionModelHandler() {
        view.getChildTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //  display actionBox if needed (more than one item selected)
                view.getToolbar().getActionBox().setVisible(view.getChildTableSelectedObjects().size() > 0);

                if (view.getChildTableSelectedObjects().size() == 1) {
                    //  display detail section if only one item selected
                    selectedChildObject
                        = (TableDisplayDetailModuleSupplier) view.getChildTableSelectedObjects().iterator().next();
                    if (selectedChildObject != null) {
                        initDetailSectionFull(selectedChildObject);
                        eventBus.openDetail();
                    }
                } else {
                    //  display advertisement if more than one item selected
                    selectedChildObject = null;
                    eventBus.displayAdvertisement();
                }
            }
        });
    }

    /**************************************************************************/
    /* Abstract methods                                                       */
    /**************************************************************************/
    abstract UniversalAsyncGrid initParentTable();

    abstract UniversalAsyncGrid initChildTable();
}
