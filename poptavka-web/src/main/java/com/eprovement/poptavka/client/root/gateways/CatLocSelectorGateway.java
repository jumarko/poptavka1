/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Event;
import java.util.List;

/**
 * Gateway interface for Category selector module.
 * Defines which methods are accessible to the world.
 *
 * @author Martin Slavkovsky
 */
public interface CatLocSelectorGateway {

    @Event(forwardToParent = true)
    void initCatLocSelector(SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId);

    @Event(forwardToParent = true)
    void fillCatLocs(List<ICatLocDetail> selectedCategories, int instanceId);

    @Event(forwardToParent = true)
    void setCatLocs(List<ICatLocDetail> categories);

    @Event(forwardToParent = true)
    void requestHierarchy(int selectorType, ICatLocDetail category, int instanceId);

    @Event(forwardToParent = true)
    void registerCatLocTreeSelectionHandler(SelectionChangeEvent.Handler selectionHandler);
}
