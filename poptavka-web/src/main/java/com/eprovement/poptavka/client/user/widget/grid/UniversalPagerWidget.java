/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.resources.UniversalPager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;

/**
 *
 * @author Martin
 */
public class UniversalPagerWidget extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static UniversalPagerWidget.UniversalPagerWidgetUiBinder uiBinder =
            GWT.create(UniversalPagerWidget.UniversalPagerWidgetUiBinder.class);

    interface UniversalPagerWidgetUiBinder extends UiBinder<Widget, UniversalPagerWidget> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //pager definition
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSize;
    //Pager definition
    //How many options in page size combo is generated.
    private static final int PAGE_SIZE_ITEMS_COUNT = 3;
    //Represent gab between page size options.
    private static final int PAGE_SIZE_MULTIPLICANT = 10;
    //Which of the items of pageSize combo is selected. by default.
    private static final int PAGE_SIZE_ITEM_SELECTED = 0;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public UniversalPagerWidget() { //DataGrid grid) {
        initPageSizeListBox();
        initPager();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Create page size list box and generate items according to constants
     * PAGE_SIZE_ITEMS_COUNT and PAGE_SIZE_MULTIPLICANT. At the end select
     * item according to PAGE_SIZE_ITEM_SELECTED constant.
     */
    private void initPageSizeListBox() {
        pageSize = new ListBox();
        for (int i = 1; i < PAGE_SIZE_ITEMS_COUNT; i++) {
            pageSize.addItem(Integer.toString(i * PAGE_SIZE_MULTIPLICANT));
        }
        pageSize.setSelectedIndex(PAGE_SIZE_ITEM_SELECTED);
    }

    /**
     * Initialize pager and page size list box.
     */
    private void initPager() { //DataGrid grid) {
        SimplePager.Resources pagerResources = GWT.create(UniversalPager.class);
        pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);

        pager.setPageSize(Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex())));
//        pager.setDisplay(grid);
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("pageSize")
    public void pageSizeChangeHandler(ChangeEvent event) {
        /* Setting pager will fire RangeRangeEvent on bind display object (table), which
         * should take care of data retrieving of new range */
        /* Notice, that, setting pager won't display any loading indicator notifying
         * user about processing request */

        int page = pager.getPageStart() / pager.getPageSize();

        //if selected page > 1 and page size changed, recalculate new page start.
        pager.setPageStart(page * getPageSize());
        pager.setPageSize(getPageSize());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public SimplePager getPager() {
        return pager;
    }

    public ListBox getPageSizeListBox() {
        return pageSize;
    }

    public int getPageSize() {
        return Integer.parseInt(pageSize.getItemText(pageSize.getSelectedIndex()));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setDisplay(HasRows object) {
        pager.setDisplay(object);
    }
}
