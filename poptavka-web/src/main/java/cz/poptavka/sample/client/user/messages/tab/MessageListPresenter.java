package cz.poptavka.sample.client.user.messages.tab;


import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.user.messages.MessagesModuleEventBus;
import cz.poptavka.sample.client.user.widget.DevelDetailWrapperPresenter;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;
import java.util.HashMap;
import java.util.Map;

//@Presenter(view = SupplierList.class)
@Presenter(view = MessageList.class, multiple = true)
public class MessageListPresenter extends LazyPresenter<MessageListPresenter.IListM, MessagesModuleEventBus> {

    public interface IListM extends LazyView {

        Widget getWidgetView();

        //control buttons getters
        Button getReadBtn();

        Button getUnreadBtn();

        Button getStarBtn();

        Button getUnstarBtn();

        //table getters
        MessageListGrid<UserMessageDetail> getGrid();

        ListDataProvider<UserMessageDetail> getDataProvider();

        List<Long> getSelectedIdList();

        Set<UserMessageDetail> getSelectedMessageList();

        //detail wrapper
        SimplePanel getWrapperPanel();
    }
    //viewType
    private ViewType type = ViewType.POTENTIAL;
    private ConversationWrapperPresenter detailSection = null;
    //remove this annotation for production
    @SuppressWarnings("unused")
    private boolean initialized = false;

    /** Defines button actions. */
    public void bindView() {
        view.getReadBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                internalReadUpdate(true);
                updateReadStatus(view.getSelectedIdList(), true);
            }
        });
        view.getUnreadBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                internalReadUpdate(false);
                updateReadStatus(view.getSelectedIdList(), false);
            }
        });
        view.getStarBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                internalStarUpdate(true);
                updateStarStatus(view.getSelectedIdList(), true);
            }
        });
        view.getUnstarBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                internalStarUpdate(false);
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
    private void init() {
//        commented code is from production code
//        if (!initialized) {
//        eventBus.requestSupplierNewDemands();
//        }
//        orderColumns.clear();
//        orderColumns.put("message.created", OrderType.ASC);
//
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (detailSection == null) {
            detailSection = eventBus.addHandler(ConversationWrapperPresenter.class);
            detailSection.initDetailWrapper(view.getWrapperPanel(), type);
        }
        initialized = true;
    }
    private final Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();

    public void onInitInbox() {
        this.init();
        eventBus.getInboxMessages(Storage.getUser().getUserId());
    }

    public void onInitSent() {
        this.init();
        eventBus.getSentMessages(Storage.getUser().getUserId());
    }

    public void onInitTrash() {
        this.init();
        eventBus.getDeletedMessages(Storage.getUser().getUserId());
    }

    public void onDisplayMessages(List<UserMessageDetail> messages) {
        List<UserMessageDetail> list = view.getDataProvider().getList();
        list.clear();
        for (UserMessageDetail d : messages) {
            list.add(d);
        }

        view.getDataProvider().refresh();
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
//    public void onResponseSupplierNewDemands(ArrayList<UserMessageDetail> data) {
//        GWT.log("++ onResponseSupplierNewDemands");
//
//        List<UserMessageDetail> list = view.getDataProvider().getList();
//        list.clear();
//        for (UserMessageDetail d : data) {
//            list.add(d);
//        }
//        view.getDataProvider().refresh();
//    }
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
    private void internalReadUpdate(boolean isRead) {
        Iterator<UserMessageDetail> it = view.getSelectedMessageList().iterator();
        while (it.hasNext()) {
            UserMessageDetail message = it.next();
            message.setRead(isRead);
        }
        view.getDataProvider().refresh();
        view.getGrid().redraw();
    }

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
    private void internalStarUpdate(boolean isStared) {
        Iterator<UserMessageDetail> it = view.getSelectedMessageList().iterator();
        while (it.hasNext()) {
            UserMessageDetail message = it.next();
            message.setStarred(isStared);
        }
        view.getDataProvider().refresh();
        view.getGrid().redraw();
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related conversation
     * @param userMessageId ID for demand related conversation
     */
    public void displayConversation(Long threadRootId, Long subRootId) {
        //TODO
        //copy role check from old implementation
        //
        //

        //can be solved by enum in future or can be accesed from storage class
//        detailSection.showLoading(DevelDetailWrapperPresenter.DETAIL);
//        eventBus.requestDemandDetail(demandId, type);

        //add conversation loading events and so on
        detailSection.showLoading(DevelDetailWrapperPresenter.CHAT);
        eventBus.requestConversation(threadRootId, subRootId);

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
