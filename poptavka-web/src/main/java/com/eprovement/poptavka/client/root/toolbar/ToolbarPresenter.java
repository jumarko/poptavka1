/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.toolbar;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IToolbarView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * Toolbar consists of four parts: left icon, title, custom content, right icon.
 * Left and Right icon serves for providing animation functionality for <b>3-layout-responsive-views</b>.
 *
 * <br/>
 * View is <b>3-layout-responsive-view</b> if its uiBinders constucts following hierarchy:
 * <pre>
 * {@code
 * <g:SimplePanel styleName='{res.layout.leftContainer}' debugId='leftContainer'/>
 * <b:FluidContainer styleName='{res.layout.contentContainer} user'>
 *      <b:FluidRow>
 *          <b:Column size="7"  addStyleNames='{res.initial.expandOnSmall}'/>
 *          <b:Column size="5"  addStyleNames='span-detail'>
 *              <g:SimplePanel styleName='{res.layout.detailContainer}' debugId='detailPanel'/>
 *          </b:Column>
 *      </b:FluidRow>
 * </b:FluidContainer>
 * }
 * </pre>
 * Left and Right panels can be implemented separately or you can miss one or both.
 * <b><i>Note:</i></b>
 * Toolbar consists of two parts: general toolbar - represented by this widget and
 * custom toolbar - provided by each module.
 * Animation is enabled by responsive design. Left sliding panel animates for tiny-middle screens.
 * Right sliding panel animates for tiny-small screen.s
 *
 * @author Martin Slavkovsky
 * @since 10.7.2013
 */
@Presenter(view = ToolbarView.class)
public class ToolbarPresenter extends LazyPresenter<IToolbarView, RootEventBus> {

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    /**
     * Sets toolbar widget to page layout.
     */
    public void onStart() {
        eventBus.setToolbar(view);
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private ResponsiveLayoutSelectors animation = GWT.create(ResponsiveLayoutSelectors.class);
    private boolean isCategoryPanelOpen = false;
    private boolean isDetailPanelOpen = false;
    /** Constants. **/
    private static final String SLIDE_PX_CATEGORY = "330px";
    private static final String SLIDE_PX_DETAIL = "350px";

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    /**
     * Bind handlers for Left & Right sliding menu icon.
     */
    @Override
    public void bindView() {
        if (view.getLeftSlidingMenuIcon() != null) {
            view.getLeftSlidingMenuIcon().addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    openCategoryTreePanel(!isCategoryPanelOpen);
                }
            });
        }
        if (view.getRightSlidingMenuIcon() != null) {
            view.getRightSlidingMenuIcon().addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    openDetailPanel(!isDetailPanelOpen);
                }
            });
        }
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Sets custom toolbar widget.
     * @param title of toolbar
     * @param content - custom toolbar widget
     * @param hasAnimationLayout - has 3-layout-responsive-view that can be animated
     */
    public void onSetToolbarContent(String title, Widget content, boolean hasAnimationLayout) {
        view.setToolbarContent(title, content, hasAnimationLayout);
    }

    /**
     * Removes animation styles on resize.
     * When animation is used it overrides application styles with its own.
     * It is not a problem unless application uses responsive design.
     * When resizing, responsive design should take care to redesign application,
     * but it is not if animation took place and overrided them.
     * Therefore remove them on resize.
     */
    public void onResetAnimation(int actualWidth) {
        if (767 <= actualWidth && actualWidth < 1200) {
            animation.getRightSlidingPanel().removeAttr("style");
            isDetailPanelOpen = false;
        } else if (1200 <= actualWidth) {
            animation.getLeftSlidingPanel().removeAttr("style");
            isCategoryPanelOpen = false;
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Animates left sliding panel.
     * @param open - true to slide left sliding panel to be visible, false otherwise
     */
    private void openCategoryTreePanel(boolean open) {
        isCategoryPanelOpen = open;
        if (open) {
            animation.getLeftSlidingPanel().animate("left: -" + SLIDE_PX_CATEGORY, 0);
            animation.getLeftSlidingPanel().animate("left: +=" + SLIDE_PX_CATEGORY);
        } else {
            animation.getLeftSlidingPanel().animate("left: -=" + SLIDE_PX_CATEGORY);
        }
    }

    /**
     * Animates right sliding panel.
     * @param open - true to slide right sliding panel to be visible, false otherwise
     */
    private void openDetailPanel(boolean open) {
        isDetailPanelOpen = open;
        if (open) {
            animation.getRightSlidingPanel().animate("right: -" + SLIDE_PX_DETAIL, 0);
            animation.getRightSlidingPanel().animate("right: +=" + SLIDE_PX_DETAIL);
        } else {
            animation.getRightSlidingPanel().animate("right: -=" + SLIDE_PX_DETAIL);
        }
    }
}