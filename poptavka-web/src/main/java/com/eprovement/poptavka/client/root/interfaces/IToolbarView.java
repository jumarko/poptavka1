package com.eprovement.poptavka.client.root.interfaces;

import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.LazyView;

/**
 * @author Martin Slavkovsky
 */
public interface IToolbarView extends LazyView, IsWidget {

    public interface IToolbarPresenter {
    }

    void setToolbarContent(String title, Widget content, boolean hasAnimationLayout);

    IconAnchor getLeftSlidingMenuIcon();

    IconAnchor getRightSlidingMenuIcon();
}
