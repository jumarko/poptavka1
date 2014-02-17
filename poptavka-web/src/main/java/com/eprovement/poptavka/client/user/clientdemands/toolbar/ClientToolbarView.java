/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.toolbar;

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
 * Custom toolbar for client demands module.
 * @author Martin Slavkovsky
 */
@Singleton
public class ClientToolbarView extends Composite implements IsWidget {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    private static ClientToolbarViewUiBinder uiBinder = GWT.create(ClientToolbarViewUiBinder.class);

    interface ClientToolbarViewUiBinder extends UiBinder<Widget, ClientToolbarView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField Button backBtn, editBtn, deleteBtn, acceptBtn, closeBtn;
    @UiField SimplePanel actionBox;
    @UiField(provided = true) UniversalPagerWidget pager;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ClientToolbar view's compontents.
     */
    public ClientToolbarView() {
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
     * Set edit demands button visibility.
     * @param visibility true to display, false otherwise
     */
    public void setEditDemandBtnsVisibility(boolean visibility) {
        editBtn.setVisible(visibility);
        deleteBtn.setVisible(visibility);
    }

    /**
     * Hides items except submenu title and pager.
     */
    public void resetBasic() {
        backBtn.setVisible(false);
        editBtn.setVisible(false);
        deleteBtn.setVisible(false);
        acceptBtn.setVisible(false);
        closeBtn.setVisible(false);
        actionBox.setVisible(false);
        pager.setVisible(true);
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
     * @return the back button
     */
    public Button getBackBtn() {
        return backBtn;
    }

    /**
     * @return the edit button
     */
    public Button getEditBtn() {
        return editBtn;
    }

    /**
     * @return the delete button
     */
    public Button getDeleteBtn() {
        return deleteBtn;
    }

    /**
     * @return the accept button
     */
    public Button getAcceptBtn() {
        return acceptBtn;
    }

    /**
     * @return the close button
     */
    public Button getCloseBtn() {
        return closeBtn;
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
