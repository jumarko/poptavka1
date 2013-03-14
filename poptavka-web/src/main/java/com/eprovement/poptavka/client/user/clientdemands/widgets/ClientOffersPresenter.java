/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;

@Presenter(view = ClientOffersView.class, multiple = true)
public class ClientOffersPresenter
        extends LazyPresenter<ClientOffersPresenter.ClientOffersLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientOffersLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalAsyncGrid<ClientDemandDetail> getDemandGrid();

        UniversalTableGrid getOfferGrid();

        //Pager
        SimplePager getDemandPager();

        SimplePager getOfferPager();

        //Action box actions
        DropdownButton getActionBox();

        NavLink getActionRead();

        NavLink getActionUnread();

        NavLink getActionStar();

        NavLink getActionUnstar();

        //Buttons
        Button getBackBtn();

        Button getAcceptBtn();

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
    private DetailsWrapperPresenter detailSection;
    private SearchModuleDataHolder searchDataHolder;
    private ClientDemandDetail selectedDemandObject;
    private IUniversalDetail selectedOfferedDemandOfferObject;
    private long selectedClientOfferedDemandId = -1;
    private long selectedClientOfferedDemandOfferId = -1;
    private FieldUpdater textFieldUpdater;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Selection Handlers
        addDemandTableSelectionHandler();
        addOfferGridSelectionModelHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Buttons Actions
        addBackButtonHandler();
        addAcceptOfferButtonHandler();
        addActionBoxChoiceHandlers();
        // Row styles
        addDemandGridRowStyles();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientOffers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);

        eventBus.setUpSearchBar(new Label("Client's contests attibure's selector will be here."));
        searchDataHolder = filter;
        eventBus.createTokenForHistory();

        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
        //init wrapper widget
        view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Details Wrapper                                                        */
    /**************************************************************************/
    /**
     * Response method to requesting details wrapper instance.
     * Initialize details wrapper and initialize details tabs according to
     * selectedDemandObject and selectedConversationObject.
     * @param detailSection Details wrapper instance.
     */
    public void onResponseDetailWrapperPresenter(final DetailsWrapperPresenter detailSection) {
        if (detailSection != null) {
            detailSection.initDetailWrapper(view.getOfferGrid(), view.getWrapperPanel());

            this.detailSection = detailSection;

            if (selectedOfferedDemandOfferObject != null) {
                initDetailSection(selectedOfferedDemandOfferObject);
            } else if (selectedDemandObject != null) {
                initDetailSection(selectedDemandObject);
            }
        }
    }

    /**
     * Initialize demand tab in Details sections.
     * If details wrapper instance doesn't exist yet, create it and in response of
     * creation initialize demand tab.
     * If instance already exist, initialize and show demand tab immediately.
     *
     * @param demandId
     */
    private void initDetailSection(ClientDemandDetail demandDetail) {
        if (detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        } else {
            detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.BLOCK);
            detailSection.initDetails(demandDetail.getDemandId());
        }
    }

    /**
     * Initialize demand, supplier and conversation tabs in Details sections.
     * If details wrapper instance doesn't exist yet, create it and in response of
     * creation initialize demand, supplier, conversation tabs.
     * If instance already exist, initialize and show tabs immediately.
     *
     * @param demandId
     */
    private void initDetailSection(IUniversalDetail conversationDetail) {
        if (detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        } else {
            detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.BLOCK);
            detailSection.initDetails(
                    conversationDetail.getDemandId(),
                    conversationDetail.getSupplierId(),
                    conversationDetail.getThreadRootId());
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

    public void onDisplayClientOfferedDemandOffers(List<IUniversalDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemandOffers");

        if (!data.isEmpty()) {
            view.setDemandTableVisible(false);
            view.setOfferTableVisible(true);
            view.getOfferPager().startLoading();

            view.getOfferGrid().getDataProvider().updateRowData(
                    view.getOfferGrid().getStart(), data);

            if (selectedClientOfferedDemandOfferId != -1) {
                eventBus.getClientOfferedDemand(selectedClientOfferedDemandOfferId);
            }
        }
    }

    public void onSelectClientOfferedDemand(ClientDemandDetail detail) {
        view.getDemandGrid().getSelectionModel().setSelected(detail, true);
    }

    public void onSelectClientOfferedDemandOffer(ClientOfferedDemandOffersDetail detail) {
        eventBus.setHistoryStoredForNextOne(false); //don't create token
        //nestaci oznacit v modeli, pretoze ten je viazany na checkboxy a akcie, musim
        //nejak vytvorit event na upadatefieldoch
        //Dolezite je len detail, ostatne atributy sa nepouzivaju
        textFieldUpdater.update(-1, detail, null);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    /** Action box handers. **/
    //--------------------------------------------------------------------------
    private void addActionBoxChoiceHandlers() {
        view.getActionRead().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestReadStatusUpdate(view.getOfferGrid().getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnread().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestReadStatusUpdate(view.getOfferGrid().getSelectedUserMessageIds(), false);
            }
        });
        view.getActionStar().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestStarStatusUpdate(view.getOfferGrid().getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnstar().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestStarStatusUpdate(view.getOfferGrid().getSelectedUserMessageIds(), false);
            }
        });
    }

    /** SelectionHandlers. **/
    //--------------------------------------------------------------------------
    /**
     * Show child table and fire event for getting its data.
     * Hides detail section if no row is selected.
     */
    private void addDemandTableSelectionHandler() {
        view.getDemandGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMAND_OFFERS);
                ClientDemandDetail selected = (ClientDemandDetail) ((SingleSelectionModel) view.getDemandGrid()
                        .getSelectionModel()).getSelectedObject();
                if (selected != null) {
                    selectedDemandObject = selected;
                    initDetailSection(selected);
                    Storage.setDemandId(selected.getDemandId());
                    Storage.setThreadRootId(selected.getThreadRootId());
                    view.setDemandTitleLabel(selected.getDemandTitle());
                    view.getOfferGrid().getDataCount(eventBus, null);
                }
            }
        });
    }

    /**
     * Show or Hide details section and action box.
     * Show if and only of one table row is selected.
     * Hide otherwise - not or more than one rows are selected.
     */
    public void addOfferGridSelectionModelHandler() {
        view.getOfferGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //set actionBox visibility
                if (view.getOfferGrid().getSelectedUserMessageIds().size() > 0) {
                    view.getActionBox().setVisible(true);
                } else {
                    view.getActionBox().setVisible(false);
                }
                //init details
                if (view.getOfferGrid().getSelectedUserMessageIds().size() > 1) {
                    detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.NONE);
                } else {
                    IUniversalDetail selected = view.getOfferGrid().getSelectedObjects().get(0);
                    selectedOfferedDemandOfferObject = selected;
                    initDetailSection(selected);
                }
            }
        });
    }

    /** Field updater. **/
    //--------------------------------------------------------------------------
    /**
     * Show and loads detail section. Show after clicking on certain columns that
     * have defined this fieldUpdater.
     */
    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<ClientOfferedDemandOffersDetail, String>() {
            @Override
            public void update(int index, ClientOfferedDemandOffersDetail object, String value) {
                object.setIsRead(true);
                view.setDemandTableVisible(false);
                view.setOfferTableVisible(true);

                MultiSelectionModel selectionModel = view.getOfferGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
            }
        };
        view.getOfferGrid().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getFinnishDateColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Field Updaters
    public void addCheckHeaderUpdater() {
        view.getOfferGrid().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<IUniversalDetail> rows = view.getOfferGrid().getVisibleItems();
                for (IUniversalDetail row : rows) {
                    ((MultiSelectionModel) view.getOfferGrid()
                            .getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getOfferGrid().getStarColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, Boolean>() {
                @Override
                public void update(int index, IUniversalDetail object, Boolean value) {
                    object.setIsStarred(!value);
                    view.getOfferGrid().redraw();
                    Long[] item = new Long[]{object.getUserMessageId()};
                    eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
                }
            });
    }

    // Buttons
    private void addBackButtonHandler() {
        view.getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
                if (detailSection != null) {
                    detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.NONE);
                }
                selectedDemandObject = null;
                selectedOfferedDemandOfferObject = null;
                view.getOfferGrid().getSelectionModel().clear();
                view.getDemandPager().startLoading();
                view.setOfferTableVisible(false);
                view.setDemandTableVisible(true);
                view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
            }
        });
    }

    private void addAcceptOfferButtonHandler() {
        view.getAcceptBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestAcceptOffer(
                        selectedOfferedDemandOfferObject.getOfferId(),
                        selectedOfferedDemandOfferObject.getUserMessageId());
            }
        });
    }

    /** Row Styles. **/
    private void addDemandGridRowStyles() {
        view.getDemandGrid().setRowStyles(new RowStyles<ClientDemandDetail>() {
            @Override
            public String getStyleNames(ClientDemandDetail row, int rowIndex) {
                if (row.getUnreadSubmessagesCount() > 0) {
                    return Storage.RSCS.grid().unread();
                } else {
                    return "";
                }
            }
        });
    }
}
