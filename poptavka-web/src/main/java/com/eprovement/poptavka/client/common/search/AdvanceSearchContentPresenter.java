package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.client.common.search.AdvanceSearchContentPresenter.AdvanceSearchContentInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import java.util.ArrayList;

/*
 * Musi byt mulitple = true, inak advance search views sa nebudu zobrazovat
 * (bude vyhadzovat chybu) -- len v pripade, ze budu dedit
 * SearchModulesViewInterface, co chceme, aby voly Lazy
 */
@Presenter(view = AdvanceSearchContentView.class)
public class AdvanceSearchContentPresenter
        extends BasePresenter<AdvanceSearchContentInterface, SearchModuleEventBus> {

    public interface AdvanceSearchContentInterface {

        //Getters
        TabLayoutPanel getTabLayoutPanel();

        SimplePanel getAttributeSelectorPanel();

        SimplePanel getCategorySelectorPanel();

        SimplePanel getLocalitySelectorPanel();

        SearchModuleDataHolder getSearchModuleDataHolder();

        Widget getWidgetView();
    }

    //Neviem zatial preco, ale nemoze to byt lazy, pretoze sa neinicializuci advace
    //search views.
    public interface AdvanceSearchAttributeSelectorViewInterface {

        ArrayList<FilterItem> getFilter();

        Widget getWidgetView();
    }

    @Override
    public void bind() {
        view.getTabLayoutPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                switch (event.getItem()) {
                    case 0:
                        if (view.getAttributeSelectorPanel().getWidget() == null) {
                        }
                        break;
                    case 1:
                        //If not yet initialized, do it
                        if (view.getCategorySelectorPanel().getWidget() == null) {
                            eventBus.initCategoryWidget(
                                    view.getCategorySelectorPanel(),
                                    Constants.WITH_CHECK_BOXES);
                        }
                        break;
                    case 2:
                        //If not yet initialized, do it
                        if (view.getLocalitySelectorPanel().getWidget() == null) {
                            eventBus.initLocalityWidget(view.getLocalitySelectorPanel());
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**************************************************************************/
    /** General Module events                                                 */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /** Navigation events                                                     */
    /**************************************************************************/
    public void onInitAdvanceSearchContent(SimplePanel embedWidget, IsWidget attributeSelector) {
        GWT.log("AdvanceSearchContentWidget created");
        if (attributeSelector == null) {
            view.getTabLayoutPanel().getTabWidget(0).getParent().setVisible(false);
            view.getTabLayoutPanel().selectTab(1);
        } else {
            view.getTabLayoutPanel().getTabWidget(0).getParent().setVisible(true);
            view.getTabLayoutPanel().selectTab(0);
        }
        view.getAttributeSelectorPanel().add(attributeSelector);
        embedWidget.setWidget(view.getWidgetView());
    }
}