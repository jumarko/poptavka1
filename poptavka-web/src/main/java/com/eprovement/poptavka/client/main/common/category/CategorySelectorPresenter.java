package com.eprovement.poptavka.client.main.common.category;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import java.util.ArrayList;
import java.util.List;

@Presenter(view = CategorySelectorView.class, multiple = true)
public class CategorySelectorPresenter
    extends LazyPresenter<CategorySelectorPresenter.CategorySelectorInterface, RootEventBus> {

    private static final String NONLEAF_SUFFIX = " >";

    public interface CategorySelectorInterface extends LazyView {

        Grid getListHolder();

        CellList getSelectedList();

        SingleSelectionModel getSelectionModel();

        ListDataProvider<CategoryDetail> getDataProvider();

        boolean isValid();

        void addToSelectedList(CategoryDetail categoryDetail);

        void removeFromSelectedList();

        int getFreeListIndex();

        ListBox createListAtIndex(int index);

        Widget getWidgetView();

        ScrollPanel getScrollPanel();

        void clearChildrenLists(int i);

        ArrayList<String> getSelectedCategoryCodes();

        void showLoader(int index);
    }
    // for preventing users from double clicking list item, what would result in multiple instances of
    // same list
    private boolean preventMultipleCalls = false;

    @Override
    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.removeFromSelectedList();
            }
        });
    }

    public void initCategoryWidget(SimplePanel embedWidget) {
        view.getListHolder().resizeColumns(0);
        eventBus.getChildListCategories(0, "ALL_CATEGORIES");
        embedWidget.setWidget(view.getWidgetView());
    }

    private void postRequest(int index, String category) {
        view.showLoader(index);
        eventBus.getChildListCategories(index, category);
    }

    public void onSetCategoryListData(int newListPosition, List<CategoryDetail> list) {
        ListBox listBox = view.createListAtIndex(newListPosition);
        setAndDisplayData(listBox, list);
        addCategoryChangeHandler(listBox, newListPosition);
    }

    private void setAndDisplayData(final ListBox box, final List<CategoryDetail> list) {
        int columnCount = view.getListHolder().getColumnCount();

        for (CategoryDetail aList : list) {
            box.addItem(aList.getParentName(), String.valueOf(aList.getId()));
        }

        //check if possible to display, if needed resize table
        int positionToInsert = view.getFreeListIndex();
        if (columnCount == positionToInsert) {
            view.getListHolder().resizeColumns(columnCount + 1);
        }
        view.getListHolder().setWidget(0, positionToInsert, box);
        view.getScrollPanel().scrollToRight();

        preventMultipleCalls = false;
        box.setVisible(true);
    }

    private void addCategoryChangeHandler(final ListBox box, final int index) {
        box.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (preventMultipleCalls) { return; }
                String text = box.getItemText(box.getSelectedIndex());
                String value = box.getValue(box.getSelectedIndex());
                if (event.isControlKeyDown() &&  ! isLeaf(text)) {
                    view.addToSelectedList(new CategoryDetail(Long.parseLong(value),
                            text.substring(0, text.indexOf(NONLEAF_SUFFIX))));
                } else {
                    if (isLeaf(text)) {
                        view.addToSelectedList(new CategoryDetail(Long.parseLong(value), text));
                    } else {
                        preventMultipleCalls = true;
                        view.clearChildrenLists(index);
                        postRequest(index + 1, box.getValue(box.getSelectedIndex()));
                    }
                }
            }
        });
    }

    private boolean isLeaf(String text) {
        return ! text.contains(" >");
    }

}
