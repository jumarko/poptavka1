/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Presenter(view = SupplierDemandsView.class)
public class SupplierDemandsPresenter extends LazyPresenter<
        SupplierDemandsPresenter.SupplierDemandsLayoutInterface, SupplierDemandsModuleEventBus> {

    public interface SupplierDemandsLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalTableWidget getTableWidget();

        SimplePanel getDetailPanel();

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private ViewType type = ViewType.EDITABLE;
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater;
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemandContest = -1;
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
        view.getTableWidget().getGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (this.detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        }
    }

    public void onInitSupplierDemandsByHistory(int tablePage, long selectedId, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        //Select Menu - my demands - selected
        eventBus.selectSupplierDemandsMenu(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        //
        //If current page differ to stored one, cancel events that would be fire automatically but with no need
        if (view.getTableWidget().getPager().getPage() != tablePage) {
            //cancel range change event in asynch data provider
            view.getTableWidget().getGrid().cancelRangeChangedEvent();
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getTableWidget().getPager().setPage(tablePage);

        //if selection differs to the restoring one
        boolean wasEqual = false;
        MultiSelectionModel selectionModel = (MultiSelectionModel) view.getTableWidget()
                .getGrid().getSelectionModel();
        for (FullOfferDetail offer : (Set<
                FullOfferDetail>) selectionModel.getSelectedSet()) {
            if (offer.getOfferDetail().getDemandId() == selectedId) {
                wasEqual = true;
            }
        }
        this.selectedSupplierDemandId = selectedId;
        if (selectedId != -1 && !wasEqual) {
            selectionModel.clear();
            lastOpenedDemandContest = -1;
            eventBus.getSupplierDemand(selectedId);
        }

        if (Storage.isAppCalledByURL()) {
            view.getTableWidget().getGrid().getDataCount(eventBus, new SearchDefinition(
                    tablePage * view.getTableWidget().getGrid().getPageSize(),
                    view.getTableWidget().getGrid().getPageSize(),
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
            this.detailSection.initDetailWrapper(view.getDetailPanel(), type);
            this.detailSection.getView().getReplyHolder().getOfferReplyBtn().setVisible(true);
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierDemands(List<FullOfferDetail> data) {
        GWT.log("++ onResponseSupplierPotentialDemands");

        view.getTableWidget().getGrid().getDataProvider().updateRowData(
                view.getTableWidget().getGrid().getStart(), data);

        if (selectedSupplierDemandId != -1) {
            eventBus.getSupplierDemand(lastOpenedDemandContest);
        }
    }

    public void onSelectSupplierDemand(FullOfferDetail detail) {
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
    public void displayDetailContent(FullOfferDetail detail) {
//        detailSection.requestDemandDetail(detail.getDemandId(), type);
        detailSection.requestDemandDetail(123L, type);

//        detailSection.requestSupplierDetail(detail.getSupplierId(), type);
        detailSection.requestSupplierDetail(142811L, type);

//        detailSection.requestContest(detail.getMessageId(),
//                detail.getUserMessageId(), Storage.getUser().getUserId());
        detailSection.requestConversation(124L, 289L, 149L);
    }

    public void onSendMessageResponse(MessageDetail sentMessage, ViewType handlingType) {
        //neccessary check for method to be executed only in appropriate presenter
        if (type.equals(handlingType)) {
//            detailSection.addConversationMessage(sentMessage);
        }
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    public void tableRangeChangeHandler() {
        view.getTableWidget().getGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                eventBus.createTokenForHistory(
                        view.getTableWidget().getPager().getPage(), selectedSupplierDemandId);
            }
        });
    }
    // TableWidget handlers

    public void addCheckHeaderUpdater() {
        view.getTableWidget().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<FullOfferDetail> rows = view.getTableWidget().getGrid().getVisibleItems();
                for (FullOfferDetail row : rows) {
                    ((MultiSelectionModel) view.getTableWidget().getGrid()
                            .getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getTableWidget().getStarColumn().setFieldUpdater(
                new FieldUpdater<FullOfferDetail, Boolean>() {
                    @Override
                    public void update(int index, FullOfferDetail object, Boolean value) {
                        object.getUserMessageDetail().setStarred(!value);
                        view.getTableWidget().getGrid().redraw();
                        Long[] item = new Long[]{object.getUserMessageDetail().getId()};
                        eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
                    }
                });
    }

    public void addReplyColumnFieldUpdater() {
        view.getTableWidget().getReplyImageColumn().setFieldUpdater(
                new FieldUpdater<FullOfferDetail, ImageResource>() {
                    @Override
                    public void update(int index, FullOfferDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addQuestionReply();
                    }
                });
    }

    public void addSendOfferColumnFieldUpdater() {
        view.getTableWidget().getSendOfferImageColumn().setFieldUpdater(
                new FieldUpdater<FullOfferDetail, ImageResource>() {
                    @Override
                    public void update(int index, FullOfferDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addOfferReply();
                    }
                });
    }

    public void addColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<FullOfferDetail, Object>() {
            @Override
            public void update(int index, FullOfferDetail object, Object value) {
                if (lastOpenedDemandContest != object.getUserMessageDetail().getId()) {
                    lastOpenedDemandContest = object.getUserMessageDetail().getId();
                    object.getUserMessageDetail().setRead(true);
                    view.getTableWidget().getGrid().redraw();
                    displayDetailContent(object);
                }
            }
        };
        view.getTableWidget().getClientNameColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getUrgencyColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Widget action handlers
    private void addActionChangeHandler() {
        view.getTableWidget().getActionBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getTableWidget().getActionBox().getSelectedIndex()) {
                    case 1:
                        eventBus.requestReadStatusUpdate(view.getTableWidget().getSelectedIdList(), true);
                        break;
                    case 2:
                        eventBus.requestReadStatusUpdate(view.getTableWidget().getSelectedIdList(), false);
                        break;
                    case 3:
                        eventBus.requestStarStatusUpdate(view.getTableWidget().getSelectedIdList(), true);
                        break;
                    case 4:
                        eventBus.requestStarStatusUpdate(view.getTableWidget().getSelectedIdList(), false);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}