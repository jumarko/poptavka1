/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.manager;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorEventBus;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSuggestionDisplay;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
import com.eprovement.poptavka.client.service.demand.CatLocSelectorRPCServiceAsync;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocSuggestionDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocTreeItem;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages selected items in a table.
 * Can be combined to use CellBrowser or TreeBrowser for selecting items.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ManagerView.class, multiple = true)
public class ManagerPresenter
        extends LazyPresenter<ManagerPresenter.ManagerInterface, CatLocSelectorEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface ManagerInterface extends LazyView {

        void setSelectedCountLabel(int count, int countRestriction);

        void setSelectorType(int selectorType);

        SuggestBox getSearchBox();

        Button getBrowseBtn();

        Label getSelectedCountLabel();

        void setPresenter(ManagerPresenter p);

        DataGrid<LinkedList<CatLocTreeItem>> getDataGrid();

        ListDataProvider<LinkedList<CatLocTreeItem>> getTableDataProvider();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private SimpleConfirmPopup popup = new SimpleConfirmPopup();
    private CatLocSelectorBuilder builder;
    private CatLocSelectorBuilder browserBuilder;
    private int updatingTableItemIdx = -1;
    private int instanceId;
    private int registerRestrition;

    /**************************************************************************/
    /* CatLocRPCServiceAsync                                                  */
    /**************************************************************************/
    @Inject
    private CatLocSelectorRPCServiceAsync catLocService;

    public CatLocSelectorRPCServiceAsync getCatLocService() {
        return catLocService;
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    /**
     * Bind handlers:
     * <ul>
     *   <li>suggestBox focus handler,</li>
     *   <li>suggestBox selection handler,</li>
     *   <li>browse button handler,</li>
     *   <li>submit button handler,</li>
     * </ul>
     */
    @Override
    public void bindView() {
        /** FOCUS. **/
        view.getSearchBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                CatLocSuggestionDisplay display =
                    ((CatLocSuggestionDisplay) view.getSearchBox().getSuggestionDisplay());
                display.setLoadingPopupPosition(view.getSearchBox());
                //show actual suggest list and remove error style if any
                if (view.getSearchBox().getText().isEmpty()) {
                    display.showShortItemsInfo(Constants.MIN_CHARS_TO_SEARCH);
                }
                view.getSearchBox().showSuggestionList();
            }
        }, FocusEvent.getType());
        view.getSearchBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                updatingTableItemIdx = -1;
                ICatLocDetail selected = ((CatLocSuggestionDetail) event.getSelectedItem()).getCatLoc();
                if (!isAlreadySelected(selected)) {
                    eventBus.requestHierarchy(builder.getSelectorType(), selected, instanceId);
                } else {
                    getCatLocSuggestionPopup().showItemAlreadyAdded();
                }
            }
        });
        view.getBrowseBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                updatingTableItemIdx = -1;
                eventBus.initCatLocSelector(popup.getSelectorPanel(), browserBuilder);
                popup.show();
            }
        });
        popup.getSubmitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (updatingTableItemIdx != -1) {
                    view.getTableDataProvider().getList().remove(updatingTableItemIdx);
                    view.setSelectedCountLabel(view.getTableDataProvider().getList().size(), registerRestrition);
                }
                final List<ICatLocDetail> selectedCatLocs = new ArrayList<ICatLocDetail>();
                eventBus.fillCatLocs(selectedCatLocs, browserBuilder.getInstanceId());
                eventBus.addCatLocs(selectedCatLocs, builder.getInstanceId());
            }
        });
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Call when new intance of SelectorManager is requested.
     */
    public void onInitNewCatLocSelectorManager(SimplePanel embedWidget, CatLocSelectorBuilder builder) {
        this.builder = builder;
        this.instanceId = builder.getInstanceId();
        this.registerRestrition = builder.getSelectionRestriction();
        this.browserBuilder = new CatLocSelectorBuilder.Builder(Constants.CAT_LOC_SELECTOR_MODULE)
            .initSelectorCellBrowser()
            .setSelectionRestriction(builder.getSelectionRestriction())
            .setCheckboxes(builder.getCheckboxes())
            .setSelectorType(builder.getSelectorType())
            .build();
        initCatLocSelectorManager(embedWidget, builder);
    }

    /**
     * Call when same intance of SelectorManager is requested.
     */
    public void onInitSameCatLocSelectorManager(SimplePanel embedWidget, CatLocSelectorBuilder builder) {
        if (this.instanceId == builder.getInstanceId()) {
            initCatLocSelectorManager(embedWidget, builder);
        }
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Displays selected item in table.
     * Displays whole item hierarchy.
     * @param result item hierarchy as list
     * @param instanceId instance id
     */
    public void onResponseHierarchyForManager(LinkedList<CatLocTreeItem> result, int instanceId) {
        if (this.instanceId == instanceId) {
            if (view.getTableDataProvider().getList().size() >= registerRestrition) {
                Window.alert(Storage.MSGS.commonCategorySelectionRestriction());
                view.getSearchBox().setText("");
            } else {
                if (updatingTableItemIdx == -1) {
                    view.getSearchBox().setText("");
                    view.getTableDataProvider().getList().add(result);
                } else {
                    view.getTableDataProvider().getList().add(updatingTableItemIdx, result);
                }
                view.setSelectedCountLabel(view.getTableDataProvider().getList().size(), registerRestrition);
            }
        }
    }

    /**
     * Append given list with selected catLocs.
     * If given list is null, initialize it first.
     * Only active presenter response to this call.
     */
    public void onFillCatLocsFromManager(List<ICatLocDetail> selectedCatLocs, int instanceId) {
        if (this.instanceId == instanceId) {
            if (selectedCatLocs == null) {
                selectedCatLocs = new ArrayList<ICatLocDetail>();
            } else {
                selectedCatLocs.clear();
            }
            for (LinkedList<CatLocTreeItem> tableItem : view.getTableDataProvider().getList()) {
                selectedCatLocs.add(tableItem.getLast().getCatLoc());
            }
        }
    }

    /**
     * Same like <b>onAddCatLocs</b> method but clears table fist.
     * @param catLocs - list of items to be set
     */
    public void onSetCatLocs(List<ICatLocDetail> catLocs, int instanceId) {
        if (this.instanceId == instanceId) {
            view.getTableDataProvider().getList().clear();
            onAddCatLocs(catLocs, instanceId);
        }
    }

    /**
     * Sets items to table without checking restrictions.
     * Used for setting manager widget at the begining.
     * Request for full item hierarchy before adding to table.
     * @param catLocs list of items to be set
     */
    public void onAddCatLocs(List<ICatLocDetail> catLocs, int instanceId) {
        if (this.instanceId == instanceId) {
            updatingTableItemIdx = -1;
            for (ICatLocDetail cat : catLocs) {
                if (isAlreadySelected(cat)) {
                    Window.alert("Item \"".concat(cat.getName()).concat("\" already added."));
                } else {
                    eventBus.requestHierarchy(builder.getSelectorType(), cat, instanceId);
                }
            }
        }
    }

    /**************************************************************************/
    /* Presenter events                                                       */
    /**************************************************************************/
    /**
     * Fired by browse button in table.
     * Manages editing selected catLoc.
     * @param idx updating table row index
     * @param catLocHierarchy selected item - catLoc's hierarchy
     */
    public void tableBrowseHandler(int idx, LinkedList<CatLocTreeItem> catLocHierarchy, int instanceId) {
        if (this.instanceId == instanceId) {
            updatingTableItemIdx = idx;
            eventBus.initCatLocSelector(popup.getSelectorPanel(), browserBuilder);
            eventBus.responseHierarchy(catLocHierarchy, browserBuilder.getInstanceId());
            popup.show();
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the CatLocSuggestionDsiaply popup
     */
    public CatLocSuggestionDisplay getCatLocSuggestionPopup() {
        return ((CatLocSuggestionDisplay) view.getSearchBox().getSuggestionDisplay());
    }

    /**
     * @return the registration restriction number
     */
    public int getRegisterRestriction() {
        return this.registerRestrition;
    }

    /**
     * @return the CatLocSelectorBuilder
     */
    public CatLocSelectorBuilder getBuilder() {
        return this.builder;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Init catLoc SelectorManager.
     * Build SelectorManager to given CatLocSelectorBuilder and
     * set SelectorManager widget to given holder.
     *
     * @param embedWidget - SelectorManager's holder
     * @param builder - defines SelectorManager attributes
     * @param instanceId - distinguish individual instances
     */
    private void initCatLocSelectorManager(SimplePanel embedToWidget, CatLocSelectorBuilder builder) {
        //reset in case of calling second time
        view.getTableDataProvider().getList().clear();
        view.setSelectedCountLabel(0, registerRestrition);
        view.setSelectorType(builder.getSelectorType());

        embedToWidget.setWidget(view.getWidgetView());
    }

    /**
     * Check if item is already selected.
     * @param catLocDetail - selected item
     * @return true if selected, false otherwise
     */
    private boolean isAlreadySelected(ICatLocDetail catLocDetail) {
        for (LinkedList<CatLocTreeItem> item : view.getTableDataProvider().getList()) {
            if (item.getLast().getCatLoc().equals(catLocDetail)) {
                return true;
            }
        }
        return false;
    }
}
