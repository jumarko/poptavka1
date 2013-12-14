/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.toolbar;

import com.eprovement.poptavka.client.root.interfaces.IToolbarView;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Toolbar view
 * @author Martin Slavkovsky
 */
public class ToolbarView extends Composite implements IToolbarView {

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
    @UiField IconAnchor categoriesAnchor, detailAnchor;
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
     * @param hasAnimationLayout - has 3-layout-responsive-view that can be animated
     */
    @Override
    public void setToolbarContent(String title, Widget content, boolean hasAnimationLayout) {
        if (!hasAnimationLayout) {
            this.categoriesAnchor.setVisible(false);
            this.detailAnchor.setVisible(false);
            this.fakeDetailAnchor.setVisible(false);
        } else {
            this.categoriesAnchor.setVisible(true);
            this.detailAnchor.setVisible(true);
            this.fakeDetailAnchor.setVisible(true);
        }
        if (title == null) {
            this.title.setVisible(false);
        } else {
            Element leftSlidingPanel = DOM.getElementById("gwt-debug-leftSlidingPanel");
            if (leftSlidingPanel != null) {
                this.title.setWidth(leftSlidingPanel.getClientWidth() + "px");
            }
            this.title.setVisible(true);
            this.title.setText(title);
        }
        if (content == null) {
            this.customContent.setVisible(false);
            if (!hasAnimationLayout && this.getParent() != null) {
                this.getParent().addStyleName(StyleResource.INSTANCE.initial().hideOnSmall());
            }
        } else {
            this.getParent().removeStyleName(StyleResource.INSTANCE.initial().hideOnSmall());
            this.customContent.setVisible(true);
            this.customContent.setWidget(content);
        }
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * Gets left sliding menu icon.
     * @return left menu icon anchor
     */
    @Override
    public IconAnchor getLeftSlidingMenuIcon() {
        return categoriesAnchor;
    }

    /**
     * Gets right sliding menu icon.
     * @return right menu icon anchor
     */
    @Override
    public IconAnchor getRightSlidingMenuIcon() {
        return detailAnchor;
    }
}