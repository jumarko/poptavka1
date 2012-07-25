/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Presenter(view = ClientAssignedProjectsView.class)
public class ClientAssignedProjectsPresenter extends LazyPresenter<
        ClientAssignedProjectsPresenter.ClientAssignedProjectsLayoutInterface, ClientDemandsEventBus> {

    public interface ClientAssignedProjectsLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalAsyncGrid<ClientProjectContestantDetail> getContestantsGrid();

        //Columns
        Column<ClientProjectContestantDetail, Boolean> getCheckColumn();

        Column<ClientProjectContestantDetail, Boolean> getStarColumn();

        Column<ClientProjectContestantDetail, String> getSupplierNameColumn();

        Column<ClientProjectContestantDetail, String> getPriceColumn();

        Column<ClientProjectContestantDetail, String> getReceivedColumn();

        Column<ClientProjectContestantDetail, String> getRatingColumn();

        Column<ClientProjectContestantDetail, String> getAcceptedColumn();

        //Header
        Header getCheckHeader();

        //Buttons
        Button getCloseBtn();

        Button getReplyBtn();

        //ListBox
        ListBox getActions();

        //Other
        int getContestPageSize();

        List<Long> getSelectedIdList();

        Set<ClientProjectContestantDetail> getSelectedMessageList();

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
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedProjectContest = -1;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Listbox actions
        addActionChangeHandler();
        // Buttons Actions
        addCloseBtnClickHandler();
        addReplyBtnClickHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientAssignedProjects(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_ASSIGNED_PROJECTS);
        searchDataHolder = filter;
        view.getContestantsGrid().getDataCount(eventBus, searchDataHolder);

        view.getWidgetView().asWidget().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (detailSection == null) {
            detailSection = eventBus.addHandler(DetailsWrapperPresenter.class);
            detailSection.initDetailWrapper(view.getWrapperPanel(), type);
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**
     * DEVEL METHOD
     *
     * Used for JRebel correct refresh. It is called from DemandModulePresenter, when removing instance of
     * SupplierListPresenter. it has to remove it's detailWrapper first.
     */
    public void develRemoveDetailWrapper() {
        detailSection.develRemoveReplyWidget();
        eventBus.removeHandler(detailSection);
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientAssignedProjects(List<ClientProjectContestantDetail> data) {
        GWT.log("++ onResponseClientsOfferedProjects");

        view.getContestantsGrid().updateRowData(data);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(ClientProjectContestantDetail detail) {
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
                List<ClientProjectContestantDetail> rows = view.getContestantsGrid().getVisibleItems();
                for (ClientProjectContestantDetail row : rows) {
                    ((MultiSelectionModel) view.getContestantsGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getStarColumn().setFieldUpdater(new FieldUpdater<ClientProjectContestantDetail, Boolean>() {

            @Override
            public void update(int index, ClientProjectContestantDetail object, Boolean value) {
//              TableDisplay obj = (TableDisplay) object;
                object.setStarred(!value);
                view.getContestantsGrid().redraw();
                Long[] item = new Long[]{object.getUserMessageId()};
                eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
            }
        });
    }

    public void addTextColumnFieldUpdaters() {
        FieldUpdater textFieldUpdater = new FieldUpdater<ClientProjectContestantDetail, String>() {

            @Override
            public void update(int index, ClientProjectContestantDetail object, String value) {
                if (lastOpenedProjectContest != object.getUserMessageId()) {
                    lastOpenedProjectContest = object.getUserMessageId();
                    object.setRead(true);
                    view.getContestantsGrid().redraw();
                    displayDetailContent(object);
                }
            }
        };
        view.getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getAcceptedColumn().setFieldUpdater(textFieldUpdater);
        view.getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

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

    //Buttons
    private void addCloseBtnClickHandler() {
        view.getCloseBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    private void addReplyBtnClickHandler() {
        view.getReplyBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
}