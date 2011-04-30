package cz.poptavka.sample.client.common.category;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

//@Singleton
public class CategorySelectorView extends Composite implements CategorySelectorPresenter.CategorySelectorInterface {

    private static CategorySelectorUiBinder uiBinder = GWT.create(CategorySelectorUiBinder.class);

    interface CategorySelectorUiBinder extends UiBinder<Widget, CategorySelectorView> {
    }

    //default list visibleItemCount
    private static final int TEN = 10;

    private HashSet<String> selectedListStrings = new HashSet<String>();

    @UiField Grid categoryListHolder;
    private ArrayList<ListBox> subcategories = new ArrayList<ListBox>();

//    @UiField HTML loader;
    @UiField ListBox selectedList;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public Widget getWidgetView() {
        return this;
    }

    public void toggleLoader() {
//        loader.setVisible(!loader.isVisible());
    }

    @Override
    public ListBox getSelectedList() {
        return selectedList;
    }

    @Override
    public void addToSelectedList(String text, String value) {
        if (!selectedListStrings.contains(value)) {
            selectedList.addItem(text, value);
            selectedListStrings.add(text);
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
    public int getListIndex() {
        return ((subcategories.size() - 1) >= 0 ? (subcategories.size() - 1) : 0);
    }

    public boolean isSubcategoryListEmpty() {
        return subcategories.isEmpty();
    }

    @Override
    public Grid getListHolder() {
        return categoryListHolder;
    }

    @Override
    public ListBox createListAtIndex(int index) {
        ListBox list = new ListBox();
        list.setVisibleItemCount(TEN);
        list.setWidth("200");
        if (isSubcategoryListEmpty()) {
            subcategories.add(list);
        } else {
            subcategories.add(index, list);
        }
        return list;
    }

    @Override
    public void clearChildrenLists(int index) {
        for (int i = getListIndex(); i > index; i--) {
//            categoryListHolder.removeCell(0, i);
            categoryListHolder.clearCell(0, i);
            subcategories.remove(i);
        }
    }

}
