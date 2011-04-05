package cz.poptavka.sample.client.common.category;

import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategoryType;

//@Singleton
public class CategorySelectorView extends Composite implements CategorySelectorPresenter.CategorySelectorInterface {

    private static CategorySelectorUiBinder uiBinder = GWT.create(CategorySelectorUiBinder.class);

    interface CategorySelectorUiBinder extends UiBinder<Widget, CategorySelectorView> {
    }

    private HashSet<String> selectedListStrings = new HashSet<String>();

    @UiField
    ListBox rootCategoryList;
    @UiField
    ListBox categoryList;
    @UiField
    ListBox subCategoryList;
    @UiField
    HTML loader;

    @UiField ListBox selectedList;

//    public CategorySelectorView() {
//        initWidget(uiBinder.createAndBindUi(this));
//    }

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

    @Override
    public ListBox getSelectedList() {
        return selectedList;
    }

    @Override
    public void addToSelectedList() {
        int index = subCategoryList.getSelectedIndex();
        String itemText = subCategoryList.getItemText(index);
        if (!selectedListStrings.contains(itemText)) {
            selectedList.addItem(subCategoryList.getItemText(index), subCategoryList.getValue(index));
            selectedListStrings.add(itemText);
        }
    }

    @Override
    public void removeFromSelectedList() {
        int index = selectedList.getSelectedIndex();
        String item = selectedList.getItemText(index);
        selectedListStrings.remove(item);
        selectedList.removeItem(index);
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
