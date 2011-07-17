package cz.poptavka.sample.client.home.suppliers;

import com.google.gwt.event.dom.client.ChangeEvent;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.home.HomeEventBus;

import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Presenter(view = DisplaySuppliersView.class)
public class DisplaySuppliersPresenter
        extends BasePresenter<DisplaySuppliersPresenter.DisplaySuppliersViewInterface, HomeEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DisplaySuppliersPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface DisplaySuppliersViewInterface { //extends LazyView {

        ListBox getLocalityList();

        FlowPanel getPath();

        void addPath(Widget widget);

        void removePath(); //removes last one

        CellList getList();

        SimplePager getPager();

        HorizontalPanel getPanel();

        Widget getWidgetView();

        SingleSelectionModel getSelectionModel();

        SplitLayoutPanel getSplitter();

        void displaySubCategories(int columns, ArrayList<CategoryDetail> categories);
    }
    private int columns = 4;
    private ArrayList<Long> historyTokens = new ArrayList<Long>();

    public void bind() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                CategoryDetail selected = (CategoryDetail) view.getSelectionModel().getSelectedObject();
                //does it really need to be?
                if (selected.getParentName().equals("") || selected.getParentName() == null) {
                    root = true;
                }
                if (selected != null) {
                    historyTokens.add(selected.getId());
                    eventBus.getSubCategories(Long.toString(selected.getId()));
                    eventBus.addToPath(selected);
                }
            }
        });
        view.getLocalityList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                //TODO Martin najdenych dodavatelov podla categorie filtruj aj podla zvolenej lokality
            }
        });
    }
    private String category = "root";

    public void onAddToPath(CategoryDetail categoryDetail) {
        Hyperlink link = new Hyperlink(" -> " + categoryDetail.getName(),
                "!public/addToPath?" + categoryDetail.getId());
        view.addPath(link);
    }

    public void onAtSuppliers() {
        eventBus.loadingShow(MSGS.loading());
        view.getSplitter().getWidget(1).setVisible(false);
        view.getSplitter().getWidget(2).setVisible(false);

        view.getPath().clear();
        historyTokens.clear();
        view.getPath().setVisible(false);

        eventBus.getCategories();
        eventBus.getLocalities();

        Hyperlink link = new Hyperlink("root", "!public/addToPath?root");
        view.addPath(link);
        eventBus.setBodyWidget(view.getWidgetView());
    }

    public void onDisplaySubcategories(ArrayList<CategoryDetail> subcategories) {
        if (root) {
            view.getSplitter().getWidget(1).setVisible(false);
            view.getSplitter().getWidget(2).setVisible(false);
            view.getPath().setVisible(false);
            eventBus.setBodyWidget(view.getWidgetView());
            root = false;
        } else {
            view.getSplitter().getWidget(1).setVisible(true);
            view.getSplitter().getWidget(2).setVisible(true);
            view.getPath().setVisible(true);
        }
        view.displaySubCategories(columns, subcategories);
        eventBus.loadingHide();
    }
    private List<Supplier> suppliers = Arrays.asList(
            new Supplier(), new Supplier(), new Supplier(), new Supplier());
    private boolean root = true;

    /**
     * Fills locality listBox with given list of localities.
     * @param list - data (localities)
     */
    public void onSetLocalityData(final ArrayList<LocalityDetail> list) {
        final ListBox box = view.getLocalityList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem("All localities...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(),
                            String.valueOf(list.get(i).getCode()));
                }
                box.setSelectedIndex(0);
                LOGGER.info("Locality List filled");
            }
        });
    }

    public void onRemoveFromPath(Long id) {
        if (historyTokens.isEmpty()) {
            return;
        }
        int idx = historyTokens.indexOf(id);
        for (int i = historyTokens.size(); i == 0; i--) {
            if (i > idx) {
                view.getPath().remove(i + 1);
                historyTokens.remove(i);
            }
        }
    }
}
