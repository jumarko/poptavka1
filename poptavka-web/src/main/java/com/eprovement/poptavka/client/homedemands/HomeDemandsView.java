/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGridBuilder;
import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.demand.LesserDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.DemandField;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Arrays;

/**
 * Home demands module's view.
 *
 * @author praso, Martin Slavkovsky
 */
public class HomeDemandsView extends OverflowComposite
    implements HomeDemandsPresenter.HomeDemandsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static HomeDemandsViewUiBinder uiBinder = GWT.create(HomeDemandsViewUiBinder.class);

    interface HomeDemandsViewUiBinder extends UiBinder<Widget, HomeDemandsView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        StyleResource.INSTANCE.standartStyles().ensureInjected();
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid<LesserDemandDetail> dataGrid;
    @UiField SimplePanel detailPanel, footerPanel, categoryTreePanel;
    @UiField Label filterLabel;
    @UiField Button filterClearBtn;
    /** Class attributes. **/
    private UniversalPagerWidget pager;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates home demands view's components.
     */
    @Override
    public void createView() {
        initTable();
        initPager();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize UniversalAsyncGrid through UniversalGridFactory.
     */
    private void initTable() {
        dataGrid = new UniversalAsyncGridBuilder<LesserDemandDetail>()
            .addColumnDemandCreated(null)
            .addColumnDemandTitle(null)
            .addColumnLocality(null)
            .addColumnUrgency()
            .addSelectionModel(new SingleSelectionModel(), LesserDemandDetail.KEY_PROVIDER)
            .addDefaultSort(Arrays.asList(SortPair.desc(DemandField.CREATED)))
            .build();
    }

    /**
     * Initialize UniversalPagerWidget and add it to toolbar.
     */
    private void initPager() {
        pager = new UniversalPagerWidget();
        pager.addStyleName("item");
        pager.setDisplay(dataGrid);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** Table. **/
    /**
     * @return the universal asynchronous grid.
     */
    @Override
    public UniversalAsyncGrid<LesserDemandDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the table pager
     */
    @Override
    public SimplePager getPager() {
        return pager.getPager();
    }

    /** Filter. **/
    /**
     * @return the filter label
     */
    @Override
    public Label getFilterLabel() {
        return filterLabel;
    }

    /**
     * @return the filter clear button
     */
    @Override
    public Button getFilterClearBtn() {
        return filterClearBtn;
    }

    /** Other. **/
    /**
     * @return the category tree panel
     */
    @Override
    public SimplePanel getCategoryTreePanel() {
        return categoryTreePanel;
    }

    /**
     * @return the detail panel
     */
    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    /**
     * @return the footer panel
     */
    @Override
    public SimplePanel getFooterPanel() {
        return footerPanel;
    }

    /**
     * Gets toolbar custom content for home suppliers.
     * Only pager is needed, therefore return just pager.
     * @return the custom content - pager
     */
    @Override
    public Widget getToolbarContent() {
        return pager;
    }
}