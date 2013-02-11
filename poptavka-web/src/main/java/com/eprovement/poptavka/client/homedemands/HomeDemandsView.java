/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategoryTreeViewModel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.celltree.CustomCellTree;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Home demands module's view.
 *
 * @author praso, Martin Slavkovsky
 */
public class HomeDemandsView extends OverflowComposite
        implements ReverseViewInterface<HomeDemandsPresenter>, HomeDemandsPresenter.HomeDemandsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static HomeDemandsViewUiBinder uiBinder = GWT.create(HomeDemandsViewUiBinder.class);

    interface HomeDemandsViewUiBinder extends UiBinder<Widget, HomeDemandsView> {
    }
    /**************************************************************************/
    /* Home Supplier presenter                                                */
    /**************************************************************************/
    private HomeDemandsPresenter homeDemandsPresenter;

    @Override
    public void setPresenter(HomeDemandsPresenter presenter) {
        this.homeDemandsPresenter = presenter;
    }

    @Override
    public HomeDemandsPresenter getPresenter() {
        return homeDemandsPresenter;
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //Table constants
    private static final String LOCALITY_COL_WIDTH = "150px";
    //Table definitions
    @UiField(provided = true) UniversalAsyncGrid<FullDemandDetail> dataGrid;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "createdDate", "title", "locality", "endDate"
            });
    //Pager
    @UiField(provided = true) UniversalPagerWidget pager;
    //CellTree
    @UiField(provided = true) CellTree cellTree;
    //Using category detail key provider in selection model, allow us to have displayed
    //alwas only one node. The other are automaticaly closed.
    private final SingleSelectionModel<CategoryDetail> selectionCategoryModel =
            new SingleSelectionModel<CategoryDetail>(CategoryDetail.KEY_PROVIDER);
    // Others
    @UiField Label bannerLabel, filterLabel;
    @UiField DecoratorPanel filterLabelPanel;
    @UiField DemandDetailView demandDetail;
    @UiField Button offerBtn;
    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);
    private DateTimeFormat formatter = DateTimeFormat.getFormat(Storage.MSGS.formatDate());

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initTableAndPager();
        initCellTree();
        initWidget(uiBinder.createAndBindUi(this));

        bannerLabel.setVisible(true);
        demandDetail.setVisible(false);
        //set offerBtn1 enabled to false as default
        offerBtn.setEnabled(false);

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    public void initCellTree() {
        //Workaround for issue: CellTree disappeared when clicking but outside tree nodes
        CellTree.Resources resource = GWT.create(CustomCellTree.class);
        StyleInjector.injectAtEnd("." + resource.cellTreeStyle().cellTreeTopItem() + " {margin-top: 0px;}");
        cellTree = new CellTree(new CategoryTreeViewModel(
                selectionCategoryModel,
                homeDemandsPresenter.getCategoryService(),
                homeDemandsPresenter.getEventBus(),
                Constants.WITHOUT_CHECK_BOXES,
                CategoryCell.DISPLAY_COUNT_OF_DEMANDS), null, resource);
        Storage.setTree(cellTree);
        cellTree.setAnimationEnabled(true);
    }

    /**
     * Initialize this example.
     */
    private void initTableAndPager() {
        pager = new UniversalPagerWidget();
        // Create a CellTable
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        dataGrid = new UniversalAsyncGrid<FullDemandDetail>(gridColumns, pager.getPageSize(), resource);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        // Selection handler
        dataGrid.setSelectionModel(new SingleSelectionModel<FullDemandDetail>());

        // bind pager to grid
        pager.setDisplay(dataGrid);

        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {

        // Date of creation
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), bundle.columnCreatedDate(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        FullDemandDetail demandDetail = (FullDemandDetail) object;
                        if (demandDetail.getCreated() == null) {
                            return Storage.MSGS.commonNotDefined();
                        } else {
                            Date now = new Date();
                            long millis = now.getTime() - demandDetail.getCreated().getTime();
                            if (millis < Storage.DAY_LENGTH) {
                                return formatter.format(demandDetail.getCreated());
                            } else if (Storage.DAY_LENGTH <= millis && millis < 2 * Storage.DAY_LENGTH) {
                                return Storage.MSGS.creationDateYesterday();
                            } else {
                                return formatter.format(demandDetail.getCreated());
                            }
                        }
                    }
                });

        // Demand Info
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), bundle.columnDemandTitle(),
                true, Constants.COL_WIDTH_TITLE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return ((FullDemandDetail) object).getTitle();
                    }
                });

        // Locality
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), bundle.columnLocality(), false, LOCALITY_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        for (LocalityDetail loc : ((FullDemandDetail) object).getLocalities()) {
                            str.append(loc.getName());
                            str.append(",\n");
                        }
                        if (!str.toString().isEmpty()) {
                            str.delete(str.length() - 2, str.length());
                        }
                        return str.toString();
                    }
                });

        // Urgence
        /**************************************************************************/
        dataGrid.addUrgentColumn(Storage.MSGS.columnUrgency());
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** CellTree. **/
    @Override
    public CellTree getCellTree() {
        return cellTree;
    }

    @Override
    public SingleSelectionModel<CategoryDetail> getSelectionCategoryModel() {
        return selectionCategoryModel;
    }

    /** Table. **/
    @Override
    public UniversalAsyncGrid<FullDemandDetail> getDataGrid() {
        return dataGrid;
    }

    @Override
    public SimplePager getPager() {
        return pager.getPager();
    }

    /** Filter. **/
    @Override
    public DecoratorPanel getFilterLabelPanel() {
        return filterLabelPanel;
    }

    @Override
    public Label getFilterLabel() {
        return filterLabel;
    }

    /** Buttons. **/
    @Override
    public Button getOfferBtn() {
        return offerBtn;
    }

    /** Other. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void displayDemandDetail(FullDemandDetail fullDemandDetail) {
        bannerLabel.setVisible(false);
        demandDetail.setVisible(true);

        demandDetail.setDemanDetail(fullDemandDetail);
    }

    @Override
    public void hideDemandDetail() {
        bannerLabel.setVisible(true);
        demandDetail.setVisible(false);
    }
}
