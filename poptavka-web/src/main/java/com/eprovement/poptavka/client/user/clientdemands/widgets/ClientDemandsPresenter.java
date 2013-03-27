/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.actionBox.ActionBoxView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.detail.EditableDemandDetailPresenter;
import com.eprovement.poptavka.client.user.widget.detail.EditableDemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;
import java.util.Set;

@Presenter(view = ClientDemandsView.class, multiple = true)
public class ClientDemandsPresenter
        extends LazyPresenter<ClientDemandsPresenter.ClientDemandsLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientDemandsLayoutInterface extends LazyView, IsWidget {

        // Columns
        Column<IUniversalDetail, String> getSupplierNameColumn();

        Column<IUniversalDetail, String> getBodyPreviewColumn();

        Column<IUniversalDetail, String> getDateColumn();

        //Pagers
        SimplePager getDemandPager();

        SimplePager getConversationPager();

        //Others
        Button getEditDemandButton();

        Button getDeleteDemandButton();

        Button getBackBtn();

        //HtmlPanels
        HTMLPanel getChoiceButtonsPanel();

        UniversalAsyncGrid<ClientDemandDetail> getDemandGrid();

        UniversalTableGrid getConversationGrid();

        List<Long> getSelectedIdList();

        Set<ClientDemandConversationDetail> getSelectedMessageList();

        SimplePanel getDetailPanel();

        SimplePanel getActionBox();
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
    private DetailsWrapperPresenter detailSection;
    private EditableDemandDetailPresenter editDemandPresenter;
    private SearchModuleDataHolder searchDataHolder;
    private ClientDemandDetail selectedDemandObject;
    private IUniversalDetail selectedConversationObject;
    private long selectedClientDemandId = -1;
    private long selectedClientDemandConversationId = -1;
    //
    private FieldUpdater textFieldUpdater;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        //Buttons
        addBackBtnClickHandler();
        addButtonsClickHandlers();
        // Selection Handlers
        addDemandTableSelectionHandler();
        addConversationGridSelectionModelHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // RowStyles
        addDemandGridRowStyles();
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onInitClientDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS);
        eventBus.clientDemandsMenuStyleChange(Constants.CLIENT_DEMANDS);
        eventBus.initActionBox(view.getActionBox(), view.getConversationGrid());
        //Set visibility
        view.setConversationTableVisible(false);
        view.setDemandTableVisible(true);

        eventBus.setUpSearchBar(new Label("Client's projects attibure's selector will be here."));
        searchDataHolder = filter;
        eventBus.createTokenForHistory();

        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
        //request data to demand(parent) table
        if (view.getDemandGrid().isVisible()) {
            view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        } else {
            backBtnClickHandlerInner();
        }
    }

    /**************************************************************************/
    /* Details Wrapper                                                        */
    /**************************************************************************/
    /**
     * Response method to requesting details wrapper instance.
     * Initialize details wrapper and initialize details tabs according to
     * Some additional actions can be added here.
     * @param detailSection Details wrapper instance.
     */
    public void onResponseDetailWrapperPresenter(final DetailsWrapperPresenter detailSection) {
        if (detailSection != null) {
            detailSection.initDetailWrapper(view.getConversationGrid(), view.getDetailPanel());

            this.detailSection = detailSection;

            addDetailSelectionHandler();

            if (selectedConversationObject != null) {
                initDetailSection(selectedConversationObject);
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
            view.getDetailPanel().setVisible(true);
            detailSection.setEditDemandMode(false, null);
            detailSection.initDetails(demandDetail.getDemandId());
            view.getChoiceButtonsPanel().setVisible(true);
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
            view.getDetailPanel().setVisible(true);
            detailSection.setEditDemandMode(false, null);
            detailSection.initDetails(
                    conversationDetail.getDemandId(),
                    conversationDetail.getSupplierId(),
                    conversationDetail.getThreadRootId());
            view.getChoiceButtonsPanel().setVisible(false);
        }
    }

    /**************************************************************************/
    /* Display data                                                           */
    /**************************************************************************/
    /**
     * Display client's "My demands" and select item if requested.
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

    /**
     * Display conversations for selected demand and select item if requested.
     * If no conversations available:
     * 1) don't display empty conversation table, leave visible demand table
     * 2) display details wrapper for user to be able to edit demand.
     *
     * @param data Conversations for selected demand.
     */
    public void onDisplayClientDemandConversations(List<IUniversalDetail> data) {
        GWT.log("++ onResponseClientsDemandConversation");
        //if no data availbale, leave all as it is and dispaly details wrapper.
        if (!data.isEmpty()) {
            view.setDemandTableVisible(false);
            view.setConversationTableVisible(true);
            view.getConversationPager().startLoading();

            view.getConversationGrid().getDataProvider().updateRowData(
                    view.getConversationGrid().getStart(), data);

            if (selectedClientDemandConversationId != -1) {
                eventBus.getClientDemandConversation(selectedClientDemandConversationId);
            }
        }
    }

    /**
     * In conversation (child) table is empty for selected demand from parent table, there is
     * no need to display it. And also when it cases problem because details wrapper is
     * bind to selection on Conversation table items, therefore, if no data, no details wrapper
     * can be call automatically. Therefore we need to do it manually.
     */
    public void onResponseConversationNoData() {
        SingleSelectionModel selectionModel = (SingleSelectionModel) view.getDemandGrid().getSelectionModel();
        //init details
        if (selectionModel.getSelectedSet().isEmpty()) {
            view.getDetailPanel().setVisible(false);
        } else {
            if (detailSection == null) {
                eventBus.requestDetailWrapperPresenter();
            } else {
                view.getDetailPanel().setVisible(true);
                detailSection.initDetails(selectedDemandObject.getDemandId());
            }
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

    /**************************************************************************/
    /* Response methods                                                       */
    /**************************************************************************/
    public void onResponseDeleteDemand(boolean result) {
        //TODO LATER Martin - make proper notify popup and change layout
        backBtnClickHandlerInner();
        Window.alert("deleted succesfully");
    }

    public void onResponseUpdateDemand(FullDemandDetail result) {
        view.getChoiceButtonsPanel().setVisible(true);
        //reset & refresh view
        detailSection.setEditDemandMode(false, null);
        detailSection.getView().getDemandDetail().setDemanDetail(result);
        //refresh grid
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS);
        view.getDemandPager().startLoading();
        view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(
                view.getDemandGrid().getStart(),
                view.getDemandPager().getPageSize(),
                searchDataHolder,
                null));
        //notify user
        //TODO LATER Martin - make proper notify popup
        Window.alert("Updated succesfully");
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
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
                Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMAND_DISCUSSIONS);
                ClientDemandDetail selected = (ClientDemandDetail) ((SingleSelectionModel) view.getDemandGrid()
                        .getSelectionModel()).getSelectedObject();
                view.getChoiceButtonsPanel().setVisible(true);
                if (selected != null) {
                    selectedDemandObject = selected;
                    initDetailSection(selected);
                    Storage.setDemandId(selected.getDemandId());
                    Storage.setThreadRootId(selected.getThreadRootId());
                    view.setDemandTitleLabel(selected.getDemandTitle());
                    view.getConversationGrid().getDataCount(eventBus, null);
                }
            }
        });
    }

    /**
     * Show or Hide details section and action box.
     * Show if and only of one table row is selected.
     * Hide otherwise - not or more than one rows are selected.
     */
    public void addConversationGridSelectionModelHandler() {
        view.getConversationGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //set actionBox visibility
                view.getActionBox().setVisible(view.getConversationGrid().getSelectedUserMessageIds().size() > 0);
                //init details
                if (view.getConversationGrid().getSelectedUserMessageIds().size() == 1) {
                    selectedConversationObject = view.getConversationGrid().getSelectedObjects().get(0);
                    initDetailSection(selectedConversationObject);
                } else {
                    view.getDetailPanel().setVisible(false);
                    view.getChoiceButtonsPanel().setVisible(false);
                }
            }
        });
    }

    public void addDetailSelectionHandler() {
        detailSection.getView().getContainer().addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                view.getChoiceButtonsPanel().setVisible(event.getSelectedItem() == detailSection.DEMAND_DETAIL_TAB);
            }
        });
    }

    /** Field updater. **/
    //--------------------------------------------------------------------------
    public void addCheckHeaderUpdater() {
        view.getConversationGrid().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<IUniversalDetail> rows = view.getConversationGrid().getVisibleItems();
                for (IUniversalDetail row : rows) {
                    ((MultiSelectionModel) view.getConversationGrid()
                            .getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getConversationGrid().getStarColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, Boolean>() {
                @Override
                public void update(int index, IUniversalDetail object, Boolean value) {
                    object.setIsStarred(!value);
                    view.getConversationGrid().redrawRow(index);
                    ((ActionBoxView) view.getActionBox().getWidget()).getActionStar().getScheduledCommand().execute();
                }
            });
    }
    /**
     * Show and loads detail section. Show after clicking on certain columns that
     * have defined this fieldUpdater.
     */
    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<ClientDemandConversationDetail, String>() {
            @Override
            public void update(int index, ClientDemandConversationDetail object, String value) {
                object.setIsRead(true);
                view.setDemandTableVisible(false);
                view.setConversationTableVisible(true);

                MultiSelectionModel selectionModel = view.getConversationGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
            }
        };
        view.getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getBodyPreviewColumn().setFieldUpdater(textFieldUpdater);
        view.getDateColumn().setFieldUpdater(textFieldUpdater);
    }

    //Button handlers
    private void addBackBtnClickHandler() {
        view.getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                backBtnClickHandlerInner();
            }
        });
    }

    private void backBtnClickHandlerInner() {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS);
        if (detailSection != null) {
            view.getDetailPanel().setVisible(false);
        }
        selectedDemandObject = null;
        selectedConversationObject = null;
        view.getConversationGrid().getSelectionModel().clear();
        view.getChoiceButtonsPanel().setVisible(false);
        view.getDemandPager().startLoading();
        view.setConversationTableVisible(false);
        view.setDemandTableVisible(true);
        view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    private void addButtonsClickHandlers() {
        view.getEditDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                detailSection.setEditDemandMode(true, getEditableDemandPresetner());
            }
        });
        view.getDeleteDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getChoiceButtonsPanel().setVisible(false);
                eventBus.requestDeleteDemand(selectedDemandObject.getDemandId());
            }
        });
    }

    private void bindEditDemandHandlers() {
        editDemandPresenter.getView().getSubmitButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (editDemandPresenter.getView().isValid()) {
                    eventBus.requestUpdateDemand(selectedDemandObject.getDemandId(),
                            editDemandPresenter.updateDemandDetail(new FullDemandDetail()));
                }
            }
        });
        editDemandPresenter.getView().getCancelButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                detailSection.setEditDemandMode(false, null);
            }
        });
    }

    /** RowStyles. **/
    private void addDemandGridRowStyles() {
        view.getDemandGrid().setRowStyles(new RowStyles<ClientDemandDetail>() {
            @Override
            public String getStyleNames(ClientDemandDetail row, int rowIndex) {
                if (row.getUnreadSubmessagesCount() > 0) {
                    return Storage.RSCS.grid().unread();
                }
                return "";
            }
        });
    }

    /** Others. **/
    private EditableDemandDetailView getEditableDemandPresetner() {
        if (editDemandPresenter == null) {
            editDemandPresenter = eventBus.addHandler(EditableDemandDetailPresenter.class);
            bindEditDemandHandlers();
        }
        EditableDemandDetailView view = (EditableDemandDetailView) editDemandPresenter.getView();
        view.resetFields();
        view.setDemanDetail(detailSection.getView().getDemandDetail().getDemandDetail());
        return view;
    }

}
