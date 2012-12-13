package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

@Presenter(view = CategorySelectorView.class, multiple = true)
public class CategorySelectorPresenter
        extends LazyPresenter<CategorySelectorPresenter.CategorySelectorInterface, RootEventBus> {

    @Inject
    private CategoryRPCServiceAsync categoryService;

    public interface CategorySelectorInterface extends LazyView {

        void createCellBrowser(int checkboxes, int displayCountsOfWhat);

        ListDataProvider<CategoryDetail> getCellListDataProvider();

        MultiSelectionModel<CategoryDetail> getCellBrowserSelectionModel();

        SingleSelectionModel<CategoryDetail> getCellListSelectionModel();

        void toggleLoader();

        boolean isValid();

        Widget getWidgetView();
    }
    // for preventing users from double clicking list item, what would result in multiple instances of
    // same list
    private boolean preventMultipleCalls = false;

    @Override
    public void bindView() {
        view.getCellBrowserSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                List<CategoryDetail> selectedList = new ArrayList<CategoryDetail>(
                        view.getCellBrowserSelectionModel().getSelectedSet());
                view.getCellListDataProvider().setList(selectedList);
            }
        });
        view.getCellListSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.getCellBrowserSelectionModel().setSelected(
                        view.getCellListSelectionModel().getSelectedObject(),
                        false);
                view.getCellListDataProvider().getList().remove(
                        view.getCellListSelectionModel().getSelectedObject());
            }
        });
    }

    public CategoryRPCServiceAsync getCategoryService() {
        return categoryService;
    }

    /**
     *
     * @param embedWidget - panel where widget will be set up.
     * @param checkboxes - Constants.WITHOUT_CHECK_BOXES /
     *                     Constants.WITH_CHECK_BOXES    /
     *                     Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS
     * @param displayCountsOfWhat - CategoryCell.DISPLAY_COUNT_OF_DEMANDS   /
     *                              CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS /
     *                              CategoryCell.DISPLAY_COUNT_DISABLED
     */
    public void initCategoryWidget(SimplePanel embedWidget, int checkboxes, int displayCountsOfWhat,
            List<CategoryDetail> categoriesToSet) {
        view.createCellBrowser(checkboxes, displayCountsOfWhat);
        embedWidget.setWidget(view.getWidgetView());

        //Set categories if any
        if (categoriesToSet != null) {
            for (CategoryDetail catDetail : categoriesToSet) {
                //Select in cellBrowser
                view.getCellBrowserSelectionModel().setSelected(catDetail, true);
                //Display selected categories
                view.getCellListDataProvider().getList().add(catDetail);
            }
        }
    }

//    private void postRequest(int index, String category) {
//        view.showLoader(index);
//        eventBus.getChildListCategories(index, category);
//    }
    //premenovat
    public void onSetCategoryData(List<CategoryDetail> list) {
//        ListBox listBox = view.createListAtIndex(newListPosition);
//        setAndDisplayData(listBox, list);
//        addCategoryChangeHandler(listBox, newListPosition);
        //nieco aby skoncilo loading indicator
    }
//
//    private void setAndDisplayData(final ListBox box, final List<CategoryDetail> list) {
//        int columnCount = view.getListHolder().getColumnCount();
//
//        for (CategoryDetail aList : list) {
//            box.addItem(aList.getParentName(), String.valueOf(aList.getId()));
//        }
//
//        //check if possible to display, if needed resize table
//        int positionToInsert = view.getFreeListIndex();
//        if (columnCount == positionToInsert) {
//            view.getListHolder().resizeColumns(columnCount + 1);
//        }
//        view.getListHolder().setWidget(0, positionToInsert, box);
//        view.getScrollPanel().scrollToRight();
//
//        preventMultipleCalls = false;
//        box.setVisible(true);
//    }
//
//    private void addCategoryChangeHandler(final ListBox box, final int index) {
//        box.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                if (preventMultipleCalls) { return; }
//                String text = box.getItemText(box.getSelectedIndex());
//                String value = box.getValue(box.getSelectedIndex());
//                if (event.isControlKeyDown() &&  ! isLeaf(text)) {
//                    view.addToSelectedList(new CategoryDetail(Long.parseLong(value),
//                            text.substring(0, text.indexOf(NONLEAF_SUFFIX))));
//                } else {
//                    if (isLeaf(text)) {
//                        view.addToSelectedList(new CategoryDetail(Long.parseLong(value), text));
//                    } else {
//                        preventMultipleCalls = true;
//                        view.clearChildrenLists(index);
//                        postRequest(index + 1, box.getValue(box.getSelectedIndex()));
//                    }
//                }
//            }
//        });
//    }

//    private boolean isLeaf(String text) {
//        return ! text.contains(" >");
//    }
    public void getRootCategories(AsyncDataProvider dataProvider) {
        eventBus.getRootCategories(dataProvider);
    }

    public void getCategories(ListDataProvider dataProvider, long catId) {
        eventBus.getChildCategories(catId, dataProvider);
    }
}
