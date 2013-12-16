/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.catLocSelector;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.catLocSelector.others.HasCellTreeLoadingHandlers;
import com.eprovement.poptavka.client.service.demand.CatLocSelectorRPCServiceAsync;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocTreeItem;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import com.mvp4g.client.view.LazyView;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mato
 */
@EventHandler
public class CatLocSelectorInstanceManager extends BaseEventHandler<CatLocSelectorEventBus> {

    /**************************************************************************/
    /* Interfaces                                                             */
    /**************************************************************************/
    public interface CatLocSelectorInterface extends LazyView {

        SimplePanel getHolder();

        Widget getWidgetView();
    }

    public interface PresentersInterface extends HasCellTreeLoadingHandlers {

        CatLocSelectorEventBus getEventBus();

        CatLocSelectorRPCServiceAsync getService();

        int getInstanceId();

        CatLocSelectorBuilder getBuilder();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**
     * Manages one instance for each module.
     * Represent pairs: Instance ID - Widget Type
     */
    private Map<Integer, Integer> instanceIds = new HashMap<Integer, Integer>();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public void onInitCatLocSelector(
            SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId) {
        if (this.instanceIds.containsKey(instanceId)) {
            initSameCatLocSelector(embedToWidget, builder, instanceId);
        } else {
            this.instanceIds.put(instanceId, builder.getWidgetType());
            initNewCatLocSelector(embedToWidget, builder, instanceId);
        }
    }

    /**
     * Append given list with selected catLocs from CatLoc Selector widget.
     * If given list is null, initialize it first.
     * Only same instance are allowed to response.
     */
    public void onFillCatLocs(List<ICatLocDetail> selectedCatLocs, int instanceId) {
        if (this.instanceIds.containsKey(instanceId)) {
            switch (this.instanceIds.get(instanceId)) {
                case CatLocSelectorBuilder.WIDGET_TYPE_MANAGER:
                    eventBus.fillCatLocsFromManager(selectedCatLocs, instanceId);
                    break;
                case CatLocSelectorBuilder.WIDGET_TYPE_TREE:
                    eventBus.fillCatLocsFromTreeBrowser(selectedCatLocs, instanceId);
                    break;
                case CatLocSelectorBuilder.WIDGET_TYPE_BROWSER:
                    eventBus.fillCatLocsFromCellBrowser(selectedCatLocs, instanceId);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Set retrieved catLoc hierarchy to appropriate widget.
     * Only same instance are allowed to response.
     * @param result
     */
    public void onResponseHierarchy(LinkedList<CatLocTreeItem> result, int instanceId) {
        if (this.instanceIds.containsKey(instanceId)) {
            switch (this.instanceIds.get(instanceId)) {
                case CatLocSelectorBuilder.WIDGET_TYPE_MANAGER:
                    eventBus.responseHierarchyForManager(result, instanceId);
                    break;
                case CatLocSelectorBuilder.WIDGET_TYPE_TREE:
                    eventBus.responseHierarchyForTreeBrowser(result, instanceId);
                    break;
                case CatLocSelectorBuilder.WIDGET_TYPE_BROWSER:
                    eventBus.responseHierarchyForCellBrowser(result, instanceId);
                    break;
                default:
                    break;
            }
        }
    }

    /**************************************************************************/
    /** Helper methods                                                        */
    /**************************************************************************/
    /**
     * Initialize new CatLoc selector module.
     * According to given CatLocSelectorBuilder init either
     * CatLocManager, CatLocCellBrowser or CatLocTreeBrowser.
     * Only same instance are allowed to response.
     */
    private void initNewCatLocSelector(SimplePanel holder, CatLocSelectorBuilder builder, int instanceId) {
        switch (builder.getWidgetType()) {
            case CatLocSelectorBuilder.WIDGET_TYPE_MANAGER:
                eventBus.initNewCatLocSelectorManager(holder, builder, instanceId);
                break;
            case CatLocSelectorBuilder.WIDGET_TYPE_TREE:
                eventBus.initNewCatLocSelectorTreeBrowser(holder, builder, instanceId);
                break;
            case CatLocSelectorBuilder.WIDGET_TYPE_BROWSER:
                eventBus.initNewCatLocSelectorCellBrowser(holder, builder, instanceId);
                break;
            default:
                break;
        }
    }

    /**
     * Initialize same CatLoc selector module.
     * Existed instance will be reseted and called to handle event.
     * According to given CatLocSelectorBuilder init either
     * CatLocManager, CatLocCellBrowser or CatLocTreeBrowser.
     * Only same instance are allowed to response.
     */
    private void initSameCatLocSelector(SimplePanel holder, CatLocSelectorBuilder builder, int instanceId) {
        switch (builder.getWidgetType()) {
            case CatLocSelectorBuilder.WIDGET_TYPE_MANAGER:
                eventBus.initSameCatLocSelectorManager(holder, builder, instanceId);
                break;
            case CatLocSelectorBuilder.WIDGET_TYPE_TREE:
                eventBus.initSameCatLocSelectorTreeBrowser(holder, builder, instanceId);
                break;
            case CatLocSelectorBuilder.WIDGET_TYPE_BROWSER:
                eventBus.initSameCatLocSelectorCellBrowser(holder, builder, instanceId);
                break;
            default:
                break;
        }
    }
}
