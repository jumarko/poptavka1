package cz.poptavka.sample.client.common.category;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.shared.domain.CategoryDetail;

@Presenter(view = CategorySelectorView.class)
public class CategorySelectorPresenter extends
    LazyPresenter<CategorySelectorPresenter.CategorySelectorInterface, CommonEventBus> {

    public enum CategoryType {
        ROOT, MAIN, LEAF
    }

    public interface CategorySelectorInterface extends LazyView {

        ListBox getRootCategoryList();

        ListBox getCategoryList();

        ListBox getSubCategoryList();

        ListBox getSelectedList();

        String getSelectedItem(CategoryType list);

        void toggleLoader();

        void addToSelectedList();

        void removeFromSelectedList();

        Widget getWidgetView();
    }

    private static final Logger LOGGER = Logger.getLogger("CategorySelectorPresenter");

    public void bindView() {
        view.getRootCategoryList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.toggleLoader();
                view.getCategoryList().setVisible(false);
                view.getSubCategoryList().setVisible(false);
                eventBus.getChildCategories(CategoryType.MAIN, view.getSelectedItem(CategoryType.ROOT));
            }
        });
        view.getCategoryList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                view.toggleLoader();
                view.getSubCategoryList().setVisible(false);
            }
        });
        view.getSubCategoryList().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.addToSelectedList();
            }
        });
        view.getSelectedList().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.removeFromSelectedList();
            }
        });
    }

    public void onInitCategoryWidget(HasOneWidget embedWidget) {
        LOGGER.info("launching Category service RPC call ... ");
        if (!(view.getRootCategoryList().getItemCount() > 0)) {
            eventBus.getRootCategories();
        }
        embedWidget.setWidget(view.getWidgetView());
    }

    public void onSetCategoryData(CategoryType type, ArrayList<CategoryDetail> list) {
        switch (type) {
            case ROOT:
                setData(view.getRootCategoryList(), list);
                break;
            case MAIN:
                view.toggleLoader();
                setData(view.getCategoryList(), list);
                break;
            case LEAF:
                view.toggleLoader();
                setData(view.getSubCategoryList(), list);
                break;
            default:
                break;
        }
    }

    public void onGetSelectedCategoryCodes() {
        ListBox tmp = view.getSelectedList();
        ArrayList<String> codes = new ArrayList<String>();
        for (int i = 0; i < tmp.getItemCount(); i++) {
            codes.add(tmp.getValue(i));
        }
        eventBus.pushSelectedCategoryCodes(codes);
    }

    private void setData(final ListBox box, final ArrayList<CategoryDetail> list) {
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                LOGGER.info("Filling list...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), String.valueOf(list.get(i).getId()));
                }
                LOGGER.info("List filled");
            }

        });
    }



}
