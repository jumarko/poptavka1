/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.actionBox;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuItem;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ActionBoxView.class, multiple = true)
public class ActionBoxPresenter extends LazyPresenter<ActionBoxPresenter.ActionBoxViewInterface, RootEventBus> {

    public interface ActionBoxViewInterface extends LazyView, IsWidget {

        MenuItem getActionRead();

        MenuItem getActionUnread();

        MenuItem getActionStar();

        MenuItem getActionUnstar();
    }
    private UniversalTableGrid grid;

    public void initActionBox(UniversalTableGrid grid) {
        this.grid = grid;
    }

    @Override
    public void bindView() {
        view.getActionRead().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.requestReadStatusUpdate(grid.getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnread().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.requestReadStatusUpdate(grid.getSelectedUserMessageIds(), false);
            }
        });
        view.getActionStar().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.requestStarStatusUpdate(grid.getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnstar().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.requestStarStatusUpdate(grid.getSelectedUserMessageIds(), false);
            }
        });
    }

    public void updateStar(long objectId, boolean value) {
        eventBus.requestStarStatusUpdate(Arrays.asList(objectId), value);
    }
}
