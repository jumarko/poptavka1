package com.eprovement.poptavka.client.user.demands.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.demands.DemandEventBus;
import com.eprovement.poptavka.client.user.widget.DevelDetailWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;


@Presenter(view = ClientList.class)
public class ClientListPresenter extends LazyPresenter<ClientListPresenter.IList, DemandEventBus> {

    public interface IList extends LazyView {
        Widget getWidgetView();

        //control buttons getters
        Button getReadBtn();

        Button getUnreadBtn();

        Button getStarBtn();

        Button getUnstarBtn();

        //table getters
        UniversalGrid<ClientProjectDetail> getGrid();

        ListDataProvider<ClientProjectDetail> getDataProvider();

        List<Long> getSelectedIdList();

        Set<ClientProjectDetail> getSelectedMessageList();

        //detail wrapper
        SimplePanel getWrapperPanel();
    }

    //viewType
    private ViewType type = ViewType.EDITABLE;

    private DevelDetailWrapperPresenter detailSection = null;


    //remove this annotation for production
    @SuppressWarnings("unused")
    private boolean initialized = false;
    private SearchModuleDataHolder searchDataHolder;

    /** Defines button actions. */
    public void bindView() {
        view.getReadBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                internalReadUpdate(true);
                updateReadStatus(view.getSelectedIdList(), true);
            }
        });
        view.getUnreadBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                internalReadUpdate(false);
                updateReadStatus(view.getSelectedIdList(), false);
            }
        });
        view.getStarBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                internalStarUpdate(true);
                updateStarStatus(view.getSelectedIdList(), true);
            }
        });
        view.getUnstarBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                internalStarUpdate(false);
                updateStarStatus(view.getSelectedIdList(), false);
            }
        });
    }


    /**
     * Init view and fetch new supplier's demands. Demand request
     * is sent ONLY for the first time - when view is loaded.
     *
     * Associated DetailWrapper widget is created and initialized.
     */
    public void onInitClientList(SearchModuleDataHolder searchModuleDataHolder) {
//        commented code is from production code
//        if (!initialized) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_PROJECTS);
        searchDataHolder = searchModuleDataHolder;
        eventBus.requestClientsDemands(searchDataHolder);
//        }
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (detailSection == null) {
            detailSection = eventBus.addHandler(DevelDetailWrapperPresenter.class);
            detailSection.initDetailWrapper(view.getWrapperPanel(), type);
        }
        initialized = true;
    }

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
    public void onResponseClientsDemands(ArrayList<ClientProjectDetail> data) {
        GWT.log("++ onResponseClientsDemands");

        if (data.size() > 0) {
            List<ClientProjectDetail> list = view.getDataProvider().getList();
            list.clear();
            for (ClientProjectDetail d : data) {
                list.add(d);
            }
            view.getDataProvider().refresh();
        } else {
            view.getDataProvider().getList().clear();
            view.getGrid().displayEmptyTable();
        }
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
        detailSection.showLoading(DevelDetailWrapperPresenter.DEMAND);
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


}
