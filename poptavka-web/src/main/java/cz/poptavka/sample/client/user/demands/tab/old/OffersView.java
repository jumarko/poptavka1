package cz.poptavka.sample.client.user.demands.tab.old;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.widget.grid.unused.GlobalDemandOfferTable;
import cz.poptavka.sample.client.user.widget.grid.unused.SingleDemandOfferTable;
import cz.poptavka.sample.shared.domain.demand.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;

public class OffersView extends Composite implements OffersPresenter.OffersInterface {

    private static OffersViewUiBinder uiBinder = GWT.create(OffersViewUiBinder.class);
    interface OffersViewUiBinder extends UiBinder<Widget, OffersView> { }

    private static final StyleResource RSCS = GWT.create(StyleResource.class);

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    @UiField(provided = true) GlobalDemandOfferTable demandTable;
    @UiField(provided = true) SimplePager demandPager;

    @UiField(provided = true) SingleDemandOfferTable offerTable;
    @UiField(provided = true) SimplePager offerPager;

    @UiField
    Button replyBtn, deleteBtn, moreActionsBtn, refreshBtn;
    @UiField Anchor backToDemandsBtn;

    @UiField
    SimplePanel detailSection;

    // TODO remove then
    @UiField
    SimplePanel develPanel;

    private boolean offerTableVisible = false;



    @Override
    public void createView() {
        GWT.log("LOAD");
        demandTable = new GlobalDemandOfferTable(MSGS, RSCS);
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        demandPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        demandPager.setDisplay(demandTable);

        offerTable = new SingleDemandOfferTable(MSGS, RSCS);
        offerPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        offerPager.setDisplay(offerTable);

        initWidget(uiBinder.createAndBindUi(this));

        // Element newHeadDiv = header.getElement();
        // GWT.log(newHeadDiv.getNodeName());
        // Element newTable = DOM.createTable();
        // GWT.log(newTable.getNodeName());
        // Element tableHead = (Element)
        // demandTable.getElement().getFirstChildElement();
        // GWT.log(tableHead.getNodeName());
        //
        // DOM.appendChild((Element) newHeadDiv, newTable);
        // DOM.appendChild(newTable, tableHead);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }


    @Override
    public Button getReplyButton() {
        return replyBtn;
    }

    @Override
    public Button getDeleteButton() {
        return deleteBtn;
    }

    @Override
    public Button getActionButton() {
        return moreActionsBtn;
    }

    @Override
    public Button getRefreshButton() {
        return refreshBtn;
    }

    @Override
    public Set<OfferDemandDetail> getSelectedSet() {
//        return getDemandTableModel().get;
        return null;
    }
    @Override
    public NoSelectionModel<OfferDemandMessage> getDemandModel() {
        return (NoSelectionModel<OfferDemandMessage>) demandTable.getSelectionModel();
    }
    @Override
    public ListDataProvider<OfferDemandMessage> getDemandProvider() {
        return demandTable.getDataProvider();
    }

    @Override
    public SimplePanel getDetailSection() {
        return detailSection;
    }

    @Override
    public ListDataProvider<FullOfferDetail> getOfferProvider() {
        return offerTable.getDataProvider();
    }

    @Override
    public MultiSelectionModel<FullOfferDetail> getOfferModel() {
        return (MultiSelectionModel<FullOfferDetail>) offerTable.getSelectionModel();
    }

    @Override
    public Set<FullOfferDetail> getSelectedOffers() {
        return getOfferModel().getSelectedSet();
    }

    @Override
    public void swapTables() {
        if (offerTableVisible) {
            demandTable.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
            backToDemandsBtn.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
        } else {
            demandTable.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
            backToDemandsBtn.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
        }
        offerTableVisible = !offerTableVisible;
    }

    @Override
    public Anchor getBackToDemandsButton() {
        return backToDemandsBtn;
    }

}
