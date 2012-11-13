/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
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
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Presenter(view = ClientAssignedDemandsView.class)
public class ClientAssignedDemandsPresenter extends LazyPresenter<
        ClientAssignedDemandsPresenter.ClientAssignedDemandsLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientAssignedDemandsLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalTableWidget getTableWidget();

        //Other
        SimplePanel getWrapperPanel();

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
    private long lastOpenedAssignedDemand = -1;
    private long selectedClientAssignedDemandId = -1;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
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
        view.getTableWidget().getGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (this.detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        }
    }

    public void onInitClientAssignedDemandsByHistory(
            int parentTablePage, long parentId, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_ASSIGNED_DEMANDS);
        //Select Menu - my demands - selected
        eventBus.selectClientDemandsMenu(Constants.CLIENT_ASSIGNED_DEMANDS);
        //
        //If current page differ to stored one, cancel events that would be fire automatically but with no need
        if (view.getTableWidget().getPager().getPage() != parentTablePage) {
            //cancel range change event in asynch data provider
            view.getTableWidget().getGrid().cancelRangeChangedEvent();
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getTableWidget().getPager().setPage(parentTablePage);
        //if selection differs to the restoring one
        boolean wasEqual = false;
        MultiSelectionModel selectionModel = (MultiSelectionModel) view.getTableWidget()
                .getGrid().getSelectionModel();
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
    public void onDisplayClientAssignedDemands(List<FullOfferDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemands");

        view.getTableWidget().getGrid().getDataProvider().updateRowData(
                view.getTableWidget().getGrid().getStart(), data);

        if (selectedClientAssignedDemandId != -1) {
            eventBus.getClientAssignedDemand(selectedClientAssignedDemandId);
        }
    }

    public void onSelectClientAssignedDemand(FullOfferDetail detail) {
//        view.getTableWidget().getGrid().getSelectionModel().setSelected(detail, true);
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
        view.getTableWidget().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<FullOfferDetail> rows = view.getTableWidget().getGrid().getVisibleItems();
                for (FullOfferDetail row : rows) {
                    ((MultiSelectionModel) view.getTableWidget().getGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getTableWidget().getStarColumn().setFieldUpdater(new FieldUpdater<FullOfferDetail, Boolean>() {
            @Override
            public void update(int index, FullOfferDetail object, Boolean value) {
                object.setStarred(!value);
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

    public void addCloseDemandColumnFieldUpdater() {
        view.getTableWidget().getCloseDemandImageColumn().setFieldUpdater(
                new FieldUpdater<FullOfferDetail, ImageResource>() {
                    @Override
                    public void update(int index, FullOfferDetail object, ImageResource value) {
                        eventBus.requestCloseDemand(object.getDemandDetail());
                    }
                });
    }

    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<FullOfferDetail, String>() {
            @Override
            public void update(int index, FullOfferDetail object, String value) {
                if (lastOpenedAssignedDemand != object.getOfferDetail().getDemandId()) {
                    lastOpenedAssignedDemand = object.getOfferDetail().getDemandId();
                    object.setRead(true);
//                    view.getTableWidget().getGrid().redraw();
                    displayDetailContent(object);
                    MultiSelectionModel selectionModel = (MultiSelectionModel) view.getTableWidget()
                            .getGrid().getSelectionModel();
                    selectionModel.clear();
                    selectionModel.setSelected(object, true);
                    eventBus.createTokenForHistory3(
                            view.getTableWidget().getPager().getPage(),
                            object.getOfferDetail().getDemandId());
                }
            }
        };
        view.getTableWidget().getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getDeliveryColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

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