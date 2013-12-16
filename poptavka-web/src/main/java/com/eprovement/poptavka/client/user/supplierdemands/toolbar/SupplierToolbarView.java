/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands.toolbar;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * Custom toolbar for SupplierDemands module.
 * @author Martin Slavkovsky
 */
@Singleton
public class SupplierToolbarView extends Composite implements IsWidget {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierToolbarViewUiBinder uiBinder = GWT.create(SupplierToolbarViewUiBinder.class);

    interface SupplierToolbarViewUiBinder extends UiBinder<Widget, SupplierToolbarView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField Button finishBtn;
    @UiField SimplePanel actionBox;
    @UiField(provided = true) UniversalPagerWidget pager;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates SuppplierToolbar view's compontents.
     */
    public SupplierToolbarView() {
        pager = new UniversalPagerWidget();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Binds pager to table.
     * @param table
     */
    public void bindPager(UniversalAsyncGrid table) {
        pager.setDisplay(table);
    }

    /**
     * Hides items except submenu title and pager.
     */
    public void resetBasic() {
        finishBtn.setVisible(false);
        actionBox.setVisible(false);
    }

    /**
     * Hides all items except submenu title.
     */
    public void resetFull() {
        resetBasic();
        pager.setVisible(false);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the finnish butotn
     */
    public Button getFinishBtn() {
        return finishBtn;
    }

    /**
     * @return the actionBox container
     */
    public SimplePanel getActionBox() {
        return actionBox;
    }

    /**
     * @return the universal pager widget
     */
    public UniversalPagerWidget getPager() {
        return pager;
    }

    /**
     * @return the widget view
     */
    public Widget getWdigetView() {
        return this;
    }
}
