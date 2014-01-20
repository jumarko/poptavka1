package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.LazyView;

/**
 * @author Martin Slavkovsky
 */
public interface IToolbar {

    public interface Presenter extends HandleResizeEvent {
    }

    public interface View extends LazyView, IsWidget {

        void setToolbarContent(String title, Widget content);

        void refresh();

        Image getLeftSlidingMenuIcon();

        Image getRightSlidingMenuIcon();
    }
}
