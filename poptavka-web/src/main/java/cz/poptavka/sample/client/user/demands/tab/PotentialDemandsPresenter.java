package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.demand.DetailType;
import cz.poptavka.sample.shared.domain.demand.PotentialDemandDetail;

/**
 * Presenter for handling view actions.
 *
 * @author Beho
 *
 */
@Presenter(view = PotentialDemandsView.class, multiple = true)
public class PotentialDemandsPresenter extends
    LazyPresenter<PotentialDemandsPresenter.IPotentialDemands, UserEventBus> {

    private static final DetailType DETAIL_TYPE = DetailType.POTENTIAL;

    public interface IPotentialDemands extends LazyView {
        Widget getWidgetView();

        Button getReplyButton();
        Button getDeleteButton();
        Button getMarkButton();
        Button getActionButton();
        Button getRefreshButton();

        boolean getReadValueForMarkedMessages();

        CellTable<PotentialDemandDetail> getDemandTable();

        ListDataProvider<PotentialDemandDetail> getDataProvider();

        MultiSelectionModel<PotentialDemandDetail> getSelectionModel();

        Set<PotentialDemandDetail> getSelectedSet();

        SimplePanel getDetailSection();
    }

    private static final StyleResource RSC = GWT.create(StyleResource.class);

    private DetailWrapperPresenter detailPresenter = null;
    private boolean loaded = false;

    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Iterator<PotentialDemandDetail> iter = view.getSelectedSet().iterator();
                PotentialDemandDetail selected = iter.next();

                eventBus.getDemandDetail(selected.getDemandId(), DETAIL_TYPE);
                eventBus.requestPotentialDemandConversation(selected.getMessageId());

                //default set this demand as read
                // TODO this should be automatically done on server side as well
                markMessagesAsRead(selected, true);
            }
        });
        view.getMarkButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean isRead = !view.getReadValueForMarkedMessages();
                Iterator<PotentialDemandDetail> it = view.getSelectedSet().iterator();
                ArrayList<Long> messages = new ArrayList<Long>();
                while (it.hasNext()) {
                    PotentialDemandDetail d = it.next();
                    markMessagesAsRead(d, isRead);
                }
                eventBus.requestPotentialDemandReadStatusChange(messages, isRead);
            }
        });
    }

    public void onInvokePotentialDemands() {
        if (loaded) {
            eventBus.displayContent(view.getWidgetView());
            return;
        }
        eventBus.requestPotentialDemands();
        loaded = true;
    }

    public void onResponsePotentialDemands(ArrayList<PotentialDemandDetail> data) {

        List<PotentialDemandDetail> list = view.getDataProvider().getList();
        list.clear();
        for (PotentialDemandDetail d : data) {
            if (!d.isRead()) {
            }

            list.add(d);
        }
        GWT.log("** DEBUG onResponsePotentialDemands NEW ");
        view.getDataProvider().refresh();

        // Init DetailWrapper for this view
        if (detailPresenter == null) {
            detailPresenter = eventBus.addHandler(DetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(), DetailType.POTENTIAL);
        }

        // widget display
        eventBus.displayContent(view.getWidgetView());
    }

    public void markMessagesAsRead(PotentialDemandDetail detail, boolean isRead) {
        List<PotentialDemandDetail> list = view.getDataProvider().getList();
        list.get(list.indexOf(detail)).setRead(isRead);
        view.getDataProvider().refresh();
    }


    // TODO delete, just devel tool
    public void cleanDetailWrapperPresenterForDevelopment() {
        if (detailPresenter != null) {
            eventBus.removeHandler(detailPresenter);
        }
    }

}
