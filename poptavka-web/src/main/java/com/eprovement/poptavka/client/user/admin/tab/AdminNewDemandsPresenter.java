package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homedemands.HomeDemandsSearchView;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.admin.detail.AdminDetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

/**
 * Module description:.
 * Data retrieving handle categorySelectionHandler.
 * TODO add description of this module.
 * All changes to CellTree, Table range (Pager), Table Selection is stored to history.
 *
 * @author praso, Martin Slavkovsky
 */
@Presenter(view = AdminNewDemandsView.class)
public class AdminNewDemandsPresenter
        extends LazyPresenter<AdminNewDemandsPresenter.AdminNewDemandsViewInterface, AdminEventBus>
        implements NavigationConfirmationInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface AdminNewDemandsViewInterface extends LazyView, IsWidget {

        //Table
        UniversalAsyncGrid<FullDemandDetail> getDataGrid();

        Header getCheckHeader();

        Column<FullDemandDetail, String> getCreatedDateColumn();

        Column<FullDemandDetail, String> getDemnadTitleColumn();

        Column<FullDemandDetail, String> getLocalityColumn();

        MultiSelectionModel<FullDemandDetail> getSelectionModel();

        SimplePager getPager();

        //Filter
        DecoratorPanel getFilterLabelPanel();

        Label getFilterLabel();

        //Buttons
        Button getApproveBtn();

        Button getCreateConversationBtn();

        //Other
        void loadingDivShow(Widget holderWidget);

        void loadingDivHide(Widget holderWidget);

        SimplePanel getDetailPanel();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Class attributes. **/
    private AdminDetailsWrapperPresenter detailSection;
    private SearchModuleDataHolder searchDataHolder;
    private FullDemandDetail selectedDemandObject;
    private FieldUpdater textFieldUpdater;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        //nothing
    }

    public void onForward() {
        eventBus.setBody(view.getWidgetView());
        //This sets content of tab: current view attribute selector in popup.
        //However demands attribute selector is already loaded by default in first tab,
        //another setting in fourth tab is not needed
        eventBus.setUpSearchBar(new HomeDemandsSearchView());
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Main Navigation method called either by default application startup or by searching mechanism.
     * @param searchModuleDataHolder - if searching is needed, this object holds conditions to do so.
     *                               - it's also used as pointer to differ root and child sections
     */
    public void onInitNewDemands(SearchModuleDataHolder searchModuleDataHolder) {
        if (searchModuleDataHolder == null) {
            eventBus.setUpSearchBar(null);
        }
        Storage.setCurrentlyLoadedView(Constants.ADMIN_NEW_DEMANDS);
        searchDataHolder = searchModuleDataHolder;
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    /**************************************************************************/
    /* Initialization - Details Wrapper                                       */
    /**************************************************************************/
    /**
     * Response method to requesting admin details wrapper instance.
     * Initialize details wrapper and initialize details tabs according to
     * selectedDemandObject.
     * @param detailSection Details wrapper instance.
     */
    public void onResponseAdminDetailWrapperPresenter(final AdminDetailsWrapperPresenter detailSection) {
        if (detailSection != null) {
            detailSection.initDetailWrapper(view.getDataGrid(), view.getDetailPanel());

            this.detailSection = detailSection;

            if (selectedDemandObject != null) {
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
    private void initDetailSection(FullDemandDetail demandDetail) {
        if (detailSection == null) {
            eventBus.requestAdminDetailWrapperPresenter();
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
     * @param threadRootId
     */
    private void initDetailSection(long threadRootId) {
        if (detailSection == null) {
            eventBus.requestAdminDetailWrapperPresenter();
        } else {
            detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.BLOCK);
            detailSection.initDetails(
                    selectedDemandObject.getDemandId(),
                    threadRootId);
        }
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Events description.
     * - DataGrid.RangeChange
     *        - creates token when table page was changed
     *        - invoked when table page changed
     */
    @Override
    public void bindView() {
        addCheckHeaderUpdater();
        addTableSelectionModelClickHandler();
        fieldUpdaterHandlers();
        selectioModelChangeHandler();
        buttonClickHandlers();
    }

    /**************************************************************************/
    /* Bind Handlers                                                          */
    /**************************************************************************/
    public void addCheckHeaderUpdater() {
        view.getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<FullDemandDetail> rows = view.getDataGrid().getVisibleItems();
                for (FullDemandDetail row : rows) {
                    ((MultiSelectionModel) view.getDataGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addTableSelectionModelClickHandler() {
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //set actionBox visibility

                //init details
                if (getSelectedDemandIds().size() == 1) {
                    view.getApproveBtn().setVisible(true);
                    FullDemandDetail selected = view.getSelectionModel().getSelectedSet().iterator().next();
                    selectedDemandObject = selected;
                    initDetailSection(selected);
                    eventBus.requestConversationForAdmin(selected.getDemandId());
                } else {
                    view.getApproveBtn().setVisible(false);
                    view.getCreateConversationBtn().setVisible(false);
                    detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.NONE);
                }
            }
        });
    }

    public void fieldUpdaterHandlers() {
        textFieldUpdater = new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
                MultiSelectionModel selectionModel = (MultiSelectionModel) view.getDataGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
            }
        };
        view.getDemnadTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getLocalityColumn().setFieldUpdater(textFieldUpdater);
        view.getCreatedDateColumn().setFieldUpdater(textFieldUpdater);
    }

    /**
     * Display demand detail in detail view when selected by user.
     */
    private void selectioModelChangeHandler() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.getApproveBtn().setVisible(!view.getSelectionModel().getSelectedSet().isEmpty());
            }
        });
    }

    private void buttonClickHandlers() {
        view.getApproveBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestApproveDemands(view.getDataGrid(), view.getSelectionModel().getSelectedSet());
            }
        });
        view.getCreateConversationBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestCreateConversation(
                        view.getSelectionModel().getSelectedSet().iterator().next().getDemandId());
            }
        });
    }

    /**************************************************************************/
    /* Additional methods                                                     */
    /**************************************************************************/
    /**
     * Display demands of selected category.
     * @param list
     */
    public void onDisplayAdminNewDemands(List<FullDemandDetail> list) {
        view.getDataGrid().getDataProvider().updateRowData(view.getDataGrid().getStart(), list);
    }

    public void onResponseCreateConversation(long threadRootId) {
        initDetailSection(threadRootId);
    }

    public void onResponseConversationForAdmin(List<MessageDetail> conversation) {
        if (conversation.isEmpty()) {
            view.getCreateConversationBtn().setVisible(true);
            initDetailSection(selectedDemandObject);
        } else {
            view.getCreateConversationBtn().setVisible(false);
            eventBus.requestThreadRootId(selectedDemandObject.getDemandId());
        }
    }

    public void onResponseThreadRootId(long threadRootId) {
        initDetailSection(threadRootId);
    }

    public List<Long> getSelectedDemandIds() {
        List<Long> idList = new ArrayList<Long>();
        for (FullDemandDetail detail : view.getSelectionModel().getSelectedSet()) {
            idList.add(detail.getDemandId());
        }
        return idList;
    }
}