package com.eprovement.poptavka.client.user.widget;

import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.detail.SupplierDetailView;
import com.eprovement.poptavka.client.user.widget.messaging.OfferQuestionWindow;
import com.eprovement.poptavka.client.user.widget.messaging.ConversationPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODOS:
 * Loading Indicator while getting detail's data
 *
 * @author Martin
 */
public class DetailsWrapperView extends Composite
        implements DetailsWrapperPresenter.IDetailWrapper {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface DetailsWrapperViewUiBinder extends UiBinder<Widget, DetailsWrapperView> {
    }
    private static DetailsWrapperViewUiBinder uiBinder = GWT.create(DetailsWrapperViewUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField
    TabLayoutPanel container;
    @UiField
    SimplePanel demandDetailHolder;
    @UiField
    SupplierDetailView supplierDetail;
    @UiField
    ConversationPanel conversationPanel;
    @UiField
    OfferQuestionWindow replyHolder;
    @UiField
    HTMLPanel conversationHolder;
    @UiField
    HTML userDetailTabHeaderLabel;
    /** Class attribute. **/
    private LoadingDiv loadingDiv = new LoadingDiv();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
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
    public SimplePanel getDemandDetailHolder() {
        return demandDetailHolder;
    }

    @Override
    public SupplierDetailView getSupplierDetail() {
        return supplierDetail;
    }

    @Override
    public ConversationPanel getConversationPanel() {
        return conversationPanel;
    }

    @Override
    public OfferQuestionWindow getReplyHolder() {
        return replyHolder;
    }

    @Override
    public HTMLPanel getConversationHolder() {
        return conversationHolder;
    }

    @Override
    public Label getUserDetailTabHeaderLabel() {
        return userDetailTabHeaderLabel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
