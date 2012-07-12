/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsEventBus;
import com.eprovement.poptavka.client.user.widget.DevelDetailWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Presenter(view = ClientProjectsView.class)
public class ClientProjectsPresenter
        extends LazyPresenter<ClientProjectsPresenter.ClientProjectsLayoutInterface, ClientDemandsEventBus> {

    public interface ClientProjectsLayoutInterface extends LazyView, IsWidget {

        // Columns
        Column<ClientProjectDetail, Date> getFinnishDateColumn();

        Column<ClientProjectDetail, String> getPriceColumn();

        Column<ClientProjectDetail, String> getTitleColumn();

        Column<ClientProjectDetail, Date> getValidToDateColumn();

        Column<ClientProjectConversationDetail, Boolean> getStarColumn();

        // Buttons
        Button getReadBtn();

        Button getStarBtn();

        Button getUnreadBtn();

        Button getUnstarBtn();

        Button getBackBtn();

        // Others
        UniversalAsyncGrid<ClientProjectDetail> getDemandGrid();

        UniversalAsyncGrid<ClientProjectConversationDetail> getConversationGrid();

        int getDemandPageSize();

        int getConversationPageSize();

        List<Long> getSelectedIdList();

        Set<ClientProjectDetail> getSelectedMessageList();

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
    private DevelDetailWrapperPresenter detailSection = null;
    //remove this annotation for production
    @SuppressWarnings("unused")
    private boolean initialized = false;
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
        view.getDemandGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Storage.setCurrentlyLoadedView(Constants.DEMANDS_CLIENT_PROJECT_CONVERSATIONS);
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
        view.getConversationGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                ClientProjectConversationDetail selected = (ClientProjectConversationDetail) ((SingleSelectionModel)
                        view.getConversationGrid().getSelectionModel()).getSelectedObject();
                if (selected != null) {
                    if (lastOpenedProjectConversation != selected.getUserMessageId()) {
                        lastOpenedProjectConversation = selected.getUserMessageId();
                        selected.setRead(true);
                        //Nacitaj detaili
                    }
                }

            }
        });
        view.getBackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getDemandGrid().getSelectionModel().setSelected(
                        (ClientProjectDetail) ((SingleSelectionModel)
                        view.getDemandGrid().getSelectionModel()).getSelectedObject(), false);
                view.setConversationTableVisible(false);
//                view.getDemandGrid().flush();
//                view.getDemandGrid().redraw();
            }
        });
        view.getStarColumn().setFieldUpdater(new FieldUpdater<ClientProjectConversationDetail, Boolean>() {

            @Override
            public void update(int index, ClientProjectConversationDetail object, Boolean value) {
//              TableDisplay obj = (TableDisplay) object;
                object.setStarred(!value);
                view.getConversationGrid().redraw();
                Long[] item = new Long[]{object.getUserMessageId()};
//                    updateStarStatus(Arrays.asList(item), !value);
            }
        });
//        addTitleColumnFieldUpdater();
//        addPriceColumnFieldUpdater();
//        addFinnishDateColumnFieldUpdater();
//        addValidToDateColumnFieldUpdater();
        // Buttons Actions
        addReadButtonHandler();
        addUnreadButtonHandler();
        addStarButtonHandler();
        addUnstarButtonHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientProjects(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.DEMANDS_CLIENT_PROJECTS);
        searchDataHolder = filter;
        view.getDemandGrid().getDataCount(eventBus, searchDataHolder);
