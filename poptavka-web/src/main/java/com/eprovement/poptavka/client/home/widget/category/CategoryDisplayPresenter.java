package com.eprovement.poptavka.client.home.widget.category;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.shared.domain.CategoryDetail;

@Presenter(view = CategoryDisplayView.class)
public class CategoryDisplayPresenter
    extends BasePresenter<CategoryDisplayPresenter.CategoryDisplayInterface, RootEventBus> {

    public interface CategoryDisplayInterface  {
        HTML getCategoryView();

        Widget getWidgetView();
    }

    private static final Logger LOGGER = Logger.getLogger(CategoryDisplayPresenter.class.getName());

    public void onInitCategoryDisplay(SimplePanel holderWidget) {
        eventBus.getRootCategories();
        holderWidget.setWidget(view.getWidgetView());
    }

    public void onDisplayRootCategories(ArrayList<CategoryDetail> list) {
        setData(view.getCategoryView(), list);
    }

    public void onSetCategoryDisplayData(ArrayList<CategoryDetail> details) {
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
