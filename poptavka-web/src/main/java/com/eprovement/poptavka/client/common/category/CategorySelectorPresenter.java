package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
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

        boolean isValid();

        Widget getWidgetView();
    }

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
}
