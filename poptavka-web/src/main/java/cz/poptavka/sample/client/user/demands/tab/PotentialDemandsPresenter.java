package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DetailWrapperPresenter;
import cz.poptavka.sample.client.user.demands.widgets.MessageWriteWidget;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

/**
 * Presenter for handling view actions.
 *
 * @author Beho
 *
 */
@Presenter(view = PotentialDemandsView.class, multiple = true)
public class PotentialDemandsPresenter extends
    LazyPresenter<PotentialDemandsPresenter.IPotentialDemands, UserEventBus> {

    public interface IPotentialDemands extends LazyView {
        Widget getWidgetView();

        Button getReplyButton();
        Button getDeleteButton();
        Button getActionButton();
        Button getRefreshButton();

        ListDataProvider<DemandDetail> getDataProvider();

        MultiSelectionModel<DemandDetail> getSelectionModel();

        Set<DemandDetail> getSelectedSet();

        // TODO clean up detail section

//        DetailWrapperView getDetailSection();

        SimplePanel getDetailSection();
    }

    private DetailWrapperPresenter detailPresenter = null;

    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                // TODO fix multiSelection
                GWT.log("SIZE of result set: " + view.getSelectedSet().size());
                if (view.getSelectedSet().size() != 1) {
                    // do not show any demand detail
                    GWT.log("NOT ONE");
                    return;
                }
                GWT.log("ONE");
                Iterator<DemandDetail> iter = view.getSelectedSet().iterator();
//                if (iter.hasNext()) {
                long item = iter.next().getId();
//                }
                eventBus.getDemandDetail(item, DetailType.POTENTIAL);
            }
        });
    }

    public void onInvokePotentialDemands() {
        // TODO call real method to get potential demands
        eventBus.requestClientDemands();
    }

    public void onResponseClientDemands(ArrayList<DemandDetail> data) {

        List<DemandDetail> list = view.getDataProvider().getList();
        list.clear();
        for (DemandDetail d : data) {
            list.add(d);
        }
        view.getDataProvider().refresh();

        // Init DetailWrapper for this view
        if (detailPresenter == null) {
            detailPresenter = eventBus.addHandler(DetailWrapperPresenter.class);
            detailPresenter.setType(DetailType.POTENTIAL);
            detailPresenter.initDetailWrapper(view.getDetailSection());
        }

        // widget display
        eventBus.displayContent(view.getWidgetView());
    }

    public void onResponseDemandDetail(Widget widget) {
        FlowPanel panel = new FlowPanel();
        MessageWriteWidget responser = new MessageWriteWidget();
        panel.add(widget);
        panel.add(responser);
        // TODO clean up setting detail panel
//        view.getDetailSection().setDetail(panel);

        view.getDetailSection().setWidget(panel);

        responser.getReplyButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                eventBus.sendMessage(responser.getContent());
            }
        });
    }

    public void cleanDetailWrapperPresenterForDevelopment() {
        GWT.log("WRAPPER REMOVED");
        eventBus.removeHandler(detailPresenter);
    }


}
