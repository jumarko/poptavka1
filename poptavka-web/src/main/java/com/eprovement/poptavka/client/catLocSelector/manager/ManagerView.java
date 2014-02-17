/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.manager;

import com.eprovement.poptavka.client.catLocSelector.manager.ManagerPresenter.ManagerInterface;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSuggestOracle;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSuggestionDisplay;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.selectordatagrid.SelectorDataGrid;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocTreeItem;
import com.github.gwtbootstrap.client.ui.ButtonCell;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import java.util.LinkedList;

/**
 * Manager view consists of suggestBox for searching items, Browser button for
 * invoking any browser widget and table for managing selected items.
 *
 * @author Martin Slavkovsky
 */
public class ManagerView extends ReverseCompositeView<ManagerPresenter>
        implements ProvidesValidate, ManagerInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static CatLocSelectorUiBinder uiBinder = GWT.create(CatLocSelectorUiBinder.class);

    interface CatLocSelectorUiBinder extends UiBinder<Widget, ManagerView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) SuggestBox searchBox;
    @UiField Button browseBtn;
    @UiField Heading headerTitle;
    @UiField Label selectItemByLabel, selectedCountLabel;
    @UiField(provided = true)
    DataGrid<LinkedList<CatLocTreeItem>> dataGrid;
    /** Class attributes. **/
    TextBox searchTextBoxBase;
    ListDataProvider<LinkedList<CatLocTreeItem>> dataProvider;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates Manager view's components.
     */
    @Override
    public void createView() {
        initSuggestBox();
        initTable();

        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Inits suggestBox.
     */
    private void initSuggestBox() {
        searchTextBoxBase = new TextBox();
        searchBox = new SuggestBox(
                new CatLocSuggestOracle(presenter),
                searchTextBoxBase,
                new CatLocSuggestionDisplay());
    }

    /**
     * Inits table.
     */
    private void initTable() {
        DataGrid.Resources resource = GWT.create(SelectorDataGrid.class);
        dataGrid = new DataGrid<LinkedList<CatLocTreeItem>>(10, resource);
        dataProvider = new ListDataProvider<LinkedList<CatLocTreeItem>>();
        dataProvider.addDataDisplay(dataGrid);
        initTableColumns();
    }

    /**
     * Inits table columns.
     */
    private void initTableColumns() {
        //CatLoc hierarchy
        addColumn(new SafeHtmlCell(), "100%", new GetValue<SafeHtml>() {
            @Override
            public SafeHtml getValue(LinkedList<CatLocTreeItem> hierarchy) {
                SafeHtmlBuilder str = new SafeHtmlBuilder();

                for (int i = 0; i < hierarchy.size(); i++) {
                    if (i == hierarchy.size() - 1) {
                        str.appendHtmlConstant("<strong>");
                        str.appendEscaped(hierarchy.get(i).getCatLoc().getName());
                        str.appendHtmlConstant("</strong>");
                        if (!hierarchy.get(i).getCatLoc().isLeaf()) {
                            str.appendEscaped(" -> ...");
                        }
                    } else {
                        str.appendEscaped(hierarchy.get(i).getCatLoc().getName());
                        str.appendEscaped(" -> ");
                    }
                }

                return str.toSafeHtml();

            }
        }, null, null);

        //Browse cell
        addColumn(new ButtonCell(), "100px", new GetValue<String>() {
            @Override
            public String getValue(LinkedList<CatLocTreeItem> contact) {
                return "Browse";
            }
        }, new FieldUpdater<LinkedList<CatLocTreeItem>, String>() {
            @Override
            public void update(int index, LinkedList<CatLocTreeItem> object, String value) {
                //This is not going throug eventBus, therefore doesn't need to pass instanceId.
                //But in case of further development this can be forget, therefore pass id anyway.
                presenter.tableBrowseHandler(dataProvider.getList().indexOf(object),
                    object, presenter.getBuilder().getInstanceId());
            }
        }, StyleResource.INSTANCE.common().selectorButtonBrown());

        //Remove cell
        SafeHtmlBuilder as = new SafeHtmlBuilder();
        as.appendHtmlConstant(AbstractImagePrototype.create(StyleResource.INSTANCE.images().removeIcon()).getHTML());
        addColumn(new ActionCell(as.toSafeHtml(),
                new ActionCell.Delegate<LinkedList<CatLocTreeItem>>() {
                    @Override
                    public void execute(LinkedList<CatLocTreeItem> contact) {
                        dataProvider.getList().remove(contact);
                        setSelectedCountLabel(dataProvider.getList().size(), presenter.getRegisterRestriction());
                    }
                }), "75px", new GetValue<LinkedList<CatLocTreeItem>>() {
                    @Override
                    public LinkedList<CatLocTreeItem> getValue(LinkedList<CatLocTreeItem> detail) {
                        return detail;
                    }
                }, null, StyleResource.INSTANCE.common().buttonEmpty());

        //Cost
//        addColumn(new TextCell(), "100px", new GetValue<String>() {
//            @Override
//            public String getValue(LinkedList<CatLocTreeItem> contact) {
//                return "50c";
//            }
//        }, null);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setSelectedCountLabel(int count, int countRestriction) {
        selectedCountLabel.setText(
                Storage.MSGS.commonSelected() + " (" + count + "/" + countRestriction + "):");
    }

    /**
     * Sets selector type.
     * TODO Martin - add i18n strings
     * @param selectorType - SELECTOR_TYPE_CATEGORIES or SELECTOR_TYPE_LOCALITIES
     */
    @Override
    public void setSelectorType(int selectorType) {
        switch (selectorType) {
            case CatLocSelectorBuilder.SELECTOR_TYPE_CATEGORIES:
                headerTitle.setText("Select category");
                selectItemByLabel.setText("Select category by ");
                browseBtn.setText("Browse for category");
                searchTextBoxBase.setPlaceholder("Search for category");
                break;
            case CatLocSelectorBuilder.SELECTOR_TYPE_LOCALITIES:
                headerTitle.setText("Select locality");
                selectItemByLabel.setText("Select locality by ");
                browseBtn.setText("Browse for locality");
                searchTextBoxBase.setPlaceholder("Search for locality");
                break;
            default:
                break;
        }

    }
    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @return the suggestBox
     */
    @Override
    public SuggestBox getSearchBox() {
        return searchBox;
    }

    /**
     * @return the browser button
     */
    @Override
    public Button getBrowseBtn() {
        return browseBtn;
    }

    /**
     * @return the selected count label
     */
    @Override
    public Label getSelectedCountLabel() {
        return selectedCountLabel;
    }

    /**
     * @return the table (dataGrid)
     */
    @Override
    public DataGrid<LinkedList<CatLocTreeItem>> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the table data provider
     */
    @Override
    public ListDataProvider<LinkedList<CatLocTreeItem>> getTableDataProvider() {
        return dataProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return !dataProvider.getList().isEmpty();
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /******************************************************w********************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<LinkedList<CatLocTreeItem>, C> addColumn(Cell<C> cell, String width,
            final GetValue<C> getter, FieldUpdater<LinkedList<CatLocTreeItem>, C> fieldUpdater, String columnStyle) {
        Column<LinkedList<CatLocTreeItem>, C> column =
                new Column<LinkedList<CatLocTreeItem>, C>(cell) {
                    @Override
                    public C getValue(LinkedList<CatLocTreeItem> object) {
                        return getter.getValue(object);
                    }
                };
        column.setFieldUpdater(fieldUpdater);
        if (columnStyle != null) {
            column.setCellStyleNames(columnStyle);
        }
        dataGrid.addColumn(column);
        dataGrid.setColumnWidth(column, width);
        return column;
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private interface GetValue<C> {

        C getValue(LinkedList<CatLocTreeItem> detail);
    }
}
