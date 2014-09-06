/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import java.util.List;

/**
 * Gateway interface for Category selector module.
 * Defines which methods are accessible to the rest of application.
 *
 * @author Martin Slavkovsky
 */
public interface CatLocSelectorGateway {

    @Event(forwardToParent = true)
    void initCatLocSelector(SimplePanel embedToWidget, CatLocSelectorBuilder builder);

    @Event(forwardToParent = true)
    void fillCatLocs(List<ICatLocDetail> selectedCategories, int instanceId);

    @Event(forwardToParent = true)
    void setCatLocs(List<ICatLocDetail> categories, int instanceId);

    @Event(forwardToParent = true)
    void requestHierarchy(int selectorType, ICatLocDetail category, int instanceId);

    @Event(forwardToParent = true)
    void redrawCatLocSelectorGrid(int instanceId);
}
