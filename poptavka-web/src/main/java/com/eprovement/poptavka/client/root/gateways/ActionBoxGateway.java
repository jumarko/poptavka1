package com.eprovement.poptavka.client.root.gateways;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import java.util.List;

/**
 * Gateway interface for Address selector module.
 * Defines which methods are accessible to the world.
 *
 * @author Martin Slavkovsky
 */
public interface ActionBoxGateway {

    @Event(forwardToParent = true)
    void initActionBox(SimplePanel holderWidget, UniversalAsyncGrid grid);

    @Event(forwardToParent = true)
    void requestReadStatusUpdate(List<Long> userMessageIds, boolean isRead);

    @Event(forwardToParent = true)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);
}