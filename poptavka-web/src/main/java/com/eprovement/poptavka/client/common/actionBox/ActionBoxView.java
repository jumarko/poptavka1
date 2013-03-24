/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.actionBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 *
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
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public MenuItem getActionRead() {
        return actionRead;
    }

    @Override
    public MenuItem getActionUnread() {
        return actionUnread;
    }

    @Override
    public MenuItem getActionStar() {
        return actionStar;
    }

    @Override
    public MenuItem getActionUnstar() {
        return actionUnstar;
    }
}
