package com.eprovement.poptavka.client.user.widget;

import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.detail.EditableDemandDetailView;
import com.eprovement.poptavka.client.user.widget.detail.SupplierDetailView;
import com.eprovement.poptavka.client.user.widget.messaging.DevelOfferQuestionWindow;
import com.eprovement.poptavka.client.user.widget.messaging.SimpleMessageWindow;
import com.eprovement.poptavka.client.user.widget.messaging.UserConversationPanel;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;

/**
 * TODOS:
 * Loading Indicator while getting detail's data
 *
 * @author Martin
 */
public class DetailsWrapperView extends Composite
        implements DetailsWrapperPresenter.IDetailWrapper {

    private static DetailsWrapperViewUiBinder uiBinder = GWT.create(DetailsWrapperViewUiBinder.class);

    interface DetailsWrapperViewUiBinder extends UiBinder<Widget, DetailsWrapperView> {
    }
    @UiField
    TabLayoutPanel container;
    @UiField
    SimplePanel demandDetailHolder;
//    DemandDetailView demandDetail;
    @UiField
    SupplierDetailView supplierDetail;
    @UiField
    UserConversationPanel conversationPanel;
    @UiField
    DevelOfferQuestionWindow replyHolder;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        container.selectTab(2);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setMessageReadHandler(ChangeHandler click) {
        for (int i = 0; i < conversationPanel.getMessagePanel().getWidgetCount(); i++) {
            ((SimpleMessageWindow) conversationPanel.getMessagePanel().getWidget(i))
                    .getUpdateRead().addChangeHandler(click);
        }
    }

    @Override
    public void setDemandDetail(FullDemandDetail demand) {
        if (demandDetailHolder.getWidget() instanceof DemandDetailView) {
            ((DemandDetailView) demandDetailHolder.getWidget()).setDemanDetail(demand);
        } else if (demandDetailHolder.getWidget() instanceof EditableDemandDetailView) {
            ((EditableDemandDetailView) demandDetailHolder.getWidget()).setDemanDetail(demand);
        }
    }

    @Override
    public void setSupplierDetail(FullSupplierDetail supplier) {
        supplierDetail.setSupplierDetail(supplier);
        //supplierHeader.toggleLoading();
    }

    @Override
    public void setChat(List<MessageDetail> chatMessages, boolean collapsed) {
        conversationPanel.setMessageList(chatMessages, true);
        //conversationHeader.toggleLoading();
    }

    @Override
    public void toggleDemandLoading() {
        //toggle actual visible state
        //Implement loading indicator
        //demandHeader.toggleLoading();
    }

    @Override
    public void toggleSupplierLoading() {
        //toggle actual visible state
        //Implement loading indicator
        //supplierHeader.toggleLoading();
    }

    @Override
    public void toggleConversationLoading() {
        //toggle actual visible state
        //Implement loading indicator
        //conversationHeader.toggleLoading();
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public TabLayoutPanel getContainer() {
        return container;
    }

    @Override
    public UserConversationPanel getConversationPanel() {
        return conversationPanel;
    }

    @Override
    public DevelOfferQuestionWindow getReplyHolder() {
        return replyHolder;
    }

    @Override
    public SimplePanel getDemandDetailHolder() {
        return demandDetailHolder;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
