package cz.poptavka.sample.client.home.demands;

import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.home.HomeEventBus;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

/**
 *
 * @author Martin Slavkovsky
 *
 */
@Presenter(view = DemandsView.class)
public class DemandsPresenter extends BasePresenter<DemandsPresenter.DemandsViewInterface, HomeEventBus> {

    private static final Logger LOGGER = Logger.getLogger(DemandsPresenter.class.getName());
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface DemandsViewInterface {

        void setDemand(FullDemandDetail demand);

        void setRegisterSupplierToken(String token);

        void setAttachmentToken(String token);

        void setLoginToken(String token);

        Widget getWidgetView();

        ListBox getCategoryList();

        ListBox getLocalityList();

        ListBox getCombo();

        AsyncDataProvider<DemandDetail> getDataProvider();

        void setDataProvider(AsyncDataProvider<DemandDetail> dataProvider);

        CellTable<DemandDetail> getCellTable();

        SimplePager getPager();

        Label getBannerLabel();

        HTMLPanel getDemandView();

        SingleSelectionModel<DemandDetail> getSelectionModel();
    }
    private int start = 0;
    private String resultSource = "";
    private long resultCount = 0;

    //TODO - Dorobit kombinaciu filtrovania podla categorii && lokality
    /**
     * Bind objects and theirs action handlers.
     */
    @Override
    public void bind() {
        view.getCategoryList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                eventBus.loadingShow(MSGS.loading());
                view.getDemandView().setVisible(false);
                view.getBannerLabel().setVisible(true);
                view.getLocalityList().setSelectedIndex(0);
                if (view.getCategoryList().getSelectedIndex() == 0) {
                    eventBus.getAllDemandsCount();
                } else {
                    eventBus.getDemandsCountCategory(Long.valueOf(view.getCategoryList().getValue(
                            view.getCategoryList().getSelectedIndex())));
                }
            }
        });
        view.getLocalityList().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                eventBus.loadingShow(MSGS.loading());
                view.getDemandView().setVisible(false);
                view.getBannerLabel().setVisible(true);
                view.getCategoryList().setSelectedIndex(0);
                if (view.getLocalityList().getSelectedIndex() == 0) {
                    eventBus.getAllDemandsCount();
                } else {
                    eventBus.getDemandsCountLocality(view.getLocalityList().getValue(
                            view.getLocalityList().getSelectedIndex()));
                }
            }
        });
        //TODO Martin - kombinacia filtrovanie locality a sucasne categories?

        // Add a selection model to handle user selection.
        view.getCellTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                DemandDetail selected = view.getSelectionModel().getSelectedObject();
                if (selected != null) {
                    view.getBannerLabel().setVisible(false);
                    view.getDemandView().setVisible(true);
                    eventBus.setDemand(selected);
                } else {
                    view.getDemandView().setVisible(false);
                    view.getBannerLabel().setVisible(true);
                }
            }
        });

        view.getCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                view.getCellTable().setRowCount(0, true);

                int newPage = Integer.valueOf(view.getCombo()
                        .getItemText(view.getCombo().getSelectedIndex()));

                view.getCellTable().setRowCount(newPage, true);

                int page = view.getPager().getPageStart() / view.getPager().getPageSize();

                view.getPager().setPageStart(page * newPage);
                view.getPager().setPageSize(newPage);
            }
        });

        view.setRegisterSupplierToken(getTokenGenerator().atRegisterSupplier());

//        view.setAttachmentToken(getTokenGenerator().atAttachement());

//        view.setLoginToken(getTokenGenerator().atLogin());
    }
    private AsyncDataProvider dataProvider = new AsyncDataProvider<FullDemandDetail>() {

        @Override
        protected void onRangeChanged(HasData<FullDemandDetail> display) {
            //just for initializing cellTable
            //will be implemented later, when allDemandsCount value will be retrieved
        }
    };

    public void onCreateAsyncDataProvider() {
        this.dataProvider = new AsyncDataProvider<FullDemandDetail>() {

            @Override
            protected void onRangeChanged(HasData<FullDemandDetail> display) {
                display.setRowCount((int) resultCount);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();

                if (resultSource.equals("all")) {
                    eventBus.getDemands(start, start + length);
                } else if (resultSource.equals("category")) {
                    eventBus.getDemandsByCategories(start, start + length,
                            Long.valueOf(view.getCategoryList().getValue(
                            view.getCategoryList().getSelectedIndex())));
                } else if (resultSource.equals("locality")) {
                    eventBus.getDemandsByLocalities(start, start + length,
                            view.getLocalityList().getValue(
                            view.getLocalityList().getSelectedIndex()));
                }
                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getCellTable());
    }

    public void onSetResultSource(String source) {
        this.resultSource = source;
    }

    public void onSetResultCount(long count) {
        this.resultCount = count;
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
                box.addItem("All categories...");
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

    public void onDisplayDemands(Collection<DemandDetail> result) {

        List<DemandDetail> list = new ArrayList<DemandDetail>(result);

        dataProvider.updateRowData(start, list);
    }

    public void onSetDemand(DemandDetail demand) {
        view.getDemandView().setVisible(true);
        view.setDemand((FullDemandDetail) demand);
    }
}
