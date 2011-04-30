package cz.poptavka.sample.client.common.category;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
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

    private boolean init = false;

    public interface CategorySelectorInterface extends LazyView {

        Grid getListHolder();

        ListBox getSelectedList();

        void toggleLoader();

        void addToSelectedList(String text, String value);

        void removeFromSelectedList();

        int getListIndex();

        ListBox createListAtIndex(int index);

        boolean isSubcategoryListEmpty();

        Widget getWidgetView();

        void clearChildrenLists(int i);
    }

    private static final Logger LOGGER = Logger.getLogger("CategorySelectorPresenter");

    public void bindView() {
        view.getSelectedList().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.removeFromSelectedList();
            }
        });
    }

    public void onInitCategoryWidget(HasOneWidget embedWidget) {
        LOGGER.info("init Category Widget ... ");
        eventBus.getChildListCategories(0, "ALL_CATEGORIES");
        embedWidget.setWidget(view.getWidgetView());
        init = false;
    }

    public void onSetCategoryListData(int newListPosition, ArrayList<CategoryDetail> list) {
        ListBox listBox = view.createListAtIndex(newListPosition);
        setAndDisplayData(listBox, list);
        addCategoryChangeHandler(listBox, newListPosition);
    }

    /** Demand cration getValues method. **/
    public void onGetSelectedCategoryCodes() {
        LOGGER.info("Getting/Pushing categories");
        ListBox tmp = view.getSelectedList();
        ArrayList<String> codes = new ArrayList<String>();
        for (int i = 0; i < tmp.getItemCount(); i++) {
            codes.add(tmp.getValue(i));
        }
        eventBus.pushSelectedCategoryCodes(codes);
    }

    private void setAndDisplayData(final ListBox box, final ArrayList<CategoryDetail> list) {
        box.setVisible(true);
        LOGGER.info("Filling list...");
        for (int i = 0; i < list.size(); i++) {
            box.addItem(list.get(i).getParentName(), String.valueOf(list.get(i).getId()));
        }
        LOGGER.info("List filled");

        //display
        view.getListHolder().setWidget(0, view.getListIndex(), box);
    }

    private void addCategoryChangeHandler(final ListBox box, final int index) {
        box.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                String text = box.getItemText(box.getSelectedIndex());
                String value = box.getValue(box.getSelectedIndex());
                if (text.contains(" >")) {
                    view.clearChildrenLists(index);
                    eventBus.getChildListCategories(index + 1, box.getValue(box.getSelectedIndex()));
                    LOGGER.fine("Next table is at index " + index + 1);
                } else {
                    view.addToSelectedList(text, value);
                }
            }
        });
    }

}
