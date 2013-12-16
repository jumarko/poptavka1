package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.clientdemands.toolbar.ClientToolbarView;
import com.eprovement.poptavka.client.user.clientdemands.widgets.AbstractClientPresenter.IAbstractClientView;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Martin Slakvovsky
 */
public abstract class AbstractClientPresenter
    extends LazyPresenter<IAbstractClientView, ClientDemandsModuleEventBus> {

    public interface IAbstractClientView extends LazyView, IsWidget {

        void initTables(UniversalAsyncGrid parentTable, UniversalAsyncGrid childTable);

        UniversalAsyncGrid getParentTable();

        UniversalAsyncGrid getChildTable();

        List<Long> getChildTableSelectedUserMessageIds();

        Set getChildTableSelectedObjects();

        SimplePanel getFooterContainer();

        SimplePanel getDetailPanel();

        ClientToolbarView getToolbar();

        IsWidget getWidgetView();

        // Setters
        void setParentTableVisible(boolean visible);

        void setChildTableVisible(boolean visible);

        void setDemandTitleLabel(String text);
    }

    /**************************************************************************/
    /* General Widget events                                                  */
    /**************************************************************************/
    @Override
    public void createPresenter() {
        view.initTables(initParentTable(), initChildTable());
    }

    @Override
    public void bindView() {
        addParentTableSelectionHandler();
        addChildTableSelectionModelHandler();
        eventBus.setFooter(view.getFooterContainer());
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    protected SearchModuleDataHolder searchDataHolder;
    protected TableDisplayDetailModule selectedParentObject;
    protected TableDisplayDetailModule selectedChildObject;
    protected FieldUpdater textFieldUpdater = new FieldUpdater<TableDisplayUserMessage, String>() {
        @Override
        public void update(int index, TableDisplayUserMessage object, String value) {
            object.setRead(true);

            MultiSelectionModel selectionModel = (MultiSelectionModel) view.getChildTable().getSelectionModel();
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
                ((MultiSelectionModel) view.getChildTable().getSelectionModel()).setSelected(row, value);
            }
        }
    };
    protected RowStyles rowStyles = new RowStyles<TableDisplayUserMessage>() {
            @Override
            public String getStyleNames(TableDisplayUserMessage row, int rowIndex) {
                if (row.getUnreadMessagesCount() > 0) {
                    return Storage.GRSCS.dataGridStyle().unread();
                }
                return "";
            }
        };

    /**************************************************************************/
    /* Protected methods                                                      */
    /**************************************************************************/
    // Table visibility
    //--------------------------------------------------------------------------
    protected void setParentTableVisible(boolean visible) {
        if (visible) {
            view.getToolbar().bindPager(view.getParentTable());
            view.getToolbar().getPager().getPager().startLoading();
        }
        view.setParentTableVisible(visible);
    }

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
    protected void initDetailSectionDemand(TableDisplayDetailModule selectedDetail) {
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
    protected void initDetailSectionFull(TableDisplayDetailModule selectedDetail) {
        eventBus.buildDetailSectionTabs(new DetailModuleBuilder.Builder()
            .addDemandTab(selectedDetail.getDemandId())
            .addUserTab(selectedDetail.getSupplierId())
            .addConversationTab(selectedDetail.getThreadRootId(), selectedDetail.getSenderId())
            .selectTab(DetailModuleBuilder.CONVERSATION_TAB)
            .build());
        view.getToolbar().setEditDemandBtnsVisibility(false);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void addParentTableSelectionHandler() {
        if (view.getParentTable().getColumnCount() != 0) {
            view.getParentTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                @Override
                public void onSelectionChange(SelectionChangeEvent event) {
                    selectedParentObject = (TableDisplayDetailModule)
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
                view.getToolbar().getActionBox().setVisible(view.getChildTableSelectedUserMessageIds().size() > 0);

                if (view.getChildTableSelectedUserMessageIds().size() == 1) {
                    //  display detail section if only one item selected
                    selectedChildObject =
                        (TableDisplayDetailModule) view.getChildTableSelectedObjects().iterator().next();
                    if (selectedChildObject != null) {
                        initDetailSectionFull(selectedChildObject);
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