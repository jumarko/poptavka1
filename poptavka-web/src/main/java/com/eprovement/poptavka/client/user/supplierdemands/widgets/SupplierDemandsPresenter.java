/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
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

@Presenter(view = SupplierDemandsView.class, multiple = true)
public class SupplierDemandsPresenter extends LazyPresenter<
        SupplierDemandsPresenter.SupplierDemandsLayoutInterface, SupplierDemandsModuleEventBus> {

    public interface SupplierDemandsLayoutInterface extends LazyView, IsWidget {

        UniversalTableGrid getDataGrid();

        SimplePager getPager();

        ListBox getActionBox();

        SimplePanel getDetailPanel();

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater;
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedPotentialDemand = -1;
    private long selectedSupplierDemandId = -1;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Range Change Handlers
        tableRangeChangeHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addReplyColumnFieldUpdater();
        addSendOfferColumnFieldUpdater();
        addColumnFieldUpdaters();
        // Listbox actions
        addActionChangeHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_POTENTIAL_DEMANDS);

        eventBus.setUpSearchBar(new Label("Supplier's projects attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        eventBus.requestDetailWrapperPresenter();
    }

    public void onInitSupplierDemandsByHistory(int tablePage, long selectedId, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        //Select Menu - my demands - selected
        eventBus.selectSupplierDemandsMenu(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        //
        //If current page differ to stored one, cancel events that would be fire automatically but with no need
        if (view.getPager().getPage() != tablePage) {
            //cancel range change event in asynch data provider
            view.getDataGrid().cancelRangeChangedEvent();
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getPager().setPage(tablePage);

        //if selection differs to the restoring one
        boolean wasEqual = false;
        MultiSelectionModel selectionModel = (MultiSelectionModel) view.getDataGrid()
                .getSelectionModel();
        for (SupplierPotentialDemandDetail detail : (Set<
                SupplierPotentialDemandDetail>) selectionModel.getSelectedSet()) {
            if (detail.getDemandId() == selectedId) {
                wasEqual = true;
            }
        }
        this.selectedSupplierDemandId = selectedId;
        if (selectedId == -1) {
            selectionModel.clear();
        } else {
            if (!wasEqual) {
                lastOpenedPotentialDemand = -1;
                eventBus.getSupplierDemand(selectedId);
            }
        }

        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            view.getDataGrid().getDataCount(eventBus, new SearchDefinition(
                    tablePage * view.getDataGrid().getPageSize(),
                    view.getDataGrid().getPageSize(),
                    filterHolder,
                    null));
        }

        eventBus.displayView(view.getWidgetView());
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        if (this.detailSection == null) {
            this.detailSection = detailSection;
            this.detailSection.initDetailWrapper(view.getDataGrid(), view.getDetailPanel());
            this.detailSection.getView().getReplyHolder().getOfferReplyBtn().setVisible(true);
            this.detailSection.setTabVisibility(DetailsWrapperPresenter.SUPPLIER, false);
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierDemands(List<IUniversalDetail> data) {
        GWT.log("++ onResponseSupplierPotentialDemands");

        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), data);

        if (selectedSupplierDemandId != -1) {
            eventBus.getSupplierDemand(lastOpenedPotentialDemand);
        }
    }

    public void onSelectSupplierDemand(SupplierPotentialDemandDetail detail) {
        eventBus.setHistoryStoredForNextOne(false);
        textFieldUpdater.update(-1, detail, null);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(SupplierPotentialDemandDetail detail) {
        detailSection.requestDemandDetail(detail.getDemandId());
        detailSection.requestConversation(detail.getThreadRootId(), Storage.getUser().getUserId());
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    public void tableRangeChangeHandler() {
        view.getDataGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                eventBus.createTokenForHistory(view.getPager().getPage(), selectedSupplierDemandId);
            }
        });
    }
    // TableWidget handlers

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
        view.getDataGrid().getStarColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, Boolean>() {
                    @Override
                    public void update(int index, IUniversalDetail object, Boolean value) {
                        object.setStarred(!value);
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

    public void addSendOfferColumnFieldUpdater() {
        view.getDataGrid().getSendOfferImageColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, ImageResource>() {
                    @Override
                    public void update(int index, IUniversalDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addOfferReply();
                    }
                });
    }

    public void addColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<SupplierPotentialDemandDetail, Object>() {
            @Override
            public void update(int index, SupplierPotentialDemandDetail object, Object value) {
                //getUserMessageDetail() -> getOfferDetail() due to fake data
//                if (lastOpenedPotentialDemand != object.getDemandId()) {
//                    lastOpenedPotentialDemand = object.getDemandId();
                object.setRead(true);
//                    view.getDataGrid().redraw();
                displayDetailContent(object);
                MultiSelectionModel selectionModel = (MultiSelectionModel) view.getDataGrid()
                        .getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
                eventBus.createTokenForHistory(
                        view.getPager().getPage(),
                        object.getDemandId());
//                }
            }
        };
        view.getDataGrid().getClientNameColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
//        view.getDataGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getDataGrid().getUrgencyColumn().setFieldUpdater(textFieldUpdater);
//        view.getDataGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Widget action handlers
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