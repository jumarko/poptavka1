package cz.poptavka.sample.client.home.widget.category;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategoryType;
import cz.poptavka.sample.client.home.HomeEventBus;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.shared.domain.CategoryDetail;

@Presenter(view = CategoryDisplayView.class)
public class CategoryDisplayPresenter
    extends BasePresenter<CategoryDisplayPresenter.CategoryDisplayInterface, HomeEventBus> {

    public interface CategoryDisplayInterface  {
        HTML getCategoryView();

        Widget getWidgetView();
    }

    private static final Logger LOGGER = Logger.getLogger(CategoryDisplayPresenter.class.getName());

    public void bind() {

    }


    public void onInitCategoryDisplay(AnchorEnum anchor) {
        eventBus.getRootCategories();
        eventBus.setHomeWidget(anchor, view.getWidgetView(), false);
    }

    public void onDisplayRootCategories(ArrayList<CategoryDetail> list) {
        setData(view.getCategoryView(), list);
    }

    public void onSetCategoryDisplayData(CategoryType type, ArrayList<CategoryDetail> details) {
        setData(view.getCategoryView(), details);
    }

    private void setData(final HTML categoryView, final List<CategoryDetail> list) {
        categoryView.setVisible(true);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
//                LOGGER.info("Filling list...");
                StringBuilder htmlString = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    CategoryDetail detail = list.get(i);
//                    LOGGER.info("detail" + detail.getName());
                    Anchor categoryAnchor = new Anchor();
                    categoryAnchor.setText(detail.getName());
                    Label categoryLabel = new Label();
                    categoryLabel.setText("" + detail.getDemands());
                    htmlString.append("<a href=" + detail.getName() + ">" + detail.getName() + "</a>"
                            + detail.getDemands());
                }
//                LOGGER.info("List filled");
                categoryView.setHTML(htmlString.toString());
            }
        });
    }

}
