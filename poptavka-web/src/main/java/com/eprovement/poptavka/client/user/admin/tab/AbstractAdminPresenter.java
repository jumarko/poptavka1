/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.admin.tab.AbstractAdminPresenter.IAbstractAdminView;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Set;

/**
 * AbsatractAdminPresenter.
 *
 * @author Martin Slakvovsky
 */
public abstract class AbstractAdminPresenter
    extends LazyPresenter<IAbstractAdminView, AdminEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface IAbstractAdminView extends LazyView, IsWidget {

        void initTable(UniversalAsyncGrid table);

        UniversalAsyncGrid getTable();

        Set getSelectedObjects();

        SimplePanel getFooterContainer();

        SimplePanel getDetailPanel();

        AdminToolbarView getToolbar();

        Widget getWidgeView();
    }

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
     * Binds table selection handlers and sets footer.
     * Don't forget to call super.bindView() in implementing class.
     */
    @Override
    public void bindView() {
        addTableSelectionModelHandler();
        eventBus.setFooter(view.getFooterContainer());
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    protected SearchModuleDataHolder searchDataHolder;
    protected TableDisplayDetailModule selectedObject;
    protected FieldUpdater textFieldUpdater = new FieldUpdater<TableDisplayDetailModule, String>() {
        @Override
        public void update(int index, TableDisplayDetailModule object, String value) {
            MultiSelectionModel selectionModel = (MultiSelectionModel) view.getTable().getSelectionModel();
            selectionModel.clear();
            selectionModel.setSelected(object, true);
        }
    };
//    TODO Martin - why commented?
//    protected FieldUpdater starFieldUpdater = new FieldUpdater<TableDisplayUserMessage, Boolean>() {
//        @Override
//        public void update(int index, TableDisplayUserMessage object, Boolean value) {
//            object.setStarred(!value);
//            view.getTable().redrawRow(index);
//            eventBus.updateStar(object.getUserMessageId(), !value);
//        }
//    };
    protected ValueUpdater<Boolean> checkboxHeader = new ValueUpdater<Boolean>() {
        @Override
        public void update(Boolean value) {
            for (Object row : view.getTable().getVisibleItems()) {
                ((MultiSelectionModel) view.getTable().getSelectionModel()).setSelected(row, value);
            }
        }
    };

    /**************************************************************************/
    /* Protected methods                                                      */
    /**************************************************************************/
    // Detail section
    //--------------------------------------------------------------------------
    /**
     * Inits detail section - demand.
     * @param selectedDetail the TableDisplayDetailModule
     */
    protected void initDetailSectionDemand(TableDisplayDetailModule selectedDetail) {
        eventBus.buildDetailSectionTabs(new DetailModuleBuilder.Builder()
            .addDemandTab(selectedDetail.getDemandId())
            .selectTab(DetailModuleBuilder.DEMAND_DETAIL_TAB)
            .build());
    }

    /**
     * Inits detail section - conversation.
     * @param selectedDetail the TableDisplayDetailModule
     */
    protected void initDetailSectionConversation(TableDisplayDetailModule selectedDetail) {
        eventBus.buildDetailSectionTabs(new DetailModuleBuilder.Builder()
            .addDemandTab(selectedDetail.getDemandId())
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
                        initDetailSectionDemand(selectedObject);
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