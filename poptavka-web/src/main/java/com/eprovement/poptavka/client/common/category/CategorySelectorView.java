package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.client.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.mvp4g.client.view.ReverseViewInterface;

public class CategorySelectorView extends Composite
        implements ProvidesValidate, ReverseViewInterface<CategorySelectorPresenter>, CategorySelectorInterface {

    private static CategorySelectorUiBinder uiBinder = GWT.create(CategorySelectorUiBinder.class);

    interface CategorySelectorUiBinder extends UiBinder<Widget, CategorySelectorView> {
    }

    /**************************************************************************/
    /* PRESENTER                                                              */
    /**************************************************************************/
    @Override
    public void setPresenter(CategorySelectorPresenter presenter) {
        this.categorySelectorPresenter = presenter;
    }

    @Override
    public CategorySelectorPresenter getPresenter() {
        return categorySelectorPresenter;
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                              */
    /**************************************************************************/
    //Presenter
    private CategorySelectorPresenter categorySelectorPresenter;
    @UiField
    HTML loader;
    //Cell Brower
    @UiField//(provided = true)
//    CellBrowser cellBrowser;
    SimplePanel cellBrowserHolder;
    //Cell List
    @UiField(provided = true)
    CellList<CategoryDetail> cellList;
    //Selection Moders
    MultiSelectionModel cellBrowserSelectionModel = new MultiSelectionModel();
    SingleSelectionModel cellListSelectionModel = new SingleSelectionModel();
    //Data Providers
    ListDataProvider<CategoryDetail> cellListDataProvider = new ListDataProvider<CategoryDetail>();

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        cellList = new CellList<CategoryDetail>(new ItemCell());
        cellList.setSelectionModel(cellListSelectionModel);
        cellListDataProvider.addDataDisplay(cellList);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void createCellBrowser(int checkboxes) {
        CellBrowser cellBrowser = new CellBrowser(new CategoryTreeViewModel(
                cellBrowserSelectionModel,
                categorySelectorPresenter.getCategoryService(),
                checkboxes, -1), null);
        cellBrowser.setSize("950px", "350px");
        cellBrowser.setAnimationEnabled(true);
        cellBrowserHolder.setWidget(cellBrowser);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public MultiSelectionModel<CategoryDetail> getCellBrowserSelectionModel() {
        return cellBrowserSelectionModel;
    }

    @Override
    public SingleSelectionModel<CategoryDetail> getCellListSelectionModel() {
        return cellListSelectionModel;
    }

    @Override
    public ListDataProvider<CategoryDetail> getCellListDataProvider() {
        return cellListDataProvider;
    }

    @Override
    public boolean isValid() {
        return !cellListDataProvider.getList().isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void toggleLoader() {
        loader.setVisible(!loader.isVisible());
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
