/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.toolbar;

import com.eprovement.poptavka.client.root.interfaces.IToolbar;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Toolbar view
 * @author Martin Slavkovsky
 */
public class ToolbarView extends Composite implements IToolbar.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ToolbarViewUiBinder uiBinder = GWT.create(ToolbarViewUiBinder.class);

    interface ToolbarViewUiBinder extends UiBinder<Widget, ToolbarView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Image detailAnchor;
    @UiField Image categoriesAnchor;
    @UiField SimplePanel customContent, fakeDetailAnchor;
    @UiField Heading title;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates widget's components.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets toolbar's components like title, custom content and enable or disable animation on desponsive designs.
     * @param title of toolbar
     * @param content - custom toolbar widget
     * @param leftIconVisible - has 3-layout-responsive-view that can be animated
     */
    @Override
    public void setToolbarContent(final String titleText, final Widget content) {
        Element leftPanel = DOM.getElementById("gwt-debug-leftSlidingPanel");

        boolean isLeftVisible = leftPanel != null;
        boolean isRightVisible = DOM.getElementById("gwt-debug-rightSlidingPanel") != null;

        categoriesAnchor.setVisible(isLeftVisible);
        detailAnchor.setVisible(isRightVisible);
        fakeDetailAnchor.setVisible(isRightVisible);

        if (title == null) {
            title.setVisible(false);
        } else {
            if (isLeftVisible) {
                title.setWidth(DOM.getElementById("gwt-debug-leftSlidingPanel").getClientWidth() + "px");
            }
            title.setVisible(true);
            title.setText(titleText);
        }
        if (content == null) {
            customContent.setVisible(false);
            if (!isLeftVisible && !isRightVisible && getParent() != null) {
                getParent().addStyleName(StyleResource.INSTANCE.initial().hideOnSmall());
            }
        } else {
            getParent().removeStyleName(StyleResource.INSTANCE.initial().hideOnSmall());
            customContent.setVisible(true);
            customContent.setWidget(content);
        }
    }

    /**
     * Sets left and right icons visibility.
     */
    @Override
    public void refresh() {
        boolean isLeftVisible = DOM.getElementById("gwt-debug-leftSlidingPanel") != null;
        boolean isRightVisible = DOM.getElementById("gwt-debug-rightSlidingPanel") != null;
        categoriesAnchor.setVisible(isLeftVisible);
        detailAnchor.setVisible(isRightVisible);
        fakeDetailAnchor.setVisible(isRightVisible);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * Gets left sliding menu icon.
     * @return left menu icon anchor
     */
    @Override
    public Image getLeftSlidingMenuIcon() {
        return categoriesAnchor;
    }

    /**
     * Gets right sliding menu icon.
     * @return right menu icon anchor
     */
    @Override
    public Image getRightSlidingMenuIcon() {
        return detailAnchor;
    }
}