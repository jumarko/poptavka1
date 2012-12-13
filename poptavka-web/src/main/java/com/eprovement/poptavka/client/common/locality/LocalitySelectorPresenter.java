package com.eprovement.poptavka.client.common.locality;

import java.util.ArrayList;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import java.util.List;

@Presenter(view = LocalitySelectorView.class, multiple = true)
public class LocalitySelectorPresenter
        extends LazyPresenter<LocalitySelectorPresenter.LocalitySelectorInterface, RootEventBus> {

    @Inject
    private LocalityRPCServiceAsync localityService;

    /** View interface methods. **/
    public interface LocalitySelectorInterface extends LazyView {

        void createCellBrowser(int checkboxes, int displayCountsOfWhat);

        ListDataProvider<LocalityDetail> getCellListDataProvider();

        MultiSelectionModel<LocalityDetail> getCellBrowserSelectionModel();

        SingleSelectionModel<LocalityDetail> getCellListSelectionModel();

        void toggleLoader();

        boolean isValid();

        Widget getWidgetView();
    }
    // for preventing users from double clicking list item, what would result in multiple instances of
    // same list
    private boolean preventMultipleCalls = false;

    @Override
    public void bindView() {
        view.getCellBrowserSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                List<LocalityDetail> selectedList = new ArrayList<LocalityDetail>(
                        view.getCellBrowserSelectionModel().getSelectedSet());
                view.getCellListDataProvider().setList(selectedList);
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

    public LocalityRPCServiceAsync getLocalityService() {
        return localityService;
    }

    public void initLocalityWidget(SimplePanel embedWidget, int checkboxes, int displayCountsOfWhat,
            List<LocalityDetail> localitiesToSet) {
        view.createCellBrowser(checkboxes, displayCountsOfWhat);
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

    //TODO premenovat
    public void onSetLocalityData(LocalityType localityType, List<LocalityDetail> list) {
        switch (localityType) {
            case DISTRICT:
                view.toggleLoader();
//                setData(view.getDistrictList(), list);
                break;
            case REGION:
//                setData(view.getRegionList(), list);
                break;
            case CITY:
                view.toggleLoader();
//                setData(view.getCityList(), list);
                break;
            default:
                break;
        }
    }

    private void setData(final ListBox box, final List<LocalityDetail> list) {
        box.clear();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getCode());
                }
            }
        });
        box.setVisible(true);
        // now he can select list again
        preventMultipleCalls = false;
    }

    public void getRootLocalities(AsyncDataProvider dataProvider) {
        eventBus.getLocalities(LocalityType.COUNTRY, dataProvider);
    }

    public void getLocalities(ListDataProvider dataProvider, LocalityType localityType, String locCode) {
        eventBus.getChildLocalities(localityType, locCode, dataProvider);
    }
}
