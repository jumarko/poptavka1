package cz.poptavka.sample.client.main.common.category;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.MainEventBus;
import cz.poptavka.sample.shared.domain.CategoryDetail;

@Presenter(view = CategorySelectorView.class, multiple = true)
public class CategorySelectorPresenter
    extends LazyPresenter<CategorySelectorPresenter.CategorySelectorInterface, MainEventBus> {

    public interface CategorySelectorInterface extends LazyView {

        Grid getListHolder();

        ListBox getSelectedList();

        boolean isValid();

        void addToSelectedList(String text, String value);

        void removeFromSelectedList();

        int getFreeListIndex();

        ListBox createListAtIndex(int index);

        Widget getWidgetView();

        ScrollPanel getScrollPanel();

        void clearChildrenLists(int i);

        ArrayList<String> getSelectedCategoryCodes();

        void showLoader(int index);
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

    public void initCategoryWidget(SimplePanel embedWidget) {
        LOGGER.info("init Category Widget ... ");
        view.getListHolder().resizeColumns(0);
        eventBus.getChildListCategories(0, "ALL_CATEGORIES");
        embedWidget.setWidget(view.getWidgetView());
    }

    private void postRequest(int index, String category) {
        view.showLoader(index);
        eventBus.getChildListCategories(index, category);
    }

    public void onSetCategoryListData(int newListPosition, ArrayList<CategoryDetail> list) {
        ListBox listBox = view.createListAtIndex(newListPosition);
        setAndDisplayData(listBox, list);
        addCategoryChangeHandler(listBox, newListPosition);
    }

    private void setAndDisplayData(final ListBox box, final ArrayList<CategoryDetail> list) {
        /** DEVEL PART **/
        int columCount = view.getListHolder().getColumnCount();

        /** END **/
        box.setVisible(true);
        LOGGER.info("Filling list...");
        for (int i = 0; i < list.size(); i++) {
            box.addItem(list.get(i).getParentName(), String.valueOf(list.get(i).getId()));
        }
        LOGGER.info("List filled");

        //check if possible to display, if needed resize table
        int positionToInsert = view.getFreeListIndex();
        if (columCount == positionToInsert) {
            view.getListHolder().resizeColumns(columCount + 1);
        }
        view.getListHolder().setWidget(0, positionToInsert, box);
        view.getScrollPanel().scrollToRight();
    }

    private void addCategoryChangeHandler(final ListBox box, final int index) {
        box.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String text = box.getItemText(box.getSelectedIndex());
                String value = box.getValue(box.getSelectedIndex());
                boolean notLeaf = text.contains(" >");
                if (event.isControlKeyDown() && notLeaf) {
                    view.addToSelectedList(text.substring(0, text.indexOf(" >")), value);
                } else {
                    if (text.contains(" >")) {
                        view.clearChildrenLists(index);
                        postRequest(index + 1, box.getValue(box.getSelectedIndex()));
                        LOGGER.fine("Next table is at index " + index + 1);
                    } else {
                        view.addToSelectedList(text, value);
                    }
                }
            }
        });
    }

}
