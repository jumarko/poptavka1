/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Presenter(view = ClientProjectsView.class)
public class ClientProjectsPresenter
        extends LazyPresenter<ClientProjectsPresenter.ClientProjectsLayoutInterface, ClientDemandsEventBus> {

    public interface ClientProjectsLayoutInterface extends LazyView, IsWidget {

        // Columns
        Header getCheckHeader();

        Column<ClientProjectConversationDetail, Boolean> getCheckColumn();

        Column<ClientProjectConversationDetail, Boolean> getStarColumn();

        Column<ClientProjectConversationDetail, String> getSupplierNameColumn();

        Column<ClientProjectConversationDetail, String> getBodyPreviewColumn();

        Column<ClientProjectConversationDetail, String> getDateColumn();

        // Buttons
        Button getReplyButton();

        // Others
        UniversalAsyncGrid<ClientProjectDetail> getDemandGrid();

        UniversalAsyncGrid<ClientProjectConversationDetail> getConversationGrid();

        int getDemandPageSize();

        int getConversationPageSize();

        List<Long> getSelectedIdList();

        Set<ClientProjectConversationDetail> getSelectedMessageList();

        //ListBox
        ListBox getActions();

        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();

        // Setters
        void setConversationTableVisible(boolean visible);

        void setDemandTitleLabel(String text);
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private ViewType type = ViewType.EDITABLE;
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedProjectConversation = -1;

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
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Listbox actions
        addActionChangeHandler();
        // Buttons Actions
        addReplyButtonHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientProjects(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_PROJECTS);
        searchDataHolder = filter;
        view.getDemandGrid().getDataCount(eventBus, searchDataHolder);

        view.getWidgetView().asWidget().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (this.detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        if (this.detailSection == null) {
            this.detailSection = detailSection;
            this.detailSection.initDetailWrapper(view.getWrapperPanel(), type);
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientProjects(List<ClientProjectDetail> data) {
        GWT.log("++ onResponseClientsProjects");

        view.getDemandGrid().updateRowData(data);
    }

    public void onDisplayClientProjectConversations(List<ClientProjectConversationDetail> data) {
        GWT.log("++ onResponseClientsProjects");

        view.getConversationGrid().updateRowData(data);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related conversation
     * @param userMessageId ID for demand related conversation
     */
    public void displayDetailContent(ClientProjectConversationDetail detail) {
//        detailSection.requestDemandDetail(detail.getDemandId(), type);
        detailSection.requestDemandDetail(123L, type);

//        detailSection.requestSupplierDetail(detail.getSupplierId(), type);
        detailSection.requestSupplierDetail(142811L, type);

//        detailSection.requestContest(detail.getMessageId(),
//                detail.getUserMessageId(), Storage.getUser().getUserId());
        detailSection.requestConversation(124L, 289L, 149L);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    // Field Updaters
    public void addCheckHeaderUpdater() {
        view.getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {

            @Override
            public void update(Boolean value) {
                List<ClientProjectConversationDetail> rows = view.getConversationGrid().getVisibleItems();
                for (ClientProjectConversationDetail row : rows) {
                    ((MultiSelectionModel) view.getConversationGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getStarColumn().setFieldUpdater(new FieldUpdater<ClientProjectConversationDetail, Boolean>() {

            @Override
            public void update(int index, ClientProjectConversationDetail object, Boolean value) {
//              TableDisplay obj = (TableDisplay) object;
                object.setStarred(!value);
                view.getConversationGrid().redraw();
                Long[] item = new Long[]{object.getUserMessageId()};
                eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
            }
        });
    }

    public void addTextColumnFieldUpdaters() {
        FieldUpdater textFieldUpdater = new FieldUpdater<ClientProjectConversationDetail, String>() {

            @Override
            public void update(int index, ClientProjectConversationDetail object, String value) {
                if (lastOpenedProjectConversation != object.getUserMessageId()) {
                    lastOpenedProjectConversation = object.getUserMessageId();
                    object.setRead(true);
                    view.getConversationGrid().redraw();
                    displayDetailContent(object);
                }
            }
        };
        view.getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getBodyPreviewColumn().setFieldUpdater(textFieldUpdater);
        view.getDateColumn().setFieldUpdater(textFieldUpdater);
    }

    // Widget action handlers
    private void addActionChangeHandler() {
        view.getActions().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getActions().getSelectedIndex()) {
                    case 1:
                        eventBus.requestReadStatusUpdate(view.getSelectedIdList(), true);
                        break;
                    case 2:
                        eventBus.requestReadStatusUpdate(view.getSelectedIdList(), false);
                        break;
                    case 3:
                        eventBus.requestStarStatusUpdate(view.getSelectedIdList(), true);
                        break;
                    case 4:
                        eventBus.requestStarStatusUpdate(view.getSelectedIdList(), false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    // Buttons
    private void addReplyButtonHandler() {
        view.getReplyButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
            }
        });
    }

    //SelectionHandlers
    private void addDemandTableSelectionHandler() {
        view.getDemandGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Storage.setCurrentlyLoadedView(Constants.CLIENT_PROJECT_DISCUSSIONS);
                ClientProjectDetail selected = (ClientProjectDetail) ((SingleSelectionModel)
                        view.getDemandGrid().getSelectionModel()).getSelectedObject();
                if (selected != null) {
                    selected.setRead(true);
                    Storage.setDemandId(selected.getDemandId());
                    view.setDemandTitleLabel(selected.getDemandTitle());
                    view.setConversationTableVisible(true);
                    view.getConversationGrid().getDataCount(eventBus, null);
                }
            }
        });
    }
}