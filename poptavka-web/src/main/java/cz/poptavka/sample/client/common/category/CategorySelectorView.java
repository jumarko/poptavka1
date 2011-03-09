package cz.poptavka.sample.client.common.category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategoryType;

@Singleton
public class CategorySelectorView extends Composite implements CategorySelectorPresenter.CategorySelectorInterface {

    private static CategorySelectorUiBinder uiBinder = GWT.create(CategorySelectorUiBinder.class);

    interface CategorySelectorUiBinder extends UiBinder<Widget, CategorySelectorView> {
    }

    private static final int VISIBLE_COUNT = 10;

    @UiField
    ListBox rootCategoryList;
    @UiField
    ListBox categoryList;
    @UiField
    ListBox subCategoryList;
    @UiField
    HTML loader;

    public CategorySelectorView() {
        initWidget(uiBinder.createAndBindUi(this));

        loader.setVisible(false);

        rootCategoryList.setVisibleItemCount(VISIBLE_COUNT);
        categoryList.setVisible(false);
        categoryList.setVisibleItemCount(VISIBLE_COUNT);
        subCategoryList.setVisible(false);
        subCategoryList.setVisibleItemCount(VISIBLE_COUNT);
    }

    @Override
    public String getSelectedItem(CategoryType list) {

        switch (list) {
            case ROOT:
                return rootCategoryList.getValue(rootCategoryList.getSelectedIndex());
            case MAIN:
                return categoryList.getValue(categoryList.getSelectedIndex());
            default:
                return subCategoryList.getValue(subCategoryList.getSelectedIndex());
        }

    }

    public Widget getWidgetView() {
        return this;
    }

    public void toggleLoader() {
        loader.setVisible(!loader.isVisible());
    }

    @Override
    public ListBox getRootCategoryList() {
        return rootCategoryList;
    }

    @Override
    public ListBox getCategoryList() {
        return categoryList;
    }

    @Override
    public ListBox getSubCategoryList() {
        return subCategoryList;
    }

}
