/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.actionBox;

import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * View consists of dropdown menu:
 * <ul>
 *   <li>mark as read</li>
 *   <li>mark as unread</li>
 *   <li>mark as starred</li>
 *   <li>mark as unstarred</li>
 * </ul
 * @author Martin Slavkovsky
 */
public class ActionBoxView extends Composite
        implements ActionBoxPresenter.ActionBoxViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ActionBoxViewUiBinder uiBinder = GWT.create(ActionBoxViewUiBinder.class);

    interface ActionBoxViewUiBinder extends UiBinder<Widget, ActionBoxView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField MenuItem actionRead, actionUnread, actionStar, actionUnstar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ActionBox view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the read menu item
     */
    @Override
    public MenuItem getActionRead() {
        return actionRead;
    }

    /**
     * @return the unread menu item
     */
    @Override
    public MenuItem getActionUnread() {
        return actionUnread;
    }

    /**
     * @return the star menu item
     */
    @Override
    public MenuItem getActionStar() {
        return actionStar;
    }

    /**
     * @return the unstar menu item
     */
    @Override
    public MenuItem getActionUnstar() {
        return actionUnstar;
    }
}
