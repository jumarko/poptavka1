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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.demands.demand.DemandView;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.demand.ClientDemandDetail;

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

        AsyncDataProvider<ClientDemandDetail> getDataProvider();

        void setDataProvider(AsyncDataProvider<ClientDemandDetail> dataProvider);

        CellTable<ClientDemandDetail> getCellTable();

        SimplePager getPager();

        Label getDemandDetailLabel();

        DemandView getDemandView();
    }
    private int start = 0;

    //TODO - Dorobit kombinaciu filtrovania podla categorii && lokality
    //TODO - ako ziskat pri pouziti filtrovania pocet filtrovanych zaznamov,
    //bez toho, aby som musel opat zistovat vsetky kategorie a ich podkategorie <- dlho trva

    /**
     * Bind objects and theirs action handlers.
     */
    @Override
    public void bind() {
        view.getCategoryList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                LOGGER.info("OnCategoryListChange");
                eventBus.getDemandsByCategories(0, 10, Long.valueOf(view.getCategoryList().getValue(
                        view.getCategoryList().getSelectedIndex())));
                //TODO - dat uzivatelovi vediet, ze nacitava - zmenit cursor abo take daco
            }
        });
        view.getLocalityList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                LOGGER.info("OnLocalityListChange: " + view.getLocalityList().getValue(
                        view.getLocalityList().getSelectedIndex()));
                eventBus.getDemandsByLocalities(0, 10, view.getLocalityList().getValue(
                        view.getLocalityList().getSelectedIndex()));
                //TODO - dat uzivatelovi vediet, ze nacitava - zmenit cursor abo take daco
            }
        });

//        dataProvider.addDataDisplay(view.getCellTable());
        // Add a selection model to handle user selection.
        final SingleSelectionModel<ClientDemandDetail> selectionModel = new SingleSelectionModel<ClientDemandDetail>();
        view.getCellTable().setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                ClientDemandDetail selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "progress");
                    //eventBus.getDemand(selected);
                    eventBus.setDemand(selected);
                }
            }
        });
    }
    private AsyncDataProvider dataProvider = new AsyncDataProvider<ClientDemandDetail>() {

        @Override
        protected void onRangeChanged(HasData<ClientDemandDetail> display) {
            //just for initializing cellTable
            //will be implemented later, when allDemandsCount value will be retrieved
        }
    };

    public void onCreateAsyncDataProvider(final long result) {
        this.dataProvider = new AsyncDataProvider<ClientDemandDetail>() {

            @Override
            protected void onRangeChanged(HasData<ClientDemandDetail> display) {
                display.setRowCount((int) result);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getDemands(start, start + length);
            }
        };
        this.dataProvider.addDataDisplay(view.getCellTable());
    }

    /**
     * Try retrieve and display all demands.
     * Get all categories and localities to display in listBoxes for later filtering.
     *
     */
    public void onAtDemands() {
        LOGGER.info("Starting demands presenter...");

        eventBus.getAllDemandsCount();

        eventBus.getCategories();

        eventBus.getLocalities();

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
                box.addItem("Localities...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(),
                            String.valueOf(list.get(i).getCode()));
                }
                box.setSelectedIndex(0);
                LOGGER.info("Locality List filled");
            }
        });
    }

    public void onDisplayDemands(Collection<ClientDemandDetail> result) {

        List<ClientDemandDetail> list = new ArrayList<ClientDemandDetail>(result);

        dataProvider.updateRowData(start, list);
    }

    public void onSetDemand(ClientDemandDetail demand) {
        view.getDemandDetailLabel().setVisible(true);
        view.getDemandView().setVisible(true);
        view.getDemandView().setDemand(demand);
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
    }
}