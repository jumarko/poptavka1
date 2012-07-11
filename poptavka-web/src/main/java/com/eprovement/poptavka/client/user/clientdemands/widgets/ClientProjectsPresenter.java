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
import com.eprovement.poptavka.shared.domain.demandsModule.ClientDemandDetail;
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
        Column<ClientDemandDetail, Date> getFinnishDateColumn();

        Column<ClientDemandDetail, String> getPriceColumn();

        Column<ClientDemandDetail, String> getTitleColumn();

        Column<ClientDemandDetail, Date> getValidToDateColumn();

        // Buttons
        Button getReadBtn();

        Button getStarBtn();

        Button getUnreadBtn();

        Button getUnstarBtn();

        // Others
        UniversalAsyncGrid<ClientDemandDetail> getDemandGrid();

        int getPageSize();

        List<Long> getSelectedIdList();

        Set<ClientDemandDetail> getSelectedMessageList();

        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();
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
        addTitleColumnFieldUpdater();
        addPriceColumnFieldUpdater();
        addFinnishDateColumnFieldUpdater();
        addValidToDateColumnFieldUpdater();
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
        Storage.setCurrentlyLoadedView(Constants.DEMANDS_CLIENT_MY_DEMANDS);
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
    public void onResponseClientsProjects(List<ClientDemandDetail> data) {
        GWT.log("++ onResponseClientsProjects");

        view.getDemandGrid().updateRowData(data);
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
        view.getTitleColumn().setFieldUpdater(new FieldUpdater<ClientDemandDetail, String>() {

            @Override
            public void update(int index, ClientDemandDetail object, String value) {
//                TableDisplay obj = (TableDisplay) object;
//                obj.setRead(true);
//                dataGrid.redraw();
//presenter.displayDetailContent(object.getDemandId(),object.getMessageId(), object.getUserMessageId());
            }
        });
    }

    public void addPriceColumnFieldUpdater() {
        view.getPriceColumn().setFieldUpdater(new FieldUpdater<ClientDemandDetail, String>() {

            @Override
            public void update(int index, ClientDemandDetail object, String value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    public void addFinnishDateColumnFieldUpdater() {
        view.getFinnishDateColumn().setFieldUpdater(new FieldUpdater<ClientDemandDetail, Date>() {

            @Override
            public void update(int index, ClientDemandDetail object, Date value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

    }

    public void addValidToDateColumnFieldUpdater() {
        view.getValidToDateColumn().setFieldUpdater(new FieldUpdater<ClientDemandDetail, Date>() {

            @Override
            public void update(int index, ClientDemandDetail object, Date value) {
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