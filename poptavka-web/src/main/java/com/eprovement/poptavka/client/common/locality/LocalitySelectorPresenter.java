package com.eprovement.poptavka.client.common.locality;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

@Presenter(view = LocalitySelectorView.class, multiple = true)
public class LocalitySelectorPresenter
        extends LazyPresenter<LocalitySelectorPresenter.LocalitySelectorInterface, RootEventBus> {

    /** View interface methods. **/
    public interface LocalitySelectorInterface extends LazyView {

        void createCellBrowser(int checkboxes, int displayCountsOfWhat);

        void setSelectedCountLabel(int count);

        ListDataProvider<LocalityDetail> getCellListDataProvider();

        MultiSelectionModel<LocalityDetail> getCellBrowserSelectionModel();

        SingleSelectionModel<LocalityDetail> getCellListSelectionModel();

        boolean isValid();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**
     * Selection restriction.
     * True - allow only Constants.REGISTER_MAX_LOCALITIES to be selected.
     * False - no restrictions to selection.
     */
    private boolean selectionRestrictions = false;

    /**************************************************************************/
    /* LocalityRPCServiceAsync                                                */
    /**************************************************************************/
    @Inject
    private LocalityRPCServiceAsync localityService;

    public LocalityRPCServiceAsync getLocalityService() {
        return localityService;
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getCellBrowserSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                List<LocalityDetail> selectedList = new ArrayList<LocalityDetail>(
                        view.getCellBrowserSelectionModel().getSelectedSet());
                if (selectionRestrictions
                        && (view.getCellBrowserSelectionModel().getSelectedSet().size()
                        > Constants.REGISTER_MAX_CATEGORIES)) {
                    Window.alert(Storage.MSGS.commonLocalitySelectionRestriction());
                    view.getCellBrowserSelectionModel().setSelected(getSelectedObjectOverAllowedMax(), false);
                } else {
                    view.getCellListDataProvider().setList(selectedList);
                    if (selectionRestrictions) {
                        view.setSelectedCountLabel(selectedList.size());
                    }
                }
            }
        });
        view.getCellListSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.getCellBrowserSelectionModel().setSelected(
                        view.getCellListSelectionModel().getSelectedObject(),
                        false);
                view.getCellListDataProvider().getList().remove(
                        view.getCellListSelectionModel().getSelectedObject());
            }
        });
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     *
     * @param embedWidget - panel where widget will be set up.
     * @param checkboxes - Constants.WITHOUT_CHECK_BOXES /
     *                     Constants.WITH_CHECK_BOXES    /
     *                     Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS
     * @param displayCountsOfWhat - LocalityCell.DISPLAY_COUNT_OF_DEMANDS   /
     *                              LocalityCell.DISPLAY_COUNT_OF_SUPPLIERS /
     *                              LocalityCell.DISPLAY_COUNT_DISABLED
     */
    public void initLocalityWidget(SimplePanel embedWidget, int checkboxes, int displayCountsOfWhat,
            List<LocalityDetail> localitiesToSet, boolean selectionRestrictions) {
        view.createCellBrowser(checkboxes, displayCountsOfWhat);
        this.selectionRestrictions = selectionRestrictions;
        embedWidget.setWidget(view.getWidgetView());

        //Set localities if any
        if (localitiesToSet != null) {
            for (LocalityDetail locDetail : localitiesToSet) {
                //Select in cellBrowser
                view.getCellBrowserSelectionModel().setSelected(locDetail, true);
                //Display selected categories
                view.getCellListDataProvider().getList().add(locDetail);
            }
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private LocalityDetail getSelectedObjectOverAllowedMax() {
        for (LocalityDetail selectedLocality : view.getCellBrowserSelectionModel().getSelectedSet()) {
            if (!view.getCellListDataProvider().getList().contains(selectedLocality)) {
                return selectedLocality;
            }
        }
        return new LocalityDetail();
    }
}
