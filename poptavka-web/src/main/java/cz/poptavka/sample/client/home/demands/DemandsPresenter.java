package cz.poptavka.sample.client.home.demands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.annotation.Presenter;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.domain.demand.Category;
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
        void displayDemandsList(List<Demand> result);

        void displayDemandsSet(Set<Demand> result);

        Widget getWidgetView();

        ListBox getCategoryList();

        ListBox getLocalityList();
    }

    public interface DemandsPagerInterface {
        void displayDemandsList(List<Demand> result);

        void displayDemandsSet(Set<Demand> result);

        void display(FlexTable flexTable);

        Widget getWidgetView();
    }

    @Inject
    private DemandRPCServiceAsync demandService;
    @Inject
    private CategoryRPCServiceAsync categoryService;
    @Inject
    private LocalityRPCServiceAsync localityService;

    /**
     * Bind objects and theirs action handlers.
     */
    public void bind() {
        view.getCategoryList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                LOGGER.info("OnCategoryListChange");
                eventBus.filterByCategory(view.getCategoryList().getValue(
                        view.getCategoryList().getSelectedIndex()));
            }
        });
        view.getLocalityList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {

            }
        });
    }

    /**
     * Try retrieve and display all demands.
     * Get all categories and localities to display in listBoxes for later filtering.
     *
     */
    public void onStart() {
        LOGGER.info("Starting demands presenter...");
        LOGGER.info("Getting categories...");
        eventBus.getCategories();
        LOGGER.info("Getting localities...");
        eventBus.getLocalities();
        LOGGER.info("Displaying demands...");
        eventBus.displayDemands();
        eventBus.setHomeWidget(AnchorEnum.FIRST, view.getWidgetView(), true);
    }

    /**
     * Get all localities. Used for display in listBox localities.
     */
    public void onGetLocalities() {
        localityService.getLocalities(LocalityType.REGION, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.info("onFailureGetLocalities - regions");

            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.setLocalityData(view.getLocalityList(), result);
            }
        });

    }

    /**
     * Get all categories. Used for display in listBox categories.
     */
    public void onGetCategories() {
        categoryService
                .getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {

                    @Override
                    public void onFailure(Throwable arg0) {
                        LOGGER.info("onFailureCategory");
                    }

                    @Override
                    public void onSuccess(ArrayList<CategoryDetail> list) {
                        eventBus.setCategoryData(view.getCategoryList(), list);
                    }
                });
    }

    /**
     * Get category by its code.
     * @param code - String representing code
     */
    public void onFilterByCategory(String code) {
        LOGGER.info("FilterByCategory: " + code);
        categoryService.getCategory(code, new AsyncCallback<Category>() {

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.info("onFailureFilterbyCategory");

            }

            @Override
            public void onSuccess(Category result) {
                LOGGER.info("category found: " + result);
                if (result != null) {
                    eventBus.displayDemandsByCategory(result);
                }
            }
        });
        LOGGER.info("End of method filter by category");
    }

    public void onFilterByLocality(String code) { }

    /**
     * Get demands by given category and its child subCategories.
     * @param category - given category
     */
    public void onDisplayDemandsByCategory(Category category) {
        LOGGER.info("Display demand by category: " + category.getName());
        Category[] categories = (Category[]) category.getChildren().toArray();
        demandService.getDemands(categories, new AsyncCallback<Set<Demand>>() {

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.info("onFailureDisplayDemandsByCategory");
            }

            @Override
            public void onSuccess(Set<Demand> result) {
                view.displayDemandsSet(result);
            }
        });
    }

    /**
     * Fills category listBox with given list of localities.
     * @param box - listBox to be filled
     * @param list - data (categories)
     */
    public void onSetCategoryData(final ListBox box,
            final ArrayList<CategoryDetail> list) {
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
    public void onSetLocalityData(final ListBox box,
            final ArrayList<LocalityDetail> list) {
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

    /**
     * Get all demand from database.
     *
     * @param result
     */
    public void onDisplayDemands() {
        demandService.getAllDemands(new AsyncCallback<List<Demand>>() {

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.info("onFailureDisplayDemands");
            }

            @Override
            public void onSuccess(List<Demand> result) {
                LOGGER.info("onSuccessDisplayDemands");
                view.displayDemandsList(result);
            }
        });
    }
}
