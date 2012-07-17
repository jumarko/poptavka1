package com.eprovement.poptavka.client.common.category;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import java.util.ArrayList;

public class CategorySelectorView extends Composite
    implements CategorySelectorPresenter.CategorySelectorInterface, ProvidesValidate {

    private static CategorySelectorUiBinder uiBinder = GWT.create(CategorySelectorUiBinder.class);
    interface CategorySelectorUiBinder extends UiBinder<Widget, CategorySelectorView> {    }

    //default list visibleItemCount
    private static final int TEN = 10;

    @UiField
    ScrollPanel masterPanel;
    @UiField
    Grid categoryListHolder;
    private ArrayList<ListBox> listBoxes = new ArrayList<ListBox>();

    @UiField(provided = true) CellList selectedList;
    private SingleSelectionModel<CategoryDetail> selectionModel = new SingleSelectionModel<CategoryDetail>();
    private ListDataProvider<CategoryDetail> dataProvider = new ListDataProvider<CategoryDetail>();

//    private HashSet<String> selectedListTitles = new HashSet<String>();

    @Override
    public void createView() {
        selectedList = new CellList<CategoryDetail>(new ItemCell());
        selectedList.setSelectionModel(selectionModel);
        dataProvider.addDataDisplay(selectedList);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public Widget getWidgetView() {
        return this;
    }

    @Override
    public CellList getSelectedList() {
        return selectedList;
    }

    @Override
    public SingleSelectionModel getSelectionModel() {
        return selectionModel;
    }

    @Override
    public ListDataProvider<CategoryDetail> getDataProvider() {
        return dataProvider;
    }

    @Override
    public void addToSelectedList(CategoryDetail categoryDetail) { //String text, String value
//        if (!selectedListTitles.contains(text)) {
        if (!dataProvider.getList().contains(categoryDetail)) {
            dataProvider.getList().add(categoryDetail);
        }
    }

    @Override
    public void removeFromSelectedList() {
        dataProvider.getList().remove(selectionModel.getSelectedObject());
    }

    /** Returns actual free depth level. **/
    @Override
    public int getFreeListIndex() {
        return ((listBoxes.size() - 1) >= 0 ? (listBoxes.size() - 1) : 0);
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
        if (listBoxes.isEmpty()) {
            listBoxes.add(list);
        } else {
            listBoxes.add(index, list);
        }
        return list;
    }

    @Override
    public void clearChildrenLists(int index) {
        for (int i = getFreeListIndex(); i > index; i--) {
            categoryListHolder.clearCell(0, i);
            listBoxes.remove(i);
        }
    }

    public ScrollPanel getScrollPanel() {
        return masterPanel;
    }

    @Override
    public boolean isValid() {
        return dataProvider.getList().size() > 0;
    }

    /** Demand cration getValues method. **/
    @Override
    public ArrayList<String> getSelectedCategoryCodes() {
        ArrayList<String> codes = new ArrayList<String>();
        for (CategoryDetail catDetail: dataProvider.getList()) {
            codes.add(Long.toString(catDetail.getId()));
        }
        return codes;
    }

    @Override
    public void showLoader(int index) {
        try {
            int columCount = categoryListHolder.getColumnCount();
            int positionToInsert = getFreeListIndex() + 1;
            if (columCount == positionToInsert) {
                categoryListHolder.resizeColumns(columCount + 1);
            }
            HTML html = new HTML("&nbsp;");
            categoryListHolder.setWidget(0, index, html);
            html.setStyleName(StyleResource.INSTANCE.common().smallLoader());
        } catch (Exception ex) {
            Window.alert(ex.getMessage() + "\nColumn count: " + categoryListHolder.getColumnCount()
                    + " posToInsert: " + getFreeListIndex());
        }
    }



}

/**
 * The Cell used to render a {@link CategoryDetail}.
 */
class ItemCell extends AbstractCell<CategoryDetail> {

    @Override
    public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        // Value can be null, so do a null check..
        if (value == null) {
            return;
        }

        // Add category name.
        sb.appendHtmlConstant("<table><tr><td style='font-size:95%;'>");
        sb.appendEscaped(value.getName());
        sb.appendHtmlConstant("</td>");
        // Add image for remove.
        sb.appendHtmlConstant("<td>");
        sb.appendHtmlConstant(AbstractImagePrototype.create(
                StyleResource.INSTANCE.images().errorIcon()).getHTML());
        sb.appendHtmlConstant("</td></tr></table>");
    }
}
