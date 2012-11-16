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
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
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
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
import java.util.Set;

@Presenter(view = ClientDemandsView.class)
public class ClientDemandsPresenter
        extends LazyPresenter<ClientDemandsPresenter.ClientDemandsLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientDemandsLayoutInterface extends LazyView, IsWidget {

        // Columns
        Header getCheckHeader();

        Column<ClientDemandConversationDetail, Boolean> getCheckColumn();

        Column<ClientDemandConversationDetail, Boolean> getStarColumn();

        Column<ClientDemandConversationDetail, ImageResource> getReplyColumn();

        Column<ClientDemandConversationDetail, String> getSupplierNameColumn();

        Column<ClientDemandConversationDetail, String> getBodyPreviewColumn();

        Column<ClientDemandConversationDetail, String> getDateColumn();

        //Pagers
        SimplePager getDemandPager();

        SimplePager getConversationPager();
        // Others

        Button getBackBtn();

        UniversalAsyncGrid<ClientDemandDetail> getDemandGrid();

        UniversalAsyncGrid<ClientDemandConversationDetail> getConversationGrid();

        List<Long> getSelectedIdList();

        Set<ClientDemandConversationDetail> getSelectedMessageList();

        //ListBox
        ListBox getActions();

        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();

        // Setters
        void setDemandTableVisible(boolean visible);

        void setConversationTableVisible(boolean visible);

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
    private long lastOpenedDemandConversation = -1;
    private long selectedClientDemandId = -1;
    private long selectedClientDemandConversationId = -1;
    //
    private FieldUpdater textFieldUpdater = null;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        addBackBtnClickHandler();
        // Range Change Handlers
        demandGridRangeChangeHandler();
        conversationGridRangeChangeHandler();
        // Selection Handlers
        addDemandTableSelectionHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        addReplyColumnFieldUpdater();
        // Listbox actions
        addActionChangeHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS);
        eventBus.setUpSearchBar(new Label("Client's projects attibure's selector will be here."));
        searchDataHolder = filter;
        view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (this.detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        }
    }

    public void onInitClientDemandsByHistory(int parentTablePage, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS);
        //Select Menu - my demands - selected
        eventBus.selectClientDemandsMenu(Constants.CLIENT_DEMANDS);
        //
        //If current page differ to stored one, cancel events that would be fire automatically but with no need
        if (view.getDemandPager().getPage() != parentTablePage) {
            view.getDemandGrid().cancelRangeChangedEvent(); //cancel range change event in asynch data provider
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getDemandPager().setPage(parentTablePage);
        //Change visibility
        view.setConversationTableVisible(false);
        view.setDemandTableVisible(true);

        this.selectedClientDemandId = -1;

        if (Storage.isAppCalledByURL()) {
            view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(
                    parentTablePage * view.getDemandGrid().getPageSize(),
                    view.getDemandGrid().getPageSize(),
                    filterHolder,
                    null));
        }

        eventBus.displayView(view.getWidgetView());
    }

    public void onInitClientDemandConversationByHistory(ClientDemandDetail clientDemandDetail,
            int childTablePage, long childId, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMAND_DISCUSSIONS);
        //Select Menu - my demands - selected
        eventBus.selectClientDemandsMenu(Constants.CLIENT_DEMANDS);
        //
        clientDemandDetail.setRead(true);
        Storage.setDemandId(clientDemandDetail.getDemandId());
        view.setDemandTitleLabel(clientDemandDetail.getDemandTitle());
        view.setDemandTableVisible(false);
        view.setConversationTableVisible(true);
        //
        if (view.getConversationPager().getPage() != childTablePage) {
            view.getConversationGrid().cancelRangeChangedEvent(); //cancel range change event in asynch data provider
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getConversationPager().setPage(childTablePage);
        //if selection differs to the restoring one
        boolean wasEqual = false;
        MultiSelectionModel selectionModel = (MultiSelectionModel) view.getConversationGrid().getSelectionModel();
        //find out if child id is already selected
        for (ClientDemandConversationDetail cdcd : (Set<
                ClientDemandConversationDetail>) selectionModel.getSelectedSet()) {
            if (cdcd.getSupplierId() == childId) {
                wasEqual = true;
            }
        }

        this.selectedClientDemandConversationId = childId;
        if (childId != -1 && !wasEqual) {
            selectionModel.clear();
            lastOpenedDemandConversation = -1;
            eventBus.getClientDemandConversation(childId);
        }

        if (Storage.isAppCalledByURL()) {
            view.getConversationGrid().getDataCount(eventBus, new SearchDefinition(
                    childTablePage * view.getConversationGrid().getPageSize(),
                    view.getConversationGrid().getPageSize(),
                    filterHolder,
                    null));
        }

        eventBus.displayView(view.getWidgetView());
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
    public void onDisplayClientDemands(List<ClientDemandDetail> data) {
        GWT.log("++ onResponseClientsDemands");

        view.getDemandGrid().getDataProvider().updateRowData(
                view.getDemandGrid().getStart(), data);

        if (selectedClientDemandId != -1) {
            eventBus.getClientDemand(selectedClientDemandId);
        }
    }

    public void onDisplayClientDemandConversations(List<ClientDemandConversationDetail> data) {
        GWT.log("++ onResponseClientsDemands");

        view.getConversationGrid().getDataProvider().updateRowData(
                view.getConversationGrid().getStart(), data);

        if (selectedClientDemandConversationId != -1) {
            eventBus.getClientDemandConversation(selectedClientDemandConversationId);
        }
    }

    public void onSelectClientDemand(ClientDemandDetail detail) {
        view.getDemandGrid().getSelectionModel().setSelected(detail, true);
    }

    public void onSelectClientDemandConversation(ClientDemandConversationDetail detail) {
        eventBus.setHistoryStoredForNextOne(false); //don't create token
        //nestaci oznacit v modeli, pretoze ten je viazany na checkboxy a akcie, musim
        //nejak vytvorit event na upadatefieldoch
        //Dolezite je len detail, ostatne atributy sa nepouzivaju
        textFieldUpdater.update(-1, detail, null);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related conversation
     * @param userMessageId ID for demand related conversation
     */
    public void displayDetailContent(ClientDemandConversationDetail detail) {
//        detailSection.requestDemandDetail(detail.getDemandId(), type);
        detailSection.requestDemandDetail(123L, type);

//        detailSection.requestSupplierDetail(detail.getSupplierId(), type);
        detailSection.requestSupplierDetail(142811L, type);

//        detailSection.requestContest(detail.getMessageId(),
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
        view.getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<ClientDemandConversationDetail> rows = view.getConversationGrid().getVisibleItems();
                for (ClientDemandConversationDetail row : rows) {
                    ((MultiSelectionModel) view.getConversationGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getStarColumn().setFieldUpdater(
                new FieldUpdater<ClientDemandConversationDetail, Boolean>() {
                    @Override
                    public void update(int index, ClientDemandConversationDetail object, Boolean value) {
                        object.setStarred(!value);
                        view.getConversationGrid().redraw();
                        Long[] item = new Long[]{object.getUserMessageId()};
                        eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
                    }
                });
    }

    public void addReplyColumnFieldUpdater() {
        view.getReplyColumn().setFieldUpdater(
                new FieldUpdater<ClientDemandConversationDetail, ImageResource>() {
                    @Override
                    public void update(int index, ClientDemandConversationDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addQuestionReply();
                    }
                });
    }

    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<ClientDemandConversationDetail, String>() {
            @Override
            public void update(int index, ClientDemandConversationDetail object, String value) {
                if (lastOpenedDemandConversation != object.getUserMessageId()) {
                    lastOpenedDemandConversation = object.getUserMessageId();
                    object.setRead(true);
//                    view.getConversationGrid().redraw();
                    view.setDemandTableVisible(false);
                    view.setConversationTableVisible(true);
                    displayDetailContent(object);
                    MultiSelectionModel selectionModel = (MultiSelectionModel) view.getConversationGrid()
                            .getSelectionModel();
                    selectionModel.clear();
                    selectionModel.setSelected(object, true);
                    eventBus.createTokenForHistory2(Storage.getDemandId(),
                            view.getConversationPager().getPage(), object.getSupplierId());
                }
            }
        };
        view.getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getBodyPreviewColumn().setFieldUpdater(textFieldUpdater);
        view.getDateColumn().setFieldUpdater(textFieldUpdater);
    }

    // Widget action handlers
    private void addActionChangeHandler() {
        view.getActions().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getActions().getSelectedIndex()) {
                    case 1:
                        eventBus.requestReadStatusUpdate(view.getSelectedIdList(), true);
                        break;
                    case 2:
                        eventBus.requestReadStatusUpdate(view.getSelectedIdList(), false);
                        break;
                    case 3:
                        eventBus.requestStarStatusUpdate(view.getSelectedIdList(), true);
                        break;
                    case 4:
                        eventBus.requestStarStatusUpdate(view.getSelectedIdList(), false);
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
                Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMAND_DISCUSSIONS);
                ClientDemandDetail selected = (ClientDemandDetail) ((SingleSelectionModel) view.getDemandGrid()
                        .getSelectionModel()).getSelectedObject();
                if (selected != null) {
                    selected.setRead(true);
                    Storage.setDemandId(selected.getDemandId());
                    view.setDemandTitleLabel(selected.getDemandTitle());
                    view.setDemandTableVisible(false);
                    view.setConversationTableVisible(true);
                    view.getConversationGrid().getDataCount(eventBus, null);
                    eventBus.createTokenForHistory2(selected.getDemandId(), view.getConversationPager().getPage(), -1);
                }
            }
        });
    }

    //Button handlers
    private void addBackBtnClickHandler() {
        view.getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.setConversationTableVisible(false);
                view.setDemandTableVisible(true);
                //ziskaj nanovo data?
                if (view.getDemandGrid().getDataProvider() == null) {
                    view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(
                            view.getDemandGrid().getStart(),
                            view.getDemandGrid().getPageSize(),
                            searchDataHolder,
                            null));
                }
            }
        });
    }

    /**************************************************************************/
    /**
     * If demand table range change (page changed), create token for new data (different page).
     */
    private void demandGridRangeChangeHandler() {
        view.getDemandGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                eventBus.createTokenForHistory1(view.getDemandPager().getPage());
            }
        });
    }

    /**
     * If conversation table range change (page changed), create token for new data (different page).
     */
    private void conversationGridRangeChangeHandler() {
        view.getConversationGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                eventBus.createTokenForHistory2(
                        Storage.getDemandId(), view.getConversationPager().getPage(), -1);
            }
        });
    }
}