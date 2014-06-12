/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.clients;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.domain.adminModule.AdminClientDetail;
import com.eprovement.poptavka.shared.domain.demand.OriginDetail;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminClientsView.class)
public class AdminClientsPresenter
    extends LazyPresenter<AdminClientsPresenter.AdminClientsInterface, AdminEventBus> {

    private SearchModuleDataHolder searchDataHolder;

    /**
     * Interface for widget AdminClientsView.
     */
    public interface AdminClientsInterface extends LazyView, IsWidget {

        /**
         * @param origins data to be set to origin list box
         */
        void setOrigins(List<OriginDetail> origins);

        /**
         * @param client data to be set to widget
         */
        void setClientDetail(AdminClientDetail client);

        /**
         * @return the thable
         */
        UniversalAsyncGrid<AdminClientDetail> getTable();

        /**
         * @return the table's selection model
         */
        SingleSelectionModel<AdminClientDetail> getSelectionModel();

        /**
         * @return new email string
         */
        String getNewEmail();

        /**
         * @return selected origin id
         */
        Long getSelectedOriginId();

        /**
         * @return the save email button
         */
        Button getSaveEmailBtn();

        /**
         * @return the save origin button
         */
        Button getSaveOriginBtn();

        AdminToolbarView getToolbar();
    }

    /**************************************************************************/
    /*  Initialization                                                        */
    /**************************************************************************/
    /**
     * Initial methods for handling starting.
     * @param filter
     */
    public void onInitClients(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_CLIENTS);
        searchDataHolder = filter;
        view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        view.getSelectionModel().clear();
        view.getToolbar().getClientsFilterBtn().setVisible(true);
        view.asWidget().setStyleName(Storage.RSCS.common().userContent());
        eventBus.requestOrigins();
        eventBus.displayView(view);
    }

    //*************************************************************************/
    //                           ACTION HANDLERS                              */
    //*************************************************************************/
    /**
     * Register handlers for widget actions.
     */
    @Override
    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.setClientDetail(view.getSelectionModel().getSelectedObject());
            }
        });
        view.getSaveEmailBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestChangeEmail(view.getTable(),
                    view.getSelectionModel().getSelectedObject().getUserId(), view.getNewEmail());
            }
        });
        view.getSaveOriginBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestChangeOrigin(view.getTable(),
                    view.getSelectionModel().getSelectedObject().getId(), view.getSelectedOriginId());
            }
        });
        view.getToolbar().getClientsFilterBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (view.getToolbar().getClientsFilterBtn().isDown()) {
                    if (searchDataHolder == null) {
                        searchDataHolder = SearchModuleDataHolder.getSearchModuleDataHolder();
                    }
                    searchDataHolder.getAttributes().add(new FilterItem(
                        UserField.EMAIL,
                        FilterItem.Operation.OPERATION_LIKE,
                        Constants.ADMIN_CLIENTS_FILTER_EMAIL,
                        0));
                    view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
                } else {
                    searchDataHolder.getAttributes().clear();
                    view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
                }
            }
        });
    }

    /**
     * Sets requested origin data to origin list box.
     * @param result data
     */
    public void onResponseOrigins(List<OriginDetail> result) {
        view.setOrigins(result);
    }
}
