package cz.poptavka.sample.client.user.demands.widgets;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.messages.ReplyWindowPresenter;
import cz.poptavka.sample.client.user.messages.UserConversationPanel;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@Presenter(view = DetailWrapperView.class, multiple = true)
public class DetailWrapperPresenter extends
        LazyPresenter<DetailWrapperPresenter.IDetailWrapper, UserEventBus> {

    public interface IDetailWrapper extends LazyView {
        Widget getWidgetView();

        void setDetail(Widget demandDetailWidget);

        UserConversationPanel getConversationPanel();

        SimplePanel getReplyHolder();
    }

    private DetailType type;

    private ReplyWindowPresenter replyPresenter = null;

    private long messageId;

    /**
     * Initialize widget and sets his type.
     *
     * @param detailSection holder for widget
     * @param type type of view, where is this widget loaded
     */
    public void initDetailWrapper(SimplePanel detailSection, DetailType detailType) {
        GWT.log("DEMAND DETAIL Presenter LOADED");
        detailSection.setWidget(view.getWidgetView());
        this.type = detailType;
    }

    /**
     * Response when user click demand to see the details. DemandDetails widget,
     * past conversation regarding this demand and reply widget is created.
     *
     * @param detail DemandDetail to be displayed
     * @param typeOfDetail type of what detail section should handle this event
     */
    public void onSetDemandDetail(DemandDetail detail, DetailType typeOfDetail) {
        if (!typeOfDetail.equals(type)) {
            //event not for this instance of presenter
            return;
        }

        // initialization of parts of view

        //detail part
        // TODO selection if editable or not
        view.setDetail(new DemandDetailView(detail));

        // conversation part
        view.getConversationPanel();

        // reply part
        replyPresenter = eventBus.addHandler(ReplyWindowPresenter.class);
        replyPresenter.initReplyWindow(view.getReplyHolder(), messageId);

        // GUI visual event
        eventBus.loadingHide();
    }


    public void onSetPotentialDemandConversation(ArrayList<MessageDetail> messageList, DetailType wrapperhandlerType) {
        if (!wrapperhandlerType.equals(type)) {
            //event not for this instance of presenter
            GWT.log(view.getWidgetView().getClass().getName()
                    + " does NOT handle method onSetPotentialDemandConversation");
            return;
        }
        if (messageList == null) {
            GWT.log(" ** messageList is empty()");
            return;
        }
        view.getConversationPanel().setMessageList(messageList, true);
    }

    /**
     * Visual sign, that demand detail is loading.
     * @param demandId
     * @param typeOfDetail
     */
    public void onGetDemandDetail(Long demandId, DetailType typeOfDetail) {
        if (!typeOfDetail.equals(type)) {
            return;
        }
        Widget anchor = view.getWidgetView();
        eventBus.loadingShowWithAnchor("", anchor);
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

}
