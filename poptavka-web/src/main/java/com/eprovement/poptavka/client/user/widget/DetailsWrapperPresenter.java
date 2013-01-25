package com.eprovement.poptavka.client.user.widget;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.detail.EditableDemandDetailPresenter;
import com.eprovement.poptavka.client.user.widget.detail.EditableDemandDetailView;
import com.eprovement.poptavka.client.user.widget.detail.SupplierDetailView;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.client.user.widget.messaging.OfferQuestionWindow;
import com.eprovement.poptavka.client.user.widget.messaging.ConversationPanel;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;
import java.util.Set;

@Presenter(view = DetailsWrapperView.class, multiple = true)
public class DetailsWrapperPresenter
        extends LazyPresenter<DetailsWrapperPresenter.IDetailWrapper, RootEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Constants - TabPanel indexes. **/
    public static final int DEMAND_DETAIL_TAB = 0;
    public static final int USER_DETAIL_TAB = 3;
    public static final int CONVERSATION_TAB = 4;
    /** Class Attributes. **/
    private UniversalTableGrid table = null;
    private EditableDemandDetailPresenter editDemandPresenter = null;
    private long demandId = -1;
    private long clientOrSupplierId = -1;
    private long threadRootId = -1;

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface IDetailWrapper extends LazyView {

        Widget getWidgetView();

        TabLayoutPanel getContainer();

//        DemandDetailView getDemandDetail();
        SimplePanel getDemandDetailHolder();

        SupplierDetailView getSupplierDetail();

        ConversationPanel getConversationPanel();

        OfferQuestionWindow getReplyHolder();

        HTMLPanel getConversationHolder();

        Label getUserDetailTabHeaderLabel();

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
                //is message to send valid
                if (view.getReplyHolder().isValid()) {
                    // distinguish what kind of message should be sent
                    //Kedze reply widget je robeny tak, ze najprv sa urci ci chcem poslat spravu alebo ponuku
                    //zobrazi sa podla toho widget - budna poslanie spravy alebo na poslanie ponuky.
                    //Potom samotne poslanie sa vykona kliknutim na tlacidlo Submit, teda musime zistit co
                    //za akciu ma ten submit vykonat, teda ci poslat spravu alebo ponuku, podla skor zvolenej akcie.
                    switch (view.getReplyHolder().getSelectedResponse()) {
                        case OfferQuestionWindow.RESPONSE_QUESTION:
                            MessageDetail questionMessageToSend =
                                    view.getConversationPanel().updateSendingMessage(
                                    view.getReplyHolder().getCreatedMessage());
                            questionMessageToSend.setSenderId(Storage.getUser().getUserId());
                            eventBus.sendQuestionMessage(questionMessageToSend);
                            break;
                        case OfferQuestionWindow.RESPONSE_OFFER:
                            OfferMessageDetail offerMessageToSend =
                                    view.getConversationPanel().updateSendingOfferMessage(
                                    view.getReplyHolder().getCreatedOfferMessage());
                            offerMessageToSend.setSenderId(Storage.getUser().getUserId());
                            eventBus.sendOfferMessage(offerMessageToSend);
                            break;
                        default:
                            break;
                    }
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
    public void initDetailWrapper(UniversalTableGrid grid, SimplePanel detailSection) {
        detailSection.setWidget(view.getWidgetView());
        view.getContainer().selectTab(CONVERSATION_TAB, false);
        this.table = grid;
    }

    /**
     * Store needed ids for later data retrieving. Data are not retrieved immediately,
     * but only for selected tab, therefore we need to remember them in case, user,
     * changes tab.
     *
     * @param demandId
     * @param clientOrSupplierId
     * @param threadRootId
     */
    public void initDetails(long demandId, long clientOrSupplierId, long threadRootId) {
        clear();
        this.demandId = demandId;
        this.clientOrSupplierId = clientOrSupplierId;
        this.threadRootId = threadRootId;
        requestActualTabData();
    }

    public void requestActualTabData() {
        switch (view.getContainer().getSelectedIndex()) {
            case DEMAND_DETAIL_TAB:
                requestDemandDetail(demandId);
                break;
            case USER_DETAIL_TAB:
                if (Constants.getClientDemandsConstants().contains(Storage.getCurrentlyLoadedView())) {
                    requestClientDetail(clientOrSupplierId);
                } else {
                    requestSupplierDetail(clientOrSupplierId);
                }
                break;
            case CONVERSATION_TAB:
                requestConversation(threadRootId, Storage.getUser().getUserId());
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    /**
     * Sent message is displayed and reply window is enabled again.
     *
     * @param sentMessage
     */
    public void onAddConversationMessage(MessageDetail sentMessage) {
        view.getConversationPanel().addMessage(sentMessage);
        view.getReplyHolder().setDefaultStyle();
        //Always will be only one item.
        for (IUniversalDetail detail : (Set<IUniversalDetail>) table.getSelectionModel().getSelectedSet()) {
            detail.setUserMessageId(sentMessage.getUserMessageId());
            detail.setMessageCount(detail.getMessageCount() + 1);
        }
        table.redraw();
    }

    /**************************************************************************/
    /* Request methods                                                        */
    /**************************************************************************/
    //Radej takto, ako mat v kazdom eventbuse forward metody.
    /*
     * Request/Response Method pair
     * DemandDetail for detail section
     * @param demandId
     * @param type
     */
    public void requestDemandDetail(Long demandId) {
        if (view.getContainer().getWidget(DEMAND_DETAIL_TAB).getParent().isVisible()) {
            view.loadingDivShow(view.getDemandDetailHolder());
        }
        if (view.getContainer().getWidget(DEMAND_DETAIL_TAB).getParent().isVisible()) {
            view.loadingDivShow(view.getDemandDetailHolder());
        }
        eventBus.requestDemandDetail(demandId);
    }

    public void requestClientDetail(Long clientId) {
        view.loadingDivShow(view.getSupplierDetail());
        eventBus.requestClientDetail(clientId);
    }

    public void requestSupplierDetail(Long supplierId) {
        view.loadingDivShow(view.getSupplierDetail());
        eventBus.requestSupplierDetail(supplierId);
    }

    /**
     * Loads conversation between client and supplier. Each conversation begins with threadRoot message that must be
     * passed here as a parameter.
     *
     * @param threadRootId - root message i.e. first demand message in the conversation always created by client
     * @param userId - user who's chatting messages we are going to retrieve
     */
    public void requestConversation(long threadRootId, long userId) {
        view.loadingDivShow(view.getConversationHolder());
        eventBus.requestConversation(threadRootId, userId);
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
        if (Storage.getCurrentlyLoadedView() == Constants.CLIENT_DEMANDS
                || Storage.getCurrentlyLoadedView() == Constants.CLIENT_DEMAND_DISCUSSIONS) {
            if (editDemandPresenter != null) {
                eventBus.removeHandler(editDemandPresenter);
            }
            editDemandPresenter = eventBus.addHandler(EditableDemandDetailPresenter.class);
            view.getDemandDetailHolder().setWidget(editDemandPresenter.getView());
            ((EditableDemandDetailView) editDemandPresenter.getView()).setDemanDetail(demandDetail);
            view.loadingDivHide(view.getDemandDetailHolder());
        } else {
            DemandDetailView demandDetailWidget = new DemandDetailView();
            demandDetailWidget.setDemanDetail(demandDetail);
            view.getDemandDetailHolder().setWidget(demandDetailWidget);
            view.loadingDivHide(view.getDemandDetailHolder());
        }
    }

    /**
     * Response method for fetching clientDetail.
     *
     * @param clientDetail to be displayed
     */
    public void onResponseClientDetail(FullClientDetail clientDetail) {
        view.getSupplierDetail().setSupplierDetail(clientDetail);
        view.loadingDivHide(view.getSupplierDetail());
    }

    /**
     * Response method for fetching supplierDetail.
     *
     * @param supplierDetail detail to be displayed
     */
    public void onResponseSupplierDetail(FullSupplierDetail supplierDetail) {
        view.getSupplierDetail().setSupplierDetail(supplierDetail);
        view.loadingDivHide(view.getSupplierDetail());
    }

    /**
     * Response method for fetching demand-related chat.
     * Internally creates clickHandler for ReplyWidgets submit button.
     *
     * @param chatMessages - demand-related conversation
     * @param supplierListType
     */
    public void onResponseConversation(List<MessageDetail> chatMessages) {
        view.getConversationPanel().setMessageList(chatMessages, true);
        //bind messages to handler that updates read status
        setMessageReadHandler();
        view.loadingDivHide(view.getConversationHolder());
    }

    /**************************************************************************/
    /* HELPER methods                                                         */
    /**************************************************************************/
    private void setMessageReadHandler() {
        //TODO RELEASE: refactor migth not be needed because all messages are set to read when displaying conversation
//        final ChangeHandler click = new ChangeHandler() {
//            @Override
//            public void onChange(ChangeEvent event) {
//                String messagId = ((TextBox) event.getSource()).getText();
//                eventBus.requestReadStatusUpdate(Arrays.asList(Long.valueOf(messagId)), true);
//                table.refresh();
//            }
//        };
//        for (int i = 0; i < view.getConversationPanel().getMessagePanel().getWidgetCount(); i++) {
//            ((SimpleMessageWindow) view.getConversationPanel().getMessagePanel().getWidget(i))
//                    .getUpdateRead().addChangeHandler(click);
//        }
    }

    /**************************************************************************/
    /* HELPER methods                                                         */
    /**************************************************************************/
    public void clear() {
        if (Storage.getCurrentlyLoadedView() == Constants.CLIENT_DEMANDS
                || Storage.getCurrentlyLoadedView() == Constants.CLIENT_DEMAND_DISCUSSIONS) {
            ((EditableDemandDetailView) view.getDemandDetailHolder().getWidget()).clear();
        } else {
            ((DemandDetailView) view.getDemandDetailHolder().getWidget()).clear();
        }
        view.getSupplierDetail().clear();
        view.getConversationPanel().clear();
    }
}
