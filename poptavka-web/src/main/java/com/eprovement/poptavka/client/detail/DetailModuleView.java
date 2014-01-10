/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.detail.views.OfferQuestionWindow;
import com.eprovement.poptavka.client.detail.views.DemandDetailView;
import com.eprovement.poptavka.client.detail.views.DetailLoadingDiv;
import com.eprovement.poptavka.client.detail.views.RatingDetailView;
import com.eprovement.poptavka.client.detail.views.UserDetailView;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**
 * Detail module view consists of Demand, Supplier, Rating, Conversation, Advertisement tabs.
 *
 * @author Martin Slavkovsky
 */
public class DetailModuleView extends Composite
        implements DetailModulePresenter.IDetailWrapper {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface DetailsWrapperViewUiBinder extends UiBinder<Widget, DetailModuleView> {
    }
    private static DetailsWrapperViewUiBinder uiBinder = GWT.create(DetailsWrapperViewUiBinder.class);

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureDetailStylesInjected();
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField TabLayoutPanel container;
    @UiField SimplePanel demandDetailHolder, advertisementHolder;
    @UiField DemandDetailView demandDetail;
    @UiField UserDetailView userDetail;
    @UiField RatingDetailView ratingDetail;
    @UiField(provided = true) CellList messageList;
    @UiField OfferQuestionWindow replyHolder;
    @UiField FluidContainer conversationHolder;
    /** Class attribute. **/
    private DetailLoadingDiv loadingDiv = new DetailLoadingDiv();
    private ListDataProvider messageProvider = new ListDataProvider(MessageDetail.KEY_PROVIDER);

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates demand module view's compontents.
     */
    @Override
    public void createView() {
        messageList = new CellList<MessageDetail>(new MessageCell());
        messageProvider.addDataDisplay(messageList);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets loading indicator. This will display the indicator in given panel.
     * @param holderWidget - loading indicator holder panel
     */
    @Override
    public void loadingDivShow(Widget holderWidget) {
        GWT.log("  - loading div created");
        if (loadingDiv == null) {
            loadingDiv = new DetailLoadingDiv();
        }
        holderWidget.getElement().appendChild(loadingDiv.getElement());
    }

    /**
     * Hides loading indicator by removing it from given panel.
     * @param holderWidget - loading indicator holder panel
     */
    @Override
    public void loadingDivHide(Widget holderWidget) {
        GWT.log("  - loading div removed");
        if (holderWidget.getElement().isOrHasChild(loadingDiv.getElement())) {
            holderWidget.getElement().removeChild(loadingDiv.getElement());
        }
    }

    /**
     * Hide message panel if is empty, show otherwise.
     */
    @Override
    public void setMessagePanelVisibility() {
        messageList.setVisible(!messageProvider.getList().isEmpty());
        conversationHolder.setVisible(replyHolder.getMessage() != null);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @return the tab layout panel
     */
    @Override
    public TabLayoutPanel getContainer() {
        return container;
    }

    /**
     * @return the demand detail panel
     */
    @Override
    public SimplePanel getDemandDetailHolder() {
        return demandDetailHolder;
    }

    /**
     * @return the advertisement panel
     */
    @Override
    public SimplePanel getAdvertisementHolder() {
        return advertisementHolder;
    }

    /**
     * @return the DemandDetailView
     */
    @Override
    public DemandDetailView getDemandDetail() {
        return demandDetail;
    }

    /**
     * @return the UserDetailView
     */
    @Override
    public UserDetailView getSupplierDetail() {
        return userDetail;
    }

    /**
     * @return the RatingDetailView
     */
    @Override
    public RatingDetailView getRatingDetail() {
        return ratingDetail;
    }

    /**
     * @return the conversation cellList
     */
    @Override
    public CellList getMessageList() {
        return messageList;
    }

    /**
     * @return the conversation list data provider
     */
    @Override
    public ListDataProvider getMessageProvider() {
        return messageProvider;
    }

    /**
     * @return the conversation OfferQuestionWindow
     */
    @Override
    public OfferQuestionWindow getReplyHolder() {
        return replyHolder;
    }

    /**
     * @return the conversation fluid container
     */
    @Override
    public FluidContainer getConversationHolder() {
        return conversationHolder;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
