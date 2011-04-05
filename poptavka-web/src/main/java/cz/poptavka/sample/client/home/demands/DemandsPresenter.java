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
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

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
//    @Inject
//    private CategoryRPCServiceAsync localityService;

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
     * Try retrieve demand from server and display them on Success.
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

    public void onGetLocalities() {

    }

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

    public void onFilterByCategory(String code) {
        categoryService.getCategory(code, new AsyncCallback<Category>() {

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.info("onFailureFilterbyCategory");

            }

            @Override
            public void onSuccess(Category result) {
                if (result != null) {
                    LOGGER.info("category found: ");
                    eventBus.displayDemandsByCategory(result);
                }
            }
        });
    }

    public void onDisplayDemandsByCategory(Category category) {
        Category[] categories = {category};
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
     * Call DemandsView to display given list of demands.
     *
     * @param result
     */
    public void onDisplayDemands() {
        //Nefunguje zatial
//        demandService.getAllDemands(new AsyncCallback<List<Demand>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                LOGGER.info("onFailureDisplayDemands");
//            }
//
//            @Override
//            public void onSuccess(List<Demand> result) {
//                LOGGER.info("List: " + result);
//                view.displayDemandsList(result);
//            }
//        });

        view.displayDemandsList(this.getDemands());
    }

    private ArrayList<Demand> getDemands() {
        ArrayList<Demand> demands = new ArrayList<Demand>();

        Demand d1 = new Demand();
        d1.setTitle("demand 1");
        d1.setDescription("poptavka d1");
        demands.add(d1);

        Demand d2 = new Demand();
        d2.setTitle("demand 2");
        d2.setDescription("poptavka d1");
        demands.add(d2);

        Demand d3 = new Demand();
        d3.setTitle("demand 3");
        d3.setDescription("poptavka d1");
        demands.add(d3);

        Demand d4 = new Demand();
        d4.setTitle("demand 4");
        d4.setDescription("poptavka d1");
        demands.add(d4);

        Demand d5 = new Demand();
        d5.setTitle("demand 5");
        d5.setDescription("poptavka d1");
        demands.add(d5);

        Demand d6 = new Demand();
        d6.setTitle("demand 6");
        d6.setDescription("poptavka d1");
        demands.add(d6);

        Demand d7 = new Demand();
        d7.setTitle("demand 7");
        d7.setDescription("poptavka d1");
        demands.add(d7);

        Demand d8 = new Demand();
        d8.setTitle("demand 8");
        d8.setDescription("poptavka d1");
        demands.add(d8);

        Demand d9 = new Demand();
        d9.setTitle("demand 9");
        d9.setDescription("poptavka d1");
        demands.add(d9);

        Demand d10 = new Demand();
        d10.setTitle("demand 01");
        d10.setDescription("poptavka d1");
        demands.add(d10);

        Demand d11 = new Demand();
        d11.setTitle("demand 11");
        d11.setDescription("poptavka d1");
        demands.add(d11);

        Demand d12 = new Demand();
        d12.setTitle("demand 12");
        d12.setDescription("poptavka d1");
        demands.add(d12);

        Demand d13 = new Demand();
        d13.setTitle("demand 13");
        d13.setDescription("poptavka d1");
        demands.add(d13);

        Demand d14 = new Demand();
        d14.setTitle("demand 14");
        d14.setDescription("poptavka d1");
        demands.add(d14);

        Demand d15 = new Demand();
        d15.setTitle("demand 15");
        d15.setDescription("poptavka d1");
        demands.add(d15);

        Demand d16 = new Demand();
        d16.setTitle("demand 16");
        d16.setDescription("poptavka d1");
        demands.add(d16);

        Demand d17 = new Demand();
        d17.setTitle("demand 17");
        d17.setDescription("poptavka d1");
        demands.add(d17);

        Demand d18 = new Demand();
        d18.setTitle("demand 18");
        d18.setDescription("poptavka d1");
        demands.add(d18);

        Demand d19 = new Demand();
        d19.setTitle("demand 19");
        d19.setDescription("poptavka d1");
        demands.add(d19);

        Demand d20 = new Demand();
        d20.setTitle("demand 20");
        d20.setDescription("poptavka d1");
        demands.add(d20);

        Demand d21 = new Demand();
        d21.setTitle("demand 21");
        d21.setDescription("poptavka d1");
        demands.add(d21);

        return demands;
    }
}
