package cz.poptavka.sample.client.home.widget.category;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.HomeEventBus;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.home.widget.category.item.CategoryItemView;
import cz.poptavka.sample.shared.domain.CategoryDetail;

@Presenter(view = CategoryDisplayView.class)
public class CategoryDisplayPresenter
    extends BasePresenter<CategoryDisplayPresenter.CategorySelectorInterface, HomeEventBus> {

    public interface CategorySelectorInterface  {
        CategoryItemView getCategoryItem();

        Widget getWidgetView();
    }

    private static final Logger LOGGER = Logger.getLogger(CategoryDisplayPresenter.class.getName());

    public void bind() {

    }


    public void onInitCategorySelector(AnchorEnum anchor) {
//        eventBus.getRootCategories();
        eventBus.setHomeWidget(anchor, view.getWidgetView(), false);
    }

    public void onDisplayRootCategories(ArrayList<CategoryDetail> list) {
        setData(view.getCategoryItem(), list);
    }

    private void setData(final CategoryItemView categoryItem, final List<CategoryDetail> list) {
        categoryItem.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                LOGGER.info("Filling list...");
                for (int i = 0; i < list.size(); i++) {
                    CategoryDetail detail = list.get(i);
                    Anchor categoryAnchor = new Anchor();
                    categoryAnchor.setText(detail.getName());

                    Label categoryLabel = new Label();
                    categoryLabel.setText("" + detail.getDemands());

                    categoryItem.setItemImage(new Image());
                    categoryItem.setItemLink(categoryAnchor);
                    categoryItem.setItemCount(categoryLabel);
                }
                LOGGER.info("List filled");
            }

        });
    }

}