//        }
        view.getWidgetView().asWidget().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (detailSection == null) {
            detailSection = eventBus.addHandler(DevelDetailWrapperPresenter.class);
            detailSection.initDetailWrapper(view.getWrapperPanel(), type);
        }
        initialized = true;
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**
     * DEVEL METHOD
     *
     * Used for JRebel correct refresh. It is called from DemandModulePresenter, when removing instance of
     * SupplierListPresenter. it has to remove it's detailWrapper first.
     */
    public void develRemoveDetailWrapper() {
        detailSection.develRemoveReplyWidget();
        eventBus.removeHandler(detailSection);
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientsProjects(List<ClientProjectDetail> data) {
        GWT.log("++ onResponseClientsProjects");

        view.getDemandGrid().updateRowData(data);
    }

    public void onDisplayClientsProjectConversations(List<ClientProjectConversationDetail> data) {
        GWT.log("++ onResponseClientsProjects");

        view.getConversationGrid().updateRowData(data);
    }

    //call eventBus to update READ status
    //called in ClickEvent of action button.
    public void updateReadStatus(List<Long> selectedIdList, boolean newStatus) {
        eventBus.requestReadStatusUpdate(selectedIdList, newStatus);
    }

    /**
     * Triggered by action button: read/unread button
     * When changing read of multiple demands by action button. Visible change has to be done manually;
     *
     * @param isRead
     */
    /*
     private void internalReadUpdate(boolean isRead) {
     Iterator<PotentialDemandMessage> it = view.getSelectedMessageList().iterator();
     while (it.hasNext()) {
     PotentialDemandMessage message = it.next();
     message.setRead(isRead);
     }
     view.getDataProvider().refresh();
     view.getGrid().redraw();
     }
     */
    /**
     * Call eventBus to update STARRED status.
     * T
     * his method is called by clicking star image on single demand by default. Also is called in ClickEvent of
     * action button.
     * @param list
     * @param newStatus
     */
    public void updateStarStatus(List<Long> list, boolean newStatus) {
        eventBus.requestStarStatusUpdate(list, newStatus);
    }

    /**
     * Triggered by action button: star/unstar buttons
     * when starring multiple demands by action button. Visible change has to be done manually;
     *
     * @param isStared
     */
    /*
     private void internalStarUpdate(boolean isStared) {
     Iterator<PotentialDemandMessage> it = view.getSelectedMessageList().iterator();
     while (it.hasNext()) {
     PotentialDemandMessage message = it.next();
     message.setStarred(isStared);
     }
     view.getDataProvider().refresh();
     view.getGrid().redraw();
     }
     */
    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related conversation
     * @param userMessageId ID for demand related conversation
     */
    public void displayDetailContent(Long demandId, long messageId, Long userMessageId) {
        //TODO
        //copy role check from old implementation
        //
        //

        //can be solved by enum in future or can be accesed from storage class
        detailSection.showLoading(DevelDetailWrapperPresenter.DETAIL);
        eventBus.requestDemandDetail(demandId, type);

        //add conversation loading events and so on
        detailSection.showLoading(DevelDetailWrapperPresenter.CHAT);
        eventBus.requestChatForSupplierList(messageId, userMessageId, Storage.getUser().getUserId());

        //init default replyWidget
        //it is initalized now, because we do not need to have it visible before first demand selection
        detailSection.initReplyWidget();
    }

    public void onSendMessageResponse(MessageDetail sentMessage, ViewType handlingType) {
        //neccessary check for method to be executed only in appropriate presenter
        if (type.equals(handlingType)) {
            detailSection.addConversationMessage(sentMessage);
        }
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    // Field Updaters
    public void addTitleColumnFieldUpdater() {
        view.getTitleColumn().setFieldUpdater(new FieldUpdater<ClientProjectDetail, String>() {

            @Override
            public void update(int index, ClientProjectDetail object, String value) {
//                TableDisplay obj = (TableDisplay) object;
//                obj.setRead(true);
//                dataGrid.redraw();
//presenter.displayDetailContent(object.getDemandId(),object.getMessageId(), object.getUserMessageId());
            }
        });
    }

    public void addPriceColumnFieldUpdater() {
        view.getPriceColumn().setFieldUpdater(new FieldUpdater<ClientProjectDetail, String>() {

            @Override
            public void update(int index, ClientProjectDetail object, String value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    public void addFinnishDateColumnFieldUpdater() {
        view.getFinnishDateColumn().setFieldUpdater(new FieldUpdater<ClientProjectDetail, Date>() {

            @Override
            public void update(int index, ClientProjectDetail object, Date value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

    }

    public void addValidToDateColumnFieldUpdater() {
        view.getValidToDateColumn().setFieldUpdater(new FieldUpdater<ClientProjectDetail, Date>() {

            @Override
            public void update(int index, ClientProjectDetail object, Date value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    // Buttons

    private void addReadButtonHandler() {
        view.getReadBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                internalReadUpdate(true);
                updateReadStatus(view.getSelectedIdList(), true);
            }
        });
    }

    private void addUnreadButtonHandler() {
        view.getUnreadBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                internalReadUpdate(false);
                updateReadStatus(view.getSelectedIdList(), false);
            }
        });
    }

    private void addStarButtonHandler() {
        view.getStarBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                internalStarUpdate(true);
                updateStarStatus(view.getSelectedIdList(), true);
            }
        });

    }

    private void addUnstarButtonHandler() {
        view.getUnstarBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                internalStarUpdate(false);
                updateStarStatus(view.getSelectedIdList(), false);
            }
        });
    }
}