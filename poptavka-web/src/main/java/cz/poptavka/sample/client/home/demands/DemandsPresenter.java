package cz.poptavka.sample.client.home.demands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
/**
 *
 * @author Martin Slavkovsky
 *
 */
@Presenter(view = DemandsView.class)
public class DemandsPresenter extends
        BasePresenter<DemandsPresenter.DemandsViewInterface, DemandsEventBus> {

    private static final Logger LOGGER = Logger
            .getLogger(DemandsPresenter.class.getName());

    public interface DemandsViewInterface {

        Widget getWidgetView();

        ListBox getCategoryList();

        ListBox getLocalityList();
    }

    /**
     * Bind objects and theirs action handlers.
     */
    public void bind() {
        view.getCategoryList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                LOGGER.info("OnCategoryListChange");
                eventBus.getCategory(Long.parseLong(view.getCategoryList().getValue(
                        view.getCategoryList().getSelectedIndex())));
            }
        });
        view.getLocalityList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                LOGGER.info("OnLocalityListChange");
                eventBus.getLocality(Long.parseLong(view.getLocalityList().getValue(
                        view.getLocalityList().getSelectedIndex())));
            }
        });
    }

    /**
     * Try retrieve and display all demands.
     * Get all categories and localities to display in listBoxes for later filtering.
     *
     */
    public void onAtDemands() {
        LOGGER.info("Starting demands presenter...");
        LOGGER.info("Getting categories...");
        eventBus.getCategories();
        LOGGER.info("Getting localities...");
        eventBus.getLocalities();
        LOGGER.info("Getting demands...");
        eventBus.getDemands();
        eventBus.setBodyWidget(view.getWidgetView());
    }


    /**
     * Fills category listBox with given list of localities.
     * @param box - listBox to be filled
     * @param list - data (categories)
     */
    public void onSetCategoryData(final ArrayList<CategoryDetail> list) {
        final ListBox box = view.getCategoryList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                LOGGER.info("Filling category list...");
                box.addItem("Categories...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(),
                            String.valueOf(list.get(i).getId()));
                }
                box.setSelectedIndex(0);
                LOGGER.info("Category List filled");
            }
        });
    }

    /**
     * Fills locality listBox with given list of localities.
     * @param box - listBox to be filled
     * @param list - data (localities)
     */
    public void onSetLocalityData(final ArrayList<LocalityDetail> list) {
        final ListBox box = view.getLocalityList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                LOGGER.info("Filling locality list...");
                box.addItem("Localities...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName());
                }
                box.setSelectedIndex(0);
                LOGGER.info("Locality List filled");
            }
        });
    }

    public void onDisplayDemands(Collection<Demand> result) {
        LOGGER.info("Displaying demands...");
        eventBus.displayDemands(result);
    }
}
