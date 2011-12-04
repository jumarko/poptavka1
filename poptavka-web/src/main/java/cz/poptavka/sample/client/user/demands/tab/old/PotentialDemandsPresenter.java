package cz.poptavka.sample.client.user.demands.tab.old;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import cz.poptavka.sample.client.user.widget.grid.unused.PotentialDemandTable;
import cz.poptavka.sample.client.user.widget.unused.OldDetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.type.ViewType;

/**
 * Presenter for handling view actions.
 *
 * @author Beho
 *
 */
@Presenter(view = PotentialDemandsView.class, multiple = true)
public class PotentialDemandsPresenter extends
    LazyPresenter<PotentialDemandsPresenter.IPotentialDemands, UserEventBus> {

    private static final ViewType DETAIL_TYPE = ViewType.POTENTIAL;

    public interface IPotentialDemands extends LazyView {
        Widget getWidgetView();

        Button getReplyButton();
        Button getDeleteButton();
        Button getActionButton();
        Button getRefreshButton();
        Button getMarkReadButton();
        Button getMarkUnreadButton();

        PotentialDemandTable getDemandTable();

        ListDataProvider<PotentialDemandMessage> getDataProvider();

        MultiSelectionModel<PotentialDemandMessage> getSelectionModel();

        Set<PotentialDemandMessage> getSelectedSet();

        SimplePanel getDetailSection();
    }

    private static final StyleResource RSC = GWT.create(StyleResource.class);

    private OldDetailWrapperPresenter detailPresenter = null;
    private boolean presenterLoaded = false;

    /** GUI buttons */

    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (view.getSelectedSet().size() == 1) {
                    Iterator<PotentialDemandMessage> iter = view.getSelectedSet().iterator();
                    PotentialDemandMessage selected = iter.next();

                    eventBus.getDemandDetail(selected.getDemandId(), DETAIL_TYPE);
                    eventBus.requestPotentialDemandConversation(selected.getMessageId(), selected.getUserMessageId());

                    //default set this demand as read
                    // TODO verify, if this is automatically done on server side as well
                    markMessagesAsRead(selected, true);
                }
            }
        });
        view.getMarkReadButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Iterator<PotentialDemandMessage> it = view.getSelectedSet().iterator();
                ArrayList<Long> messages = new ArrayList<Long>();
                while (it.hasNext()) {
                    PotentialDemandMessage d = it.next();
                    markMessagesAsRead(d, true);
                    messages.add(d.getUserMessageId());
                }
                eventBus.requestPotentialDemandReadStatusChange(messages, true);
            }
        });
        view.getMarkUnreadButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Iterator<PotentialDemandMessage> it = view.getSelectedSet().iterator();
                ArrayList<Long> messages = new ArrayList<Long>();
                while (it.hasNext()) {
                    PotentialDemandMessage d = it.next();
                    markMessagesAsRead(d, false);
                    messages.add(d.getUserMessageId());
                }
                eventBus.requestPotentialDemandReadStatusChange(messages, false);
            }
        });
    }

    public void onInvokePotentialDemands() {
        if (presenterLoaded) {
            //eventBus.displayContent(view.getWidgetView());
            return;
        }
        eventBus.requestPotentialDemands();
        presenterLoaded = true;
    }

    public void onResponsePotentialDemands(ArrayList<PotentialDemandMessage> data) {

        GWT.log("Size on frontEnd: " + data.size());
        List<PotentialDemandMessage> list = view.getDataProvider().getList();
        list.clear();
        for (PotentialDemandMessage d : data) {
            list.add(d);
        }
        GWT.log("** DEBUG onResponsePotentialDemands NEW ");

        view.getDataProvider().refresh();

        // Init DetailWrapper for this view
        if (detailPresenter == null) {
            detailPresenter = eventBus.addHandler(OldDetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(), ViewType.POTENTIAL);
        }

        // widget display
        //eventBus.displayContent(view.getWidgetView());
    }

    public void markMessagesAsRead(PotentialDemandMessage detail, boolean isRead) {
        List<PotentialDemandMessage> list = view.getDataProvider().getList();
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
