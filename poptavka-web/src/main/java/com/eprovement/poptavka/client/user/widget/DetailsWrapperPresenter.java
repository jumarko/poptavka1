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
import com.eprovement.poptavka.client.user.widget.messaging.DevelOfferQuestionWindow;
import com.eprovement.poptavka.client.user.widget.messaging.UserConversationPanel2;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
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
    /** Constants. **/
    public static final int EDITABLE_DEMAND = 0;
    public static final int DEMAND = 1;
    public static final int SUPPLIER = 2;
    public static final int CHAT = 3;
    /** Class Attributes. **/
    private UniversalTableGrid table = null;
    private EditableDemandDetailPresenter editDemandPresenter = null;

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface IDetailWrapper extends LazyView {

        Widget getWidgetView();

        TabLayoutPanel getContainer();

        DemandDetailView getDemandDetail();

        SimplePanel getEditableDemandDetail();

        SupplierDetailView getSupplierDetail();

        UserConversationPanel2 getConversationPanel();

        DevelOfferQuestionWindow getReplyHolder();

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
                //is message to send valid
                if (view.getReplyHolder().isValid()) {
                    // distinguish what kind of message should be sent
                    //Kedze reply widget je robeny tak, ze najprv sa urci ci chcem poslat spravu alebo ponuku
                    //zobrazi sa podla toho widget - budna poslanie spravy alebo na poslanie ponuky.
                    //Potom samotne poslanie sa vykona kliknutim na tlacidlo Submit, teda musime zistit co
                    //za akciu ma ten submit vykonat, teda ci poslat spravu alebo ponuku, podla skor zvolenej akcie.
                    switch (view.getReplyHolder().getSelectedResponse()) {
                        case DevelOfferQuestionWindow.RESPONSE_QUESTION:
                            MessageDetail questionMessageToSend =
                                    view.getConversationPanel().updateSendingMessage(
                                    view.getReplyHolder().getCreatedMessage());
                            questionMessageToSend.setSenderId(Storage.getUser().getUserId());
                            eventBus.sendQuestionMessage(questionMessageToSend);
                            break;
                        case DevelOfferQuestionWindow.RESPONSE_OFFER:
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
        if (Storage.getCurrentlyLoadedView() == Constants.CLIENT_DEMANDS) {
            setTabVisibility(EDITABLE_DEMAND, true);
            setTabVisibility(DEMAND, false);
        } else {
            setTabVisibility(EDITABLE_DEMAND, false);
            setTabVisibility(DEMAND, true);
        }
        view.getContainer().selectTab(CHAT);
        this.table = grid;
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
        view.getReplyHolder().setNormalStyle();
        //Always will be only one item.
        for (IUniversalDetail detail : (Set<IUniversalDetail>) table.getSelectionModel().getSelectedSet()) {
            detail.setUserMessageId(sentMessage.getUserMessageId());
            detail.setMessageCount(detail.getMessageCount() + 1);
        }
        table.redraw();
    }

    /**************************************************************************/
    /* This methods                                                           */
    /**************************************************************************/
    public void setTabVisibility(int column, boolean visible) {
        view.getContainer().getTabWidget(column).getParent().setVisible(visible);
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
        if (view.getContainer().getWidget(DEMAND).getParent().isVisible()) {
            view.loadingDivShow(view.getDemandDetail());
        }
        if (view.getContainer().getWidget(DEMAND).getParent().isVisible()) {
            view.loadingDivShow(view.getEditableDemandDetail());
        }
        eventBus.requestDemandDetail(demandId);
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
     * @param userMessageId - userMessageId
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
        if (view.getContainer().getWidget(DEMAND).getParent().isVisible()) {
            view.getDemandDetail().setDemanDetail(demandDetail);
            view.loadingDivHide(view.getDemandDetail());
        }
        if (view.getContainer().getWidget(EDITABLE_DEMAND).getParent().isVisible()) {
            if (editDemandPresenter != null) {
                eventBus.removeHandler(editDemandPresenter);
            }
            editDemandPresenter = eventBus.addHandler(EditableDemandDetailPresenter.class);
            view.getEditableDemandDetail().setWidget(editDemandPresenter.getView());
            ((EditableDemandDetailView) editDemandPresenter.getView()).setDemanDetail(demandDetail);
            view.loadingDivHide(view.getEditableDemandDetail());
        }
    }

    /**
     * Response method for fetching supplierDetail.
     *
     * @param demandDetail detail to be displayed
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
        view.getEditableDemandDetail().clear();
        view.getDemandDetail().clear();
        view.getSupplierDetail().clear();
        view.getConversationPanel().clear();
    }
}
