/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import java.util.ArrayList;

/**
 * Custom advance search attributes selector for messages module.
 * @author Martin Slavkovsky
 */
public class MessagesSearchView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, MessagesSearchView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField TextBox sender, subject, body;
    @UiField Button clearBtn;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates MessageSearch view's components.
     */
    public MessagesSearchView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    /**
     * Binds clear button handler. Clears view's compontents.
     */
    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        clear();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * Get search fileter
     * @return list of search filter.
     */
    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!sender.getText().equals("")) {
            filters.add(new FilterItem("companyName", Operation.OPERATION_LIKE, sender.getText(), 0));
        }
        if (!subject.getText().equals("")) {
            filters.add(new FilterItem("subject", Operation.OPERATION_LIKE, subject.getText(), 1));
        }
        if (!body.getText().equals("")) {
            filters.add(new FilterItem("body", Operation.OPERATION_LIKE, body.getText(), 2));
        }
        return filters;
    }

    /**
     * Clears view's compontents.
     */
    @Override
    public void clear() {
        sender.setText("");
        subject.setText("");
        body.setText("");
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}