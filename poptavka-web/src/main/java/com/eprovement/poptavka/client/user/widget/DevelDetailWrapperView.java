package com.eprovement.poptavka.client.user.widget;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.user.widget.detail.ClickableDiv;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.detail.SupplierDetailView;
import com.eprovement.poptavka.client.user.widget.messaging.UserConversationPanel;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import java.util.List;

public class DevelDetailWrapperView extends Composite
    implements DevelDetailWrapperPresenter.IDetailWrapper {

    private static DetailWrapperViewUiBinder uiBinder = GWT.create(DetailWrapperViewUiBinder.class);
    interface DetailWrapperViewUiBinder extends UiBinder<Widget, DevelDetailWrapperView> {   }

    @UiField HTMLPanel container;
    @UiField ClickableDiv demandHeader, supplierHeader, conversationHeader;
    @UiField DemandDetailView demandDetail;
    @UiField SupplierDetailView supplierDetail;
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
        supplierHeader.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                supplierHeader.toggleOpen();
                supplierDetail.toggleVisible();
            }
        });
        conversationHeader.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                conversationHeader.toggleOpen();
                conversationPanel.toggleVisible();
            }
        });
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setDemandDetail(FullDemandDetail demand) {
        demandDetail.setDemanDetail(demand);
        demandHeader.toggleLoading();
    }

    @Override
    public void setSupplierDetail(FullSupplierDetail supplier) {
        supplierDetail.setSupplierDetail(supplier);
        supplierHeader.toggleLoading();
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
    public void setChat(List<MessageDetail> chatMessages, boolean collapsed) {
        conversationPanel.setMessageList(chatMessages, true);
        conversationHeader.toggleLoading();
    }

    @Override
    public void toggleDemandLoading() {
        //toggle actual visible state
        demandHeader.toggleLoading();
    }

    @Override
    public void toggleSupplierLoading() {
        //toggle actual visible state
        supplierHeader.toggleLoading();
    }

    @Override
    public void toggleConversationLoading() {
        //toggle actual visible state
        conversationHeader.toggleLoading();
    }

}
