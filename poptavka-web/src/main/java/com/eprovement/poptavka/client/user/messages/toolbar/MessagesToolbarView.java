/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.messages.toolbar;

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
 * Custom toolbar for Messages module.
 * @author Martin Slavkovsky
 */
@Singleton
public class MessagesToolbarView extends Composite implements IsWidget {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static MessagesToolbarViewUiBinder uiBinder = GWT.create(MessagesToolbarViewUiBinder.class);

    interface MessagesToolbarViewUiBinder extends UiBinder<Widget, MessagesToolbarView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField Button replyBtn;
    @UiField SimplePanel actionBox;
    @UiField(provided = true) UniversalPagerWidget pager;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates MessagesToolbar view's components.
     */
    public MessagesToolbarView() {
        pager = new UniversalPagerWidget();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Binds pager to table
     * @param table
     */
    public void bindPager(UniversalAsyncGrid table) {
        pager.setDisplay(table);
    }

    /**
     * Hides items except submenu title and pager.
     */
    public void resetBasic() {
        replyBtn.setVisible(false);
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
     * @return the reply button
     */
    public Button getReplyBtn() {
        return replyBtn;
    }

    /**
     * @return the actionbox panel
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
