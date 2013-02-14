package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homedemands.HomeDemandsSearchView;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
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

        //Detail
        void displayDemandDetail(FullDemandDetail fullDemandDetail);

        void hideDemandDetail();

        //Filter
        DecoratorPanel getFilterLabelPanel();

        Label getFilterLabel();

        //Buttons
        Button getApproveBtn();

        //Other
        Widget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Class attributes. **/
    private SearchModuleDataHolder searchDataHolder;
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
    /* Navigation events                                                      */
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
        fieldUpdaterHandlers();
        selectioModelChangeHandler();
        approveDemandsButtonClickHandler();
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

    public void fieldUpdaterHandlers() {
        textFieldUpdater = new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
                fieldUpdaterHandlersInner(object);
            }
        };
        view.getDemnadTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getLocalityColumn().setFieldUpdater(textFieldUpdater);
        view.getCreatedDateColumn().setFieldUpdater(textFieldUpdater);
    }

    private void fieldUpdaterHandlersInner(FullDemandDetail object) {
        MultiSelectionModel selectionModel = view.getSelectionModel();
        selectionModel.setSelected(object, true);
        view.displayDemandDetail(object);
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

    private void approveDemandsButtonClickHandler() {
        view.getApproveBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestApproveDemands(view.getDataGrid(), view.getSelectionModel().getSelectedSet());
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
}