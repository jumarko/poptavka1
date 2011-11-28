package cz.poptavka.sample.client.user.demands.develmodule.s.list;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.user.demands.widget.ClickableDiv;
import cz.poptavka.sample.client.user.demands.widget.DemandDetailView;
import cz.poptavka.sample.client.user.messages.widget.UserConversationPanel;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;

public class DevelDetailWrapperView extends Composite
    implements DevelDetailWrapperPresenter.IDetailWrapper {

    private static DetailWrapperViewUiBinder uiBinder = GWT.create(DetailWrapperViewUiBinder.class);
    interface DetailWrapperViewUiBinder extends UiBinder<Widget, DevelDetailWrapperView> {   }

    @UiField HTMLPanel container;
    @UiField ClickableDiv demandHeader, conversationHeader;
    @UiField DemandDetailView demandDetail;
    @UiField UserConversationPanel conversationPanel;
    @UiField SimplePanel replyHolder;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        demandHeader.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                demandHeader.toggleOpen();
                demandDetail.toggleVisible();
            }
        });
        conversationHeader.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                conversationHeader.toggleOpen();
                conversationPanel.setVisible(!conversationPanel.isVisible());
            }
        });
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setDetail(FullDemandDetail demand) {
        demandDetail.setDemanDetail(demand);
        demandHeader.toggleLoading();
    }

    @Override
    public UserConversationPanel getConversationPanel() {
        return conversationPanel;
    }

    @Override
    public SimplePanel getReplyHolder() {
        return replyHolder;
    }

    @Override
    public void setChat(ArrayList<MessageDetail> chatMessages) {
        conversationPanel.setMessageList(chatMessages, false);
        conversationHeader.toggleLoading();
    }

    @Override
    public void toggleDemandLoading() {
        //toggle actual visible state
        demandHeader.toggleLoading();
    }

    @Override
    public void toggleConversationLoading() {
        //toggle actual visible state
        conversationHeader.toggleLoading();
    }

}
