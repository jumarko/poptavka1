/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;

@Presenter(view = ClientOffersView.class)
public class ClientOffersPresenter
        extends LazyPresenter<ClientOffersPresenter.ClientOffersLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientOffersLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalAsyncGrid<ClientDemandDetail> getDemandGrid();

        UniversalTableWidget getOfferGrid();

        //Pager
        SimplePager getDemandPager();

        SimplePager getOfferPager();

        //Buttons
        Button getBackBtn();

        //Other
        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();

        //Setter
        void setDemandTableVisible(boolean visible);

        void setOfferTableVisible(boolean visible);

        void setDemandTitleLabel(String text);
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private ViewType type = ViewType.EDITABLE;
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemandOffer = -1;
    private boolean cancelTokenCreation = false;
    private long selectedClientOfferedDemandId = -1;
    private long selectedClientOfferedDemandOfferId = -1;
    private int demandPageFromToken = -1;
    private int offerPageFromToken = -1;
    private FieldUpdater textFieldUpdater = null;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Range Change Handlers
        demandGridRangeChangeHandler();
        offerGridRangeChangeHandler();
        // Selection Handlers
        addDemandTableSelectionHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addReplyColumnFieldUpdater();
        addAcceptOfferColumnFieldUpdater();
        addDeclineOfferColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Buttons Actions
        addBackButtonHandler();
        addActionChangeHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientOffers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
        eventBus.setUpSearchBar(new Label("Client's contests attibure's selector will be here."));
        searchDataHolder = filter;
        view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        eventBus.createTokenForHistory1(0);

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (this.detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        }
    }

    public void onInitClientOfferedDemandsByHistory(int parentTablePage, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
        //Select Menu - my demands - selected
        eventBus.selectClientDemandsMenu(Constants.CLIENT_OFFERED_DEMANDS);
        //
        cancelTokenCreation = true;
        view.getDemandGrid().cancelRangeChangedEvent();
        view.getDemandGrid().setPageStart(parentTablePage * view.getDemandGrid().getPageSize());
        view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(
                parentTablePage * view.getDemandGrid().getPageSize(),
                view.getDemandGrid().getPageSize(),
                filterHolder,
                null));
        view.setOfferTableVisible(false);
        view.setDemandTableVisible(true);

        this.selectedClientOfferedDemandId = -1;

        if (Storage.isAppCalledByURL()) {
            eventBus.displayView(view.getWidgetView());
        }
    }

    public void onInitClientOfferedDemandOffersByHistory(ClientDemandDetail clientDemandDetail,
            int childTablePage, long childId, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMAND_DISCUSSIONS);
        //Select Menu - my demands - selected
        eventBus.selectClientDemandsMenu(Constants.CLIENT_DEMANDS);
        //
        clientDemandDetail.setRead(true);
        Storage.setDemandId(clientDemandDetail.getDemandId());
        view.setDemandTitleLabel(clientDemandDetail.getDemandTitle());
        view.setDemandTableVisible(false);
        view.setOfferTableVisible(true);
        //
        cancelTokenCreation = true;
        view.getOfferGrid().getGrid().cancelRangeChangedEvent();
        view.getOfferGrid().getGrid().setPageStart(childTablePage * view.getOfferGrid().getGrid().getPageSize());
        view.getOfferGrid().getGrid().getDataCount(eventBus, new SearchDefinition(
                childTablePage * view.getOfferGrid().getGrid().getPageSize(),
                view.getOfferGrid().getGrid().getPageSize(),
                filterHolder,
                null));

        this.selectedClientOfferedDemandOfferId = childId;

        if (Storage.isAppCalledByURL()) {
            eventBus.displayView(view.getWidgetView());
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        if (this.detailSection == null) {
            this.detailSection = detailSection;
            this.detailSection.initDetailWrapper(view.getWrapperPanel(), type);
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientOfferedDemands(List<ClientDemandDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemands");

        view.getDemandGrid().getDataProvider().updateRowData(
                view.getDemandGrid().getStart(), data);

        if (selectedClientOfferedDemandId != -1) {
            eventBus.getClientOfferedDemand(selectedClientOfferedDemandId);
        }
    }

    public void onDisplayClientOfferedDemandOffers(List<FullOfferDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemandOffers");

        view.getOfferGrid().getGrid().getDataProvider().updateRowData(
                view.getOfferGrid().getGrid().getStart(), data);

        if (selectedClientOfferedDemandOfferId != -1) {
            eventBus.getClientOfferedDemand(selectedClientOfferedDemandOfferId);
        }
    }

    public void onSelectClientOfferedDemand(ClientDemandDetail detail) {
        view.getDemandGrid().getSelectionModel().setSelected(detail, true);
    }

    public void onSelectClientOfferedDemandOffer(FullOfferDetail detail) {
        //nestaci oznacit v modeli, pretoze ten je viazany na checkboxy a akcie, musim
        //nejak vytvorit event na upadatefieldoch
        //Dolezite je len detail, ostatne atributy sa nepouzivaju
        textFieldUpdater.update(-1, detail, null);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(FullOfferDetail detail) {
//        detailSection.requestDemandDetail(detail.getDemandId(), type);
        detailSection.requestDemandDetail(123L, type);

//        detailSection.requestSupplierDetail(detail.getSupplierId(), type);
        detailSection.requestSupplierDetail(142811L, type);

//        detailSection.requestOffer(detail.getMessageId(),
//                detail.getUserMessageId(), Storage.getUser().getUserId());
        detailSection.requestConversation(124L, 289L, 149L);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    // Field Updaters
    public void addCheckHeaderUpdater() {
        view.getOfferGrid().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<FullOfferDetail> rows = view.getOfferGrid().getGrid().getVisibleItems();
                for (FullOfferDetail row : rows) {
                    ((MultiSelectionModel) view.getOfferGrid().getGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getOfferGrid().getStarColumn().setFieldUpdater(
                new FieldUpdater<FullOfferDetail, Boolean>() {
                    @Override
                    public void update(int index, FullOfferDetail object, Boolean value) {
                        TableDisplay obj = (TableDisplay) object;
                        object.setStarred(!value);
                        view.getOfferGrid().getGrid().redraw();
                        Long[] item = new Long[]{object.getUserMessageDetail().getId()};
                        eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
                    }
                });
    }

    public void addReplyColumnFieldUpdater() {
        view.getOfferGrid().getReplyImageColumn().setFieldUpdater(
                new FieldUpdater<FullOfferDetail, ImageResource>() {
                    @Override
                    public void update(int index, FullOfferDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addQuestionReply();
                    }
                });
    }

    public void addAcceptOfferColumnFieldUpdater() {
        view.getOfferGrid().getAcceptOfferImageColumn().setFieldUpdater(
                new FieldUpdater<FullOfferDetail, ImageResource>() {
                    @Override
                    public void update(int index, FullOfferDetail object, ImageResource value) {
                        eventBus.requestAcceptOffer(object);
                    }
                });
    }

    public void addDeclineOfferColumnFieldUpdater() {
        view.getOfferGrid().getDeclineOfferImageColumn().setFieldUpdater(
                new FieldUpdater<FullOfferDetail, ImageResource>() {
                    @Override
                    public void update(int index, FullOfferDetail object, ImageResource value) {
                        eventBus.requestDeclineOffer(object.getOfferDetail());
                    }
                });
    }

    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<FullOfferDetail, String>() {
            @Override
            public void update(int index, FullOfferDetail object, String value) {
                if (lastOpenedDemandOffer != object.getUserMessageDetail().getId()) {
                    lastOpenedDemandOffer = object.getUserMessageDetail().getId();
                    object.setRead(true);
                    view.getOfferGrid().getGrid().redraw();
                    displayDetailContent(object);
                    if (cancelTokenCreation) {
                        cancelTokenCreation = false;
                    } else {
                        eventBus.createTokenForHistory2(Storage.getDemandId(),
                                view.getOfferPager().getPage(), object.getOfferDetail().getId());
                    }
                }
            }
        };
        view.getOfferGrid().getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getDeliveryColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Buttons
    private void addBackButtonHandler() {
        view.getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.setOfferTableVisible(false);
            }
        });
    }

    private void addActionChangeHandler() {
        view.getOfferGrid().getActionBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getOfferGrid().getActionBox().getSelectedIndex()) {
                    case 1:
                        eventBus.requestReadStatusUpdate(view.getOfferGrid().getSelectedIdList(), true);
                        break;
                    case 2:
                        eventBus.requestReadStatusUpdate(view.getOfferGrid().getSelectedIdList(), false);
                        break;
                    case 3:
                        eventBus.requestStarStatusUpdate(view.getOfferGrid().getSelectedIdList(), true);
                        break;
                    case 4:
                        eventBus.requestStarStatusUpdate(view.getOfferGrid().getSelectedIdList(), false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //SelectionHandlers
    private void addDemandTableSelectionHandler() {
        view.getDemandGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMAND_OFFERS);
                ClientDemandDetail selected = (ClientDemandDetail) ((SingleSelectionModel) view.getDemandGrid()
                        .getSelectionModel()).getSelectedObject();
                if (selected != null) {
                    selected.setRead(true);
                    Storage.setDemandId(selected.getDemandId());
                    view.setDemandTitleLabel(selected.getDemandTitle());
                    view.setOfferTableVisible(true);
                    view.getOfferGrid().getGrid().getDataCount(eventBus, null);
                    eventBus.createTokenForHistory2(selected.getDemandId(), view.getOfferPager().getPage(), -1);
                }
            }
        });
    }

    /**************************************************************************/
    /**
     * Handle table range change by creating token for new range/page.
     */
    private void demandGridRangeChangeHandler() {
        view.getDemandGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                demandPageFromToken = -1;
                //In some cases we need to set pager size to 0, which fires this event
                //but we don't want to create token, therefore deny fired event if
                //cancelPagerEvent flag is True.
                if (cancelTokenCreation) {
                    cancelTokenCreation = false;
                } else {
                    eventBus.createTokenForHistory1(view.getDemandPager().getPage());
                }
            }
        });
    }

    private void offerGridRangeChangeHandler() {
        view.getOfferGrid().getGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                offerPageFromToken = -1;
                //In some cases we need to set pager size to 0, which fires this event
                //but we don't want to create token, therefore deny fired event if
                //cancelPagerEvent flag is True.
                if (cancelTokenCreation) {
                    cancelTokenCreation = false;
                } else {
                    eventBus.createTokenForHistory2(
                            Storage.getDemandId(), view.getOfferPager().getPage(), -1);
                }
            }
        });
    }
}