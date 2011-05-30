package cz.poptavka.sample.client.home.demands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.demands.demand.DemandView;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

/**
 *
 * @author Martin Slavkovsky
 *
 */
@Presenter(view = DemandsView.class)
public class DemandsPresenter extends BasePresenter<DemandsPresenter.DemandsViewInterface, DemandsEventBus> {

    private static final Logger LOGGER = Logger.getLogger(DemandsPresenter.class.getName());

    public interface DemandsViewInterface {

        Widget getWidgetView();

        ListBox getCategoryList();

        ListBox getLocalityList();

        ListDataProvider<DemandDetail> getDataProvider();

        CellTable<DemandDetail> getCellTable();

        SimplePager getPager();

        Label getDemandDetailLabel();

        DemandView getDemandView();
    }

    /**
     * Bind objects and theirs action handlers.
     */
    @Override
    public void bind() {
        view.getCategoryList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                LOGGER.info("OnCategoryListChange");
//                eventBus.getCategory(Long.parseLong(view.getCategoryList().getValue(
//                        view.getCategoryList().getSelectedIndex())));
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
        // Add a selection model to handle user selection.
        final SingleSelectionModel<DemandDetail> selectionModel = new SingleSelectionModel<DemandDetail>();
        view.getCellTable().setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                DemandDetail selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    eventBus.getDemand(selected);
                }
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

        eventBus.getCategories();

        eventBus.getLocalities();

        ResultCriteria criteria = ResultCriteria.EMPTY_CRITERIA;
        criteria = new ResultCriteria.Builder()
                .firstResult(0)
                .maxResults(10).build();
        eventBus.getDemands(criteria);

        eventBus.setBodyWidget(view.getWidgetView());
    }

    /**
     * Fills category listBox with given list of localities.
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

    public void onDisplayDemands(Collection<DemandDetail> result) {
        Object[] array = result.toArray();

        List<DemandDetail> list = view.getDataProvider().getList();

        for (int i = 0; i < array.length; i++) {
            list.add((DemandDetail) array[i]);
        }

        view.getDataProvider().refresh();
    }

    public void onSetDemand(Demand demand) {
        view.getDemandDetailLabel().setVisible(true);
        view.getDemandView().setVisible(true);
        view.getDemandView().setDemand(demand);
    }

    public ResultCriteria getResultsCriteria() {
        ResultCriteria criteria = ResultCriteria.EMPTY_CRITERIA;

        criteria = new ResultCriteria.Builder()
                .firstResult(view.getPager().getPage() * view.getPager().getPageCount())
                .maxResults(view.getPager().getPageCount())
                .build();

        return criteria;
    }
}
