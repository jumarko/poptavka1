package cz.poptavka.sample.client.user.demands.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.user.messages.UserConversationPanel;

public class DetailWrapperView extends Composite
    implements DetailWrapperPresenter.IDetailWrapper {

    private static DetailWrapperViewUiBinder uiBinder = GWT.create(DetailWrapperViewUiBinder.class);
    interface DetailWrapperViewUiBinder extends UiBinder<Widget, DetailWrapperView> {   }

    @UiField SimplePanel detailHolder;
    @UiField UserConversationPanel conversationPanel;
    @UiField SimplePanel replyHolder;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        GWT.log("DEMAND DETAIL view LOADED");
        GWT.log("Is there a conversation panel: " + (conversationPanel == null));

    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setDetail(Widget demandDetailWidget) {
        detailHolder.setWidget(demandDetailWidget);
    }

    @Override
    public UserConversationPanel getConversationPanel() {
        return conversationPanel;
    }

    @Override
    public SimplePanel getReplyHolder() {
        return replyHolder;
    }

}
