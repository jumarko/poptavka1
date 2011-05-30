package cz.poptavka.sample.client.user.demands.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class DetailWrapperView extends Composite
    implements DetailWrapperPresenter.IDetailWrapper {

    private static DetailWrapperViewUiBinder uiBinder = GWT.create(DetailWrapperViewUiBinder.class);
    interface DetailWrapperViewUiBinder extends UiBinder<Widget, DetailWrapperView> {   }

    @UiField SimplePanel detailHolder, conversationHolder;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setDetail(Widget demandDetailWidget) {
        // TODO rework whole platform when cleared
//        conversationHolder.remove(conversationHolder.getWidget());
        detailHolder.setWidget(demandDetailWidget);
    }

    @Override
    public void setConversation(Widget conversationWidget) {
        detailHolder.remove(conversationHolder.getWidget());
        conversationHolder.setWidget(conversationWidget);
    }

}
