/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.resources.UniversalPager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;

/**
 * Holds SimplePager and Menu for choosing page size. Default page size choices are: 10,20,30.
 * For different page size choices user appropriate constructor.
 *
 * @author Martin Slavkovsky
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
    /** UiBinder attributes. **/
    @UiField(provided = true) SimplePager pager;
    @UiField MenuItem pageSize;
    @UiField MenuBar pageSizeList;
    /** Class attributes. **/
    /** Constants. **/
    //How many options in page size combo is generated.
    private static final int PAGE_SIZE_ITEMS_COUNT = 3;
    //Represent gab between page size options.
    private static final int PAGE_SIZE_MULTIPLICANT = 10;
    //Which of the items of pageSize combo is selected. by default.
    private static final int PAGE_SIZE_ITEM_SELECTED = 2;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public UniversalPagerWidget() {
        initPager();
        initWidget(uiBinder.createAndBindUi(this));
        initPageSizeListBox(PAGE_SIZE_ITEMS_COUNT, PAGE_SIZE_MULTIPLICANT, PAGE_SIZE_ITEM_SELECTED);
        pager.setPageSize(Integer.parseInt(pageSize.getText()));
    }

    /**
     * Constructor that generates page size options.
     *
     * @param count - define how many options in page size combo is generated.
     * @param multiplicant - represent gab between page size options.
     * @param selectIndex - default selected page size choice. order integer.
     */
    public UniversalPagerWidget(int count, int multiplicant, int selectIndex) {
        initPager();
        initWidget(uiBinder.createAndBindUi(this));
        initPageSizeListBox(count, multiplicant, selectIndex);
        pager.setPageSize(Integer.parseInt(pageSize.getText()));
    }

    /**
     * Create page size menu options according to given attributes.
     *
     * @param count - define how many options in page size combo is generated.
     * @param mutiplicant - represent gab between page size options.
     * @param selectedIndex - default selected page size choice. order integer.
     */
    private void initPageSizeListBox(final int count, final int mutiplicant, final int selectedIndex) {
        //if selected is asking to select item beyond items count, select last item
        if (selectedIndex + 1 > count) {
            pageSize.setText(Integer.toString(count * mutiplicant));
        } else {
            pageSize.setText(Integer.toString((selectedIndex + 1) * mutiplicant));
        }
        //generate page size list
        for (int i = 1; i <= count; i++) {
            final int j = i;
            pageSizeList.addItem(new MenuItem(Integer.toString(j * mutiplicant),
                    new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            pageSize.setText(Integer.toString(j * mutiplicant));
                            pager.setPageSize(j * mutiplicant);
                        }
                    }));
        }
    }

    /**
     * Initialize pager and page size list box.
     */
    private void initPager() {
        SimplePager.Resources pagerResources = GWT.create(UniversalPager.class);
        pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setRangeLimited(false);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public SimplePager getPager() {
        return pager;
    }

    public int getPageSize() {
        return Integer.parseInt(pageSize.getText());
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setDisplay(HasRows object) {
        pager.setDisplay(object);
    }
}
