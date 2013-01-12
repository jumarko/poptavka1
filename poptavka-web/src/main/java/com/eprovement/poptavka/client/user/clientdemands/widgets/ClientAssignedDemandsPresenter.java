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
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Presenter(view = ClientAssignedDemandsView.class, multiple = true)
public class ClientAssignedDemandsPresenter extends LazyPresenter<
        ClientAssignedDemandsPresenter.ClientAssignedDemandsLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientAssignedDemandsLayoutInterface extends LazyView, IsWidget {

        UniversalTableGrid getDataGrid();

        SimplePager getPager();

        ListBox getActionBox();

        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater;
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedAssignedDemand = -1;
    private long selectedClientAssignedDemandId = -1;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        dataGridRangeChangeHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addReplyColumnFieldUpdater();
        addCloseDemandColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Listbox actions
        addActionChangeHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientAssignedDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_ASSIGNED_DEMANDS);

        eventBus.setUpSearchBar(new Label("Client's assigned projects attibure's selector will be here."));
        searchDataHolder = filter;
        eventBus.createTokenForHistory1(0);

        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
        //init wrapper widget
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        eventBus.requestDetailWrapperPresenter();
    }

    public void onInitClientAssignedDemandsByHistory(
            int parentTablePage, long parentId, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_ASSIGNED_DEMANDS);
        //Select Menu - my demands - selected
        eventBus.selectClientDemandsMenu(Constants.CLIENT_ASSIGNED_DEMANDS);
        //
        //If current page differ to stored one, cancel events that would be fire automatically but with no need
        if (view.getPager().getPage() != parentTablePage) {
            //cancel range change event in asynch data provider
            view.getDataGrid().cancelRangeChangedEvent();
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getPager().setPage(parentTablePage);
        //if selection differs to the restoring one
        boolean wasEqual = false;
        MultiSelectionModel selectionModel = (MultiSelectionModel) view.getDataGrid().getSelectionModel();
        for (FullOfferDetail offer : (Set<
                FullOfferDetail>) selectionModel.getSelectedSet()) {
            if (offer.getOfferDetail().getDemandId() == parentId) {
                wasEqual = true;
            }
        }
        this.selectedClientAssignedDemandId = parentId;
        if (parentId != -1 && !wasEqual) {
            selectionModel.clear();
            lastOpenedAssignedDemand = -1;
            eventBus.getClientAssignedDemand(parentId);
        }

        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            view.getDataGrid().getDataCount(eventBus, new SearchDefinition(
                    parentTablePage * view.getDataGrid().getPageSize(),
                    view.getDataGrid().getPageSize(),
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
            this.detailSection.initDetailWrapper(view.getDataGrid(), view.getWrapperPanel());
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientAssignedDemands(List<IUniversalDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemands");

        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), data);

        if (selectedClientAssignedDemandId != -1) {
            eventBus.getClientAssignedDemand(selectedClientAssignedDemandId);
        }
    }

    public void onSelectClientAssignedDemand(FullOfferDetail detail) {
//        view.getDataGrid().getSelectionModel().setSelected(detail, true);
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
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(FullOfferDetail detail) {
        detailSection.requestDemandDetail(detail.getDemandId());
        detailSection.requestSupplierDetail(detail.getSupplierId());
        detailSection.requestConversation(detail.getThreadRootId(), Storage.getUser().getUserId());
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    /**
     * Handle table range change by creating token for new range/page.
     */
    private void dataGridRangeChangeHandler() {
        view.getDataGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                eventBus.createTokenForHistory3(view.getPager().getPage(), lastOpenedAssignedDemand);
            }
        });
    }
    // Field Updaters

    public void addCheckHeaderUpdater() {
        view.getDataGrid().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<IUniversalDetail> rows = view.getDataGrid().getVisibleItems();
                for (IUniversalDetail row : rows) {
                    ((MultiSelectionModel) view.getDataGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getDataGrid().getStarColumn().setFieldUpdater(new FieldUpdater<IUniversalDetail, Boolean>() {
            @Override
            public void update(int index, IUniversalDetail object, Boolean value) {
                object.setIsStarred(!value);
                view.getDataGrid().redraw();
                Long[] item = new Long[]{object.getUserMessageId()};
                eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
            }
        });
    }

    public void addReplyColumnFieldUpdater() {
        view.getDataGrid().getReplyImageColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, ImageResource>() {
                    @Override
                    public void update(int index, IUniversalDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addQuestionReply();
                    }
                });
    }

    public void addCloseDemandColumnFieldUpdater() {
        view.getDataGrid().getCloseDemandImageColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, ImageResource>() {
                    @Override
                    public void update(int index, IUniversalDetail object, ImageResource value) {
                        eventBus.requestCloseDemand(object.getDemandId());
                    }
                });
    }

    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<FullOfferDetail, String>() {
            @Override
            public void update(int index, FullOfferDetail object, String value) {
                //getUserMessageDetail() -> getOfferDetail() due to fake data
//                if (lastOpenedAssignedDemand != object.getOfferDetail().getDemandId()) {
//                    lastOpenedAssignedDemand = object.getOfferDetail().getDemandId();
//                    view.getDataGrid().redraw();
                displayDetailContent(object);
                MultiSelectionModel selectionModel = (MultiSelectionModel) view.getDataGrid()
                        .getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
                eventBus.createTokenForHistory3(
                        view.getPager().getPage(),
                        object.getOfferDetail().getDemandId());
//                }
            }
        };
        view.getDataGrid().getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getFinnishDateColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    private void addActionChangeHandler() {
        view.getActionBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getActionBox().getSelectedIndex()) {
                    case Constants.READ:
                        eventBus.requestReadStatusUpdate(view.getDataGrid().getSelectedIdList(), true);
                        break;
                    case Constants.UNREAD:
                        eventBus.requestReadStatusUpdate(view.getDataGrid().getSelectedIdList(), false);
                        break;
                    case Constants.STARED:
                        eventBus.requestStarStatusUpdate(view.getDataGrid().getSelectedIdList(), true);
                        break;
                    case Constants.UNSTARED:
                        eventBus.requestStarStatusUpdate(view.getDataGrid().getSelectedIdList(), false);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}