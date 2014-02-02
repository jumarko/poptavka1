/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.pager.UniversalPagerResources;
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

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates UniversalPagerWidget view's components.
     */
    public UniversalPagerWidget() {
        initPager();
        initWidget(uiBinder.createAndBindUi(this));
        initPageSizeListBox();
        pager.setPageSize(Integer.parseInt(pageSize.getText()));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**
     * Creates UniversalPagerWidget view's components.
     * Constructor that generates page size options.
     *
     * @param count - define how many options in page size combo is generated.
     * @param multiplicant - represent gab between page size options.
     * @param selectIndex - default selected page size choice. order integer.
     */
    public UniversalPagerWidget(int count, int multiplicant, int selectIndex) {
        initPager();
        initWidget(uiBinder.createAndBindUi(this));
        initPageSizeListBox();
        pager.setPageSize(Integer.parseInt(pageSize.getText()));
    }

    /**
     * TODO LATER Martin - remake to WSListBox
     * Create page size menu options according to given attributes.
     */
    private void initPageSizeListBox() {
        //if selected is asking to select item beyond items count, select last item
        pageSize.setText(Integer.toString(Constants.PAGER_SIZE_DEFAULT));
        //generate page size list
        for (int i = 0; i < Constants.PAGER_SIZE_ITEMS.length; i++) {
            final int j = i;
            pageSizeList.addItem(new MenuItem(Constants.PAGER_SIZE_ITEMS[j],
                    new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            pageSize.setText(Constants.PAGER_SIZE_ITEMS[j]);
                            pager.setPageSize(Integer.parseInt(Constants.PAGER_SIZE_ITEMS[j]));
                        }
                    }));
        }
    }

    /**
     * Initialize pager and page size list box.
     */
    private void initPager() {
        UniversalPagerResources pagerResources = GWT.create(UniversalPagerResources.class);
        CssInjector.INSTANCE.ensurePagerStylesInjected(pagerResources);

        pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setRangeLimited(false);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the pager
     */
    public SimplePager getPager() {
        return pager;
    }

    /**
     * @return the pages size
     */
    public int getPageSize() {
        return Integer.parseInt(pageSize.getText());
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Binds pager to given HasRows object.
     * @param object - the HasRows objecct
     */
    public void setDisplay(HasRows object) {
        pager.setDisplay(object);
    }
}
