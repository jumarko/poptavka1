package com.eprovement.poptavka.client.user.admin.detail;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.LinkedList;
import java.util.List;

@Presenter(view = AdminDetailsWrapperView.class, multiple = true)
public class AdminDetailsWrapperPresenter
        extends LazyPresenter<AdminDetailsWrapperPresenter.IDetailWrapper, AdminEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Constants - TabPanel indexes. **/
    public static final int DEMAND_DETAIL_TAB = 0;
    public static final int CONVERSATION_TAB = 1;
    /** Class Attributes. **/
    private long demandId = -1;
    private long threadRootId = -1;

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface IDetailWrapper extends LazyView {

        Widget getWidgetView();

        TabLayoutPanel getContainer();

        DemandDetailView getDemandDetail();

        AdminOfferQuestionWindow getReplyHolder();

        CellList getMessagePanel();

        ListDataProvider getMessageProvider();

        HTMLPanel getConversationHolder();

        void loadingDivShow(Widget holderWidget);

        void loadingDivHide(Widget holderWidget);
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getReplyHolder().getSubmitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //Is message we want to send valid?
                if (view.getReplyHolder().isValid()) {
                    MessageDetail questionMessageToSend =
                            view.getReplyHolder().updateSendingMessage(
                            view.getReplyHolder().getCreatedMessage());
                    questionMessageToSend.setSenderId(Storage.getUser().getUserId());
                    eventBus.requestSendAdminMessage(questionMessageToSend);
                }
            }
        });
        view.getContainer().addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                requestActualTabData();
            }
        });
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Initialize widget and sets his type.
     *
     * @param detailSection
     *            holder for widget
     * @param type
     *            type of view, where is this widget loaded
     */
    public void initDetailWrapper(SimplePanel detailSection) {
        detailSection.setWidget(view.getWidgetView());
    }

    /**
     * Store needed ids for later data retrieving and hides User tab. Data are not retrieved immediately,
     * but only for selected tab, therefore we need to remember them in case, user,
     * changes tab.
     *
     * @param demandId
     * @param threadRootId
     */
    public void initDetails(long demandId, long threadRootId) {
        reset();
        this.demandId = demandId;
        this.threadRootId = threadRootId;
        view.getContainer().selectTab(CONVERSATION_TAB, false);
        requestActualTabData();
    }

    /**
     * Store needed ids for later data retrieving and hides User and Conversation tab.
     * Data are not retrieved immediately, but only for selected tab,
     * therefore we need to remember them in case, user, changes tab.
     *
     * @param demandId
     * @param supplierId
     * @param threadRootId
     */
    public void initDetails(long demandId) {
        reset();
        view.getContainer().getTabWidget(CONVERSATION_TAB).getParent().setVisible(false);
        this.demandId = demandId;
        view.getContainer().selectTab(DEMAND_DETAIL_TAB, false);
        requestActualTabData();
    }

    public void requestActualTabData() {
        switch (view.getContainer().getSelectedIndex()) {
            case DEMAND_DETAIL_TAB:
                requestDemandDetail(demandId);
                break;
            case CONVERSATION_TAB:
                requestConversation(threadRootId);
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    /**
     * Set all tabs visible and clear tabs' widgets' attributes.
     */
    public void reset() {
        setAllTabVisibility(true);
        clear();
    }

    /**************************************************************************/
    /* Request methods                                                        */
    /**************************************************************************/
    /**
     * Request demand detail for detail section tab: Demand.
     *
     * @param demandId
     */
    public void requestDemandDetail(Long demandId) {
        view.loadingDivShow(view.getDemandDetail());
        eventBus.requestDemandDetail(demandId);
    }

    /**
     * Request conversation messages for detail section tab: Conversations.
     * Loads conversation between client and supplier. Each conversation begins
     * with threadRoot message that must be passed here as a parameter.
     *
     * @param threadRootId - root message i.e. first demand message in the conversation always created by client
     * @param userId - user who's chatting messages we are going to retrieve
     */
    public void requestConversation(long threadRootId) {
        view.loadingDivShow(view.getConversationHolder());
        eventBus.requestConversation(threadRootId);
    }

    /**************************************************************************/
    /* Response methods                                                       */
    /**************************************************************************/
    /**
     * Response method for fetching demandDetail.
     *
     * @param demandDetail detail to be displayed
     */
    public void onResponseDemandDetail(FullDemandDetail demandDetail) {
        view.getDemandDetail().setDemanDetail(demandDetail);
        view.loadingDivHide(view.getDemandDetail());
    }

    /**
     * Response method for fetching demand-related chat.
     *
     * @param chatMessages - demand-related conversation
     */
    public void onResponseConversation(List<MessageDetail> chatMessages) {
        setMessageList(new LinkedList<MessageDetail>(chatMessages), true);
        view.loadingDivHide(view.getConversationHolder());
    }

    /**************************************************************************/
    /* HELPER methods                                                         */
    /**************************************************************************/
    /**
     * Set all tabs' visibility.
     * @param visible - true - all tabs will be visible, false elsewhere
     */
    private void setAllTabVisibility(boolean visible) {
        view.getContainer().getTabWidget(DEMAND_DETAIL_TAB).getParent().setVisible(visible);
        view.getContainer().getTabWidget(CONVERSATION_TAB).getParent().setVisible(visible);
    }

    /**
     * Clear detail section means reseting widget's attributes of all tabs widgets.
     */
    private void clear() {
        view.getDemandDetail().clear();
        view.getMessageProvider().getList().clear();
        view.getReplyHolder().clear();
    }

    /**************************************************************************/
    /* Conversation Methods                                                   */
    /**************************************************************************/
    /**
     * Display list of messages. Whole conversation panel consists of cellList and
     * OfferQuestionWidget. If messages count is n, then cellList displays (0,n-1) messages,
     * and OfferQuestionWidget displays (n-1,n) message with control panels - for
     * providing question and offer form. This way can be order of those two elements
     * be changed without any other changes, expect setting right messages ordering on backend.
     * Message List size is at least always 1.
     *
     * @param messages list of messages to be displayed
     */
    public void setMessageList(LinkedList<MessageDetail> messages, boolean collapsed) {
        view.getReplyHolder().setMessage(messages.removeFirst());
        view.getMessageProvider().setList(messages);
    }

    /**
     * Sent message is displayed and reply window is enabled again.
     *
     * @param sentMessage
     */
    public void onResponseSendAdminMessage(MessageDetail sentMessage) {
        view.getMessageProvider().getList().add(view.getReplyHolder().getMessage());
        view.getReplyHolder().setMessage(sentMessage);
        view.getReplyHolder().setDefaultStyle();
    }
}
