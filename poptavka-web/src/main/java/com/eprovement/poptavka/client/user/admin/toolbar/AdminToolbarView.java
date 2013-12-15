/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.toolbar;

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
 * Custom toolbar for Admin section..
 *
 * @author Martin Slavkovsky
 */
@Singleton
public class AdminToolbarView extends Composite implements IsWidget {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    private static AdminToolbarViewUiBinder uiBinder = GWT.create(AdminToolbarViewUiBinder.class);

    interface AdminToolbarViewUiBinder extends UiBinder<Widget, AdminToolbarView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField Button approveBtn, createConversationBtn;
    @UiField SimplePanel actionBox;
    @UiField(provided = true) UniversalPagerWidget pager;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates AdminToolbar view's components.
     */
    public AdminToolbarView() {
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
        approveBtn.setVisible(false);
        createConversationBtn.setVisible(false);
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
     * @return the approve button
     */
    public Button getApproveBtn() {
        return approveBtn;
    }

    /**
     * @return the create conversation button
     */
    public Button getCreateConversationBtn() {
        return createConversationBtn;
    }

    /**
     * @return the action box panel
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