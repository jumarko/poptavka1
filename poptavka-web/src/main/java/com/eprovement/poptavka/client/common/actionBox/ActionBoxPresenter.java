/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.actionBox;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.view.client.MultiSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Class for read & star statuses update.
 * ActionBox initialization requires grid from which user messages ids are retrieved.
 * Therefore table's row interpretation must be detail object that implements
 * TableDisplayUserMessage interface and must have MultiSelectionModel.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ActionBoxView.class, multiple = true)
public class ActionBoxPresenter extends LazyPresenter<ActionBoxPresenter.ActionBoxViewInterface, RootEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface ActionBoxViewInterface extends LazyView, IsWidget {

        MenuItem getActionRead();

        MenuItem getActionUnread();

        MenuItem getActionStar();

        MenuItem getActionUnstar();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private UniversalAsyncGrid grid;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * ActionBox initialization requires grid from which user messages ids are retrieved.
     * Therefore table's row interpretation must be detail object that implements
     * TableDisplayUserMessage interface and must have MultiSelectionModel.
     * @param grid
     */
    public void initActionBox(UniversalAsyncGrid grid) {
        this.grid = grid;
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getActionRead().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.requestReadStatusUpdate(getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnread().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.requestReadStatusUpdate(getSelectedUserMessageIds(), false);
            }
        });
        view.getActionStar().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.requestStarStatusUpdate(getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnstar().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.requestStarStatusUpdate(getSelectedUserMessageIds(), false);
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void updateStar(long objectId, boolean value) {
        eventBus.requestStarStatusUpdate(Arrays.asList(objectId), value);
    }

    public void onResponseReadStatusUpdate() {
        grid.refresh();
    }
    public void onResponseStarStatusUpdate() {
        grid.refresh();
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Get IDs of selected objects.
     * @return
     */
    private List<Long> getSelectedUserMessageIds() {
        List<Long> idList = new ArrayList<Long>();
        for (Iterator it = ((MultiSelectionModel)
                grid.getSelectionModel()).getSelectedSet().iterator(); it.hasNext();) {
            TableDisplayUserMessage detail = (TableDisplayUserMessage) it.next();
            idList.add(detail.getUserMessageId());
        }
        return idList;
    }
}
