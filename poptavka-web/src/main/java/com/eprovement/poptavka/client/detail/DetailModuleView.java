package com.eprovement.poptavka.client.detail;

import com.eprovement.poptavka.client.detail.views.OfferQuestionWindow;
import com.eprovement.poptavka.client.detail.views.DemandDetailView;
import com.eprovement.poptavka.client.detail.views.DetailLoadingDiv;
import com.eprovement.poptavka.client.detail.views.RatingDetailView;
import com.eprovement.poptavka.client.detail.views.UserDetailView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**
 * TODO LATER Martin: Loading Indicator while getting detail's data
 *
 * @author Martin
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
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField TabLayoutPanel container;
    @UiField ScrollPanel demandDetailHolder, advertisementHolder;
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
    @Override
    public void createView() {
        messageList = new CellList<MessageDetail>(new MessageCell());
        messageProvider.addDataDisplay(messageList);
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.details().ensureInjected();
        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void loadingDivShow(Widget holderWidget) {
        GWT.log("  - loading div created");
        if (loadingDiv == null) {
            loadingDiv = new DetailLoadingDiv();
        }
        holderWidget.getElement().appendChild(loadingDiv.getElement());
    }

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
    @Override
    public TabLayoutPanel getContainer() {
        return container;
    }

    @Override
    public SimplePanel getDemandDetailHolder() {
        return demandDetailHolder;
    }

    @Override
    public SimplePanel getAdvertisementHolder() {
        return advertisementHolder;
    }

    @Override
    public DemandDetailView getDemandDetail() {
        return demandDetail;
    }

    @Override
    public UserDetailView getSupplierDetail() {
        return userDetail;
    }

    @Override
    public RatingDetailView getRatingDetail() {
        return ratingDetail;
    }

    @Override
    public CellList getMessageList() {
        return messageList;
    }

    @Override
    public ListDataProvider getMessageProvider() {
        return messageProvider;
    }

    @Override
    public OfferQuestionWindow getReplyHolder() {
        return replyHolder;
    }

    @Override
    public FluidContainer getConversationHolder() {
        return conversationHolder;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
