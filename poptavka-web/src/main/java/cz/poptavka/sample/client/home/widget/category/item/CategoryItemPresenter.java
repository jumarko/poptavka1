package cz.poptavka.sample.client.home.widget.category.item;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.HomeEventBus;
import cz.poptavka.sample.shared.domain.CategoryDetail;

@Presenter(view = CategoryItemView.class)
public class CategoryItemPresenter
    extends BasePresenter<CategoryItemPresenter.CategoryItemInterface, HomeEventBus> {

    public interface CategoryItemInterface  {

        Label getItemCount();
        Anchor getItemLink();
        Image getItemImage();

        Widget getWidgetView();
    }

    private static final Logger LOGGER = Logger.getLogger(CategoryItemPresenter.class.getName());

    public void bind() {

    }



    private void setData(final ListBox box, final List<CategoryDetail> list) {
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                LOGGER.info("Filling list...");
                for (int i = 0; i < list.size(); i++) {

                }
                LOGGER.info("List filled");
            }

        });
    }

}
