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
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ToolbarView.class)
public class ToolbarPresenter extends LazyPresenter<IToolbarView, RootEventBus> {

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
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
    public void onSetToolbarContent(String title, Widget content, boolean hasAnimationLayout) {
        view.setToolbarContent(title, content, hasAnimationLayout);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void openCategoryTreePanel(boolean open) {
        isCategoryPanelOpen = open;
        if (open) {
            animation.getLeftSlidingPanel().animate("left: -" + SLIDE_PX_CATEGORY, 0);
            animation.getLeftSlidingPanel().animate("left: +=" + SLIDE_PX_CATEGORY);
        } else {
            animation.getLeftSlidingPanel().animate("left: -=" + SLIDE_PX_CATEGORY);
        }
    }

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