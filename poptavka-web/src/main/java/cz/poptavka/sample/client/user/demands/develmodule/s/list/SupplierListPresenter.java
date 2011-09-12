package cz.poptavka.sample.client.user.demands.develmodule.s.list;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.demands.develmodule.DemandModuleEventBus;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

@Presenter(view = SupplierList.class, multiple = true)
public class SupplierListPresenter extends LazyPresenter<SupplierListPresenter.IList, DemandModuleEventBus> {

    public interface IList extends LazyView {
        Widget getWidgetView();

        SupplierListGrid<PotentialDemandMessage> getGrid();

        ListDataProvider<PotentialDemandMessage> getDataProvider();
    }

    private boolean initialized = false;

    public void bindView() {
        view.getGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                // TODO Auto-generated method stub
                Window.alert(event.getSource().getClass().getName());
            }
        });
    }


    //init view and fetch new supplier's demands
    //demand request is sent ONLY for the first time - when view is loaded
    public void onInitSupplierList() {
//        commented code is from production code
//        if (!initialized) {
        eventBus.requestSupplierNewDemands();
//        }
        eventBus.displayView(view.getWidgetView());
        initialized = true;
    }

    //initial dataGrid data fill
    public void onResponseSupplierNewDemands(ArrayList<PotentialDemandMessage> data) {
        GWT.log("++ onResponseSupplierNewDemands");

        List<PotentialDemandMessage> list = view.getDataProvider().getList();
        list.clear();
        for (PotentialDemandMessage d : data) {
            list.add(d);
        }
        view.getDataProvider().refresh();
    }

}
