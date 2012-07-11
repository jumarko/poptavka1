package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.main.Constants;
import com.google.gwt.event.dom.client.ChangeEvent;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.List;

@Presenter(view = HomeSuppliersView.class)
public class HomeSuppliersPresenter
        extends LazyPresenter<HomeSuppliersPresenter.SuppliersViewInterface, HomeSuppliersEventBus> {

    public interface SuppliersViewInterface extends LazyView, IsWidget {
        //******** ROOT SECTION **********

        HorizontalPanel getRootSection();

        SingleSelectionModel getSelectionRootModel();

        void displayRootCategories(int columns, ArrayList<CategoryDetail> categories);

        //******** CHILD SECTION **********
        HTMLPanel getChildSection();

        Label getFilterLabel();

        Label getCategoryLoadingLabel();

        CellList getCategoriesList();

        int getPageSize();

        ListBox getPageSizeCombo();

        Button getContactBtn();

        FlowPanel getPath();

        void addPath(Widget widget);

        void removePath(); //removes last one

        UniversalAsyncGrid getDataGrid();

        SimplePager getPager();

        Widget getWidgetView();

        SingleSelectionModel getSelectionCategoryModel();

        SplitLayoutPanel getSplitter();

        void displaySuppliersDetail(FullSupplierDetail userDetail);

        void hideSuppliersDetail();
    }
    //differ if category was selected from menu, or from path
    private Boolean wasSelection = false;
    //others
    //columns number of root chategories in parent widget
    private static final int COLUMNS = 4;
    private SearchModuleDataHolder searchDataHolder = null;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        eventBus.setUpSearchBar(new HomeSuppliersViewView(), false, true, true);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * @param searchModuleDataHolder - if searching is needed, this object holds conditions to do so.
     *                               - it's also used as pointer to differ root and child sections
     */
    public void onGoToHomeSuppliersModule(SearchModuleDataHolder searchModuleDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.HOME_SUPPLIERS);
        this.searchDataHolder = searchModuleDataHolder;

        this.onDisplayParentOrChild(searchModuleDataHolder);
    }

    public void onDisplayParentOrChild(SearchModuleDataHolder searchModuleDataHolder) {
        this.searchDataHolder = searchModuleDataHolder;
        //ROOT section
        if (searchDataHolder == null) {
            //display root categories on whole page
            view.getFilterLabel().setVisible(false);
            view.getChildSection().setVisible(false);
            view.getRootSection().setVisible(true);

            // add root to path
            eventBus.addToPath(new CategoryDetail(0L, ""));
            //get Root Categories
            //If root categories already loaded and returned by history -> no need to load again
            if (view.getRootSection().getWidgetCount() == 0) {
                view.getRootSection().add(new Label(Storage.MSGS.loadingRootCategories()));
                eventBus.getCategories();
            } else {
                //Cancel selection of root categories if returned by history, otherwise "SelectionChangeHandler"
                //event won't be fired (user won't be able to choose the same category).
                view.getSelectionRootModel().setSelected(view.getSelectionRootModel().getSelectedObject(), false);
            }
            //CHILD section
        } else {
            view.getFilterLabel().setVisible(true);
            view.getChildSection().setVisible(true);
            view.getRootSection().setVisible(false);
            view.getCategoryLoadingLabel().setText(Storage.MSGS.loadingCategories());
            view.getCategoryLoadingLabel().setVisible(true);
            view.getCategoriesList().setVisible(false);
//            dataProvider.updateRowCount(0, false);
            // create path
            eventBus.getCategoryParents(searchDataHolder.getCategories().get(0).getId());
            // get Sub Categories
            eventBus.getSubCategories(searchDataHolder.getCategories().get(0).getId());
        }
    }

    @Override
    public void bindView() {
        view.getSelectionRootModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (view.getDataGrid().getDataProvider() != null) {
                    view.getDataGrid().getDataProvider().updateRowCount(0, false);
                }

                CategoryDetail selected = (CategoryDetail) view.getSelectionRootModel().getSelectedObject();

                if (selected != null) {
                    wasSelection = true;
                    eventBus.displayChildWidget(selected.getId());
                    if (searchDataHolder == null) {
                        searchDataHolder = new SearchModuleDataHolder();
                    }
                    searchDataHolder.getCategories().clear();
                    searchDataHolder.getCategories().add(new CategoryDetail(selected.getId(), selected.getName()));
                    eventBus.addToPath(selected);
                }
            }
        });
        view.getSelectionCategoryModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.getDataGrid().getDataProvider().updateRowCount(0, false);

                CategoryDetail selected = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

                if (selected != null) {
                    view.getCategoryLoadingLabel().setText(Storage.MSGS.loadingCategories());
                    view.getCategoryLoadingLabel().setVisible(true);
                    view.getCategoriesList().setVisible(false);
                    wasSelection = true;
                    view.hideSuppliersDetail();
                    view.getDataGrid().getSelectionModel().setSelected(
                            ((SingleSelectionModel) view.getDataGrid().getSelectionModel()).getSelectedObject(), false);

                    if (searchDataHolder == null) {
                        searchDataHolder = new SearchModuleDataHolder();
                    }
                    searchDataHolder.getCategories().clear();
                    searchDataHolder.getCategories().add(selected);
                    eventBus.getSubCategories(selected.getId());
                    eventBus.addToPath(selected);
                }
            }
        });
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FullSupplierDetail selected =
                        (FullSupplierDetail) ((SingleSelectionModel) view.getDataGrid().getSelectionModel())
                        .getSelectedObject();

                if (selected != null) {
                    view.displaySuppliersDetail(selected);
                }
            }
        });
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                view.getDataGrid().setRowCount(0, true);

                int newPage = Integer.valueOf(view.getPageSizeCombo().
                        getItemText(view.getPageSizeCombo().getSelectedIndex()));

                view.getDataGrid().setRowCount(newPage, true);

                int page = view.getPager().getPageStart() / view.getPager().getPageSize();

                view.getPager().setPageStart(page * newPage);
                view.getPager().setPageSize(newPage);
            }
        });

    }

    /**************************************************************************/
    /* Additional events used to display data                                 */
    /**************************************************************************/
    /* ROOT CATEGORIES */
    /**
     * Methods displays root categories. These categories are divided into cellLists (columns),
     * where number of columns depends on constant: COLUMNS.
     * @param rootCategories - root categories to be displayed
     */
    public void onDisplayRootcategories(ArrayList<CategoryDetail> rootCategories) {
        view.displayRootCategories(COLUMNS, rootCategories);
    }

    /**
     * Cares for displaying sub categories of chosen parent category and displaying suppliers for
     * chosen selection of sub categories and parent category.
     * @param subcategories - subcategories of selected parentCategory to be displayed
     * @param parentCategory - parent category of returned subcategories. Need for retrieving data from DB.
     */
    /* SUB CATEGORIES */
    public void onDisplaySubCategories(ArrayList<CategoryDetail> subcategories, Long parentCategory) {
        view.getCategoryLoadingLabel().setVisible(false);
        view.getCategoriesList().setVisible(true);
        //Force Loading indicator to show up when new data is retrieving
        view.getCategoriesList().setRowCount(subcategories.size(), true);
        view.getCategoriesList().setRowData(0, subcategories);

        if (!wasSelection) { // ak nebola vybrana kategoria zo zoznamu, ale klik na hyperlink na vyvolanie historie
            searchDataHolder.getCategories().add(new CategoryDetail(parentCategory, ""));
        }
        view.getDataGrid().getDataCount(eventBus, searchDataHolder);
        wasSelection = false;
    }

    /**
     * Display suppliers of selected category.
     * @param list
     */
    /* SUPPLIERS */
    public void onDisplaySuppliers(List<FullSupplierDetail> list) {
        // TODO Praso - neviem ci tu musi byt ten flush alebo nie? Aky ma vyznam?
        // TODO Martin - zakomentovane flush, zatial nerobi problemi pri zobrazovani,
        //               ak ok, moze sa to vyhodic uplne.
        view.getDataGrid().getDataProvider().updateRowData(view.getDataGrid().getStart(), list);
//        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    /**
     * Called from suppliers display widget root by click on one of root category.
     * Methods initialize Path, fills category list of Suppliers widget.
     * @param category selected from root categories in parent widget
     */
    /* CHILD WIDGET */
    public void onDisplayChildWidget(Long id) {



        view.getChildSection().setVisible(true);
        view.getRootSection().setVisible(false);

        view.getDataGrid().redraw();

        eventBus.getSubCategories(id);
    }

    /**
     * Manages path and history calls. Adds and removes items from path according to history.
     * @param categoryDetail - category from url to be loaded
     */
    /* PATH UPDATE */
    public void onUpdatePath(ArrayList<CategoryDetail> categories) {
        view.getPath().clear();

        //Root
        Hyperlink rootLink = new Hyperlink("root",
                getTokenGenerator().addToPath(new CategoryDetail(0L, "")));
        rootLink.setStylePrimaryName(StyleResource.INSTANCE.common().hyperlinkInline());
        view.addPath(rootLink);

        //Anything else
        for (int i = categories.size() - 1; i > 0; i--) {
            String token = getTokenGenerator().addToPath(categories.get(i));
            Hyperlink subLink = new Hyperlink(" -> " + categories.get(i).getName(), token);
            subLink.setStylePrimaryName(StyleResource.INSTANCE.common().hyperlinkInline());
            view.addPath(subLink);
        }

        //Last
        // for last user addToPath method to generate token
        eventBus.addToPath(categories.get(0));
    }

    /**
     * Adds hyperlink to path.
     * @param categoryDetail
     */
    /* PATH ADD */
    public void onAddToPath(CategoryDetail categoryDetail) {
        String symbol = "";
        //Root
        if (categoryDetail.getId() == 0) {
            view.getPath().clear();
            symbol = "root";
        } else {
            symbol = " -> ";
        }
        //Anything else
        String token = getTokenGenerator().addToPath(categoryDetail);
        Hyperlink link = new Hyperlink(symbol + categoryDetail.getName(), token);

        link.setStylePrimaryName(StyleResource.INSTANCE.common().hyperlinkInline());
        view.addPath(link);
    }
}