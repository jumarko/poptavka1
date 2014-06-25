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
import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.selectordatagrid.SelectorDataGrid;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
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
public class ManagerView extends ReverseCompositeView<ManagerPresenter> implements ManagerInterface {

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
    DataGrid<LinkedList<ICatLocDetail>> dataGrid;
    /** Class attributes. **/
    TextBox searchTextBoxBase;
    ListDataProvider<LinkedList<ICatLocDetail>> dataProvider;

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
        dataGrid = new DataGrid<LinkedList<ICatLocDetail>>(10, resource);
        dataProvider = new ListDataProvider<LinkedList<ICatLocDetail>>();
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
            public SafeHtml getValue(LinkedList<ICatLocDetail> hierarchy) {
                SafeHtmlBuilder str = new SafeHtmlBuilder();

                for (int i = 0; i < hierarchy.size(); i++) {
                    if (i == hierarchy.size() - 1) {
                        str.appendHtmlConstant("<strong>");
                        str.appendEscaped(hierarchy.get(i).getName());
                        str.appendHtmlConstant("</strong>");
                        if (!hierarchy.get(i).isLeaf()) {
                            str.appendEscaped(" -> ...");
                        }
                    } else {
                        str.appendEscaped(hierarchy.get(i).getName());
                        str.appendEscaped(" -> ");
                    }
                }

                return str.toSafeHtml();

            }
        }, null, null);

        //Browse cell
        addColumn(new ButtonCell(), "100px", new GetValue<String>() {
            @Override
            public String getValue(LinkedList<ICatLocDetail> contact) {
                return "Browse";
            }
        }, new FieldUpdater<LinkedList<ICatLocDetail>, String>() {
            @Override
            public void update(int index, LinkedList<ICatLocDetail> object, String value) {
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
                new ActionCell.Delegate<LinkedList<ICatLocDetail>>() {
                    @Override
                    public void execute(LinkedList<ICatLocDetail> contact) {
                        dataProvider.getList().remove(contact);
                        setSelectedCountLabel(dataProvider.getList().size(), presenter.getRegisterRestriction());
                    }
                }), "75px", new GetValue<LinkedList<ICatLocDetail>>() {
                    @Override
                    public LinkedList<ICatLocDetail> getValue(LinkedList<ICatLocDetail> detail) {
                        return detail;
                    }
                }, null, StyleResource.INSTANCE.common().buttonEmpty());

        //Cost
//        addColumn(new TextCell(), "100px", new GetValue<String>() {
//            @Override
//            public String getValue(LinkedList<ICatLocDetail> contact) {
//                return "50c";
//            }
//        }, null);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setSelectedCountLabel(int count, int countRestriction) {
        if (countRestriction != -1) {
            selectedCountLabel.setText(
                Storage.MSGS.commonSelected() + " (" + count + "/" + countRestriction + "):");
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        setSelectedCountLabel(0, presenter.getRegisterRestriction());
        dataProvider.getList().clear();
        dataGrid.redraw();
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
    public DataGrid<LinkedList<ICatLocDetail>> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the table data provider
     */
    @Override
    public ListDataProvider<LinkedList<ICatLocDetail>> getTableDataProvider() {
        return dataProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return !dataProvider.getList().isEmpty();
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
    private <C> Column<LinkedList<ICatLocDetail>, C> addColumn(Cell<C> cell, String width,
            final GetValue<C> getter, FieldUpdater<LinkedList<ICatLocDetail>, C> fieldUpdater, String columnStyle) {
        Column<LinkedList<ICatLocDetail>, C> column =
                new Column<LinkedList<ICatLocDetail>, C>(cell) {
                    @Override
                    public C getValue(LinkedList<ICatLocDetail> object) {
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

        C getValue(LinkedList<ICatLocDetail> detail);
    }
}
