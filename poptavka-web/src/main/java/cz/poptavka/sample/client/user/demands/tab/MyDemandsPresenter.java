package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

@Presenter(view = MyDemandsView.class, multiple = true)
public class MyDemandsPresenter extends
        LazyPresenter<MyDemandsPresenter.MyDemandsInterface, UserEventBus> {

    public interface MyDemandsInterface extends LazyView {
        Widget getWidgetView();

        Button getAnswerBtn();

        Button getEditBtn();

        Button getCloseBtn();

        Button getCancelBtn();

        CellTable<MessageDetail> getCellTable();

        SingleSelectionModel<MessageDetail> getSelectionModel();

        SimplePanel getDetailSection();

        ListDataProvider<MessageDetail> getDataProvider();
    }

    private DetailWrapperPresenter detailPresenter = null;

    public void bindView() {
        view.getCellTable().getSelectionModel()
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {
                        MessageDetail selected = view.getSelectionModel().getSelectedObject();
                        if (selected != null) {
                            // event calls from the click
                            eventBus.getDemandDetail(selected.getDemandId(), ViewType.EDITABLE);

                            // TODO delete this and uncomment the one below
//                            eventBus.getDemandMessages(0, ViewType.EDITABLE);

//                            eventBus.getDemandMessages(selected.getId(), ViewType.POTENTIAL);
//                            detailPresenter.setMessageId(selected.getMessageId());
                        }
                    }
                });
    }

    public void onInvokeMyDemands() {
        // Init DetailWrapper for this view
        if (detailPresenter == null) {
            detailPresenter = eventBus.addHandler(DetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(),
                    ViewType.EDITABLE);
        }
        // TODO temporal
        PopupPanel panel = new PopupPanel(true);
        panel.getElement().setInnerText("No GET demands event call");
        panel.show();
        GWT.log("Demands are on the way - getDemands!");
        eventBus.requestClientDemands();

        eventBus.displayContent(view.getWidgetView());
    }

    public void onResponseClientDemands(ArrayList<MessageDetail> demandMessageList) {
        List<MessageDetail> list = view.getDataProvider().getList();
        list.clear();
        for (MessageDetail m : demandMessageList) {
            list.add(m);
        }
        GWT.log("DATA SIZE: " + list.size());
        refreshDisplays();
    }

//    public void onResponseClientDemands(ArrayList<ClientDemandDetail> demands) {
//        GWT.log("Demands are on the way.    demands.size = " + demands.size());
//
//        // Add the data to the data provider, which automatically pushes it to
//        // the widget.
//        view.getDataProvider().getList().clear();
//        view.getDataProvider().getList().addAll(demands);
//        refreshDisplays();
//    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        view.getDataProvider().refresh();
    }

    public void onResponseDemandDetail(Widget widget) {
        view.getDetailSection().setWidget(widget);
    }

    // TODO delete, just devel tool
    public void cleanDetailWrapperPresenterForDevelopment() {
        if (detailPresenter != null) {
            eventBus.removeHandler(detailPresenter);
        }
    }

}
