package com.eprovement.poptavka.client.user.admin.detail;

import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.cell.MessageCell;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**
 * TODO LATER martin: Loading Indicator while getting detail's data
 *
 * @author Martin
 */
public class AdminDetailsWrapperView extends Composite
        implements AdminDetailsWrapperPresenter.IDetailWrapper {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface AdminDetailsWrapperViewUiBinder extends UiBinder<Widget, AdminDetailsWrapperView> {
    }
    private static AdminDetailsWrapperViewUiBinder uiBinder = GWT.create(AdminDetailsWrapperViewUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField TabLayoutPanel container;
    @UiField DemandDetailView demandDetail;
    @UiField(provided = true) CellList messageList;
    @UiField AdminOfferQuestionWindow replyHolder;
    @UiField HTMLPanel conversationHolder;
    /** Class attribute. **/
    private LoadingDiv loadingDiv = new LoadingDiv();
    private ListDataProvider messageProvider = new ListDataProvider(MessageDetail.KEY_PROVIDER);

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        messageList = new CellList<MessageDetail>(new MessageCell());
        messageProvider.addDataDisplay(messageList);
        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.detailTabPanel().ensureInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void loadingDivShow(Widget holderWidget) {
        GWT.log("  - loading div created");
        if (loadingDiv == null) {
            loadingDiv = new LoadingDiv();
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

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public TabLayoutPanel getContainer() {
        return container;
    }

    @Override
    public DemandDetailView getDemandDetail() {
        return demandDetail;
    }

    @Override
    public CellList getMessagePanel() {
        return messageList;
    }

    @Override
    public ListDataProvider getMessageProvider() {
        return messageProvider;
    }

    @Override
    public AdminOfferQuestionWindow getReplyHolder() {
        return replyHolder;
    }

    @Override
    public HTMLPanel getConversationHolder() {
        return conversationHolder;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
