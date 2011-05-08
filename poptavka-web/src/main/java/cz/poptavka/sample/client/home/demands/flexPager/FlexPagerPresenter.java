package cz.poptavka.sample.client.home.demands.flexPager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.demands.DemandsEventBus;
import cz.poptavka.sample.client.home.demands.demand.DemandView;
import cz.poptavka.sample.domain.ResultCriteria;
import cz.poptavka.sample.domain.demand.Demand;

@Presenter(view = FlexPagerView.class)
public class FlexPagerPresenter extends
    BasePresenter<FlexPagerPresenter.FlexPagerViewInterface, DemandsEventBus> {

    private static final Logger LOGGER = Logger.getLogger(FlexPagerPresenter.class
            .getName());

    public interface FlexPagerViewInterface {

        HasClickHandlers getLessBtn();

        HasClickHandlers getBeginBtn();

        HasClickHandlers getEndBtn();

        HasClickHandlers getMoreBtn();

        ListBox getPageSize();

        FlexTable getFlexTable();

        Label getPagerLabel();

        Widget getWidgetView();
    }

    public void bind() {
        LOGGER.info("BIND FlexPager");
        view.getLessBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.lessClicked();
            }
        });
        view.getMoreBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.moreClicked();
            }
        });
        view.getBeginBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.beginClicked();
            }
        });
        view.getEndBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.endClicked();
            }
        });
        view.getPageSize().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.changePagerSize();
            }
        });
        view.getFlexTable().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.setDemand(event);
            }
        });
    }

    private int page = 0;
    private int maxCount;
    private List<Demand> demands;

    public void onBeginClicked() {
        page = 0;
        eventBus.setRows();
    }

    public void onLessClicked() {
        if (page != 0) {
            page -= 1;
            eventBus.setRows();
        }
    }

    public void onClearFlexTable() {
        view.getFlexTable();
    }

    public void onEndClicked() {
        page = (int) maxCount / getPageSize();
        if ((maxCount % getPageSize()) == 0) {
            page -= 1;
        }
        eventBus.setRows();
    }

    public void onMoreClicked() {
        if (this.getUpperValue() < maxCount) {
            page += 1;
            eventBus.setRows();
        }
    }

    // HELP Methods
    private int getBottomValue() {
        return page * getPageSize() + 1;
    }

    private int getUpperValue() {
        return (page + 1) * getPageSize();
    }

    private int getPageSize() {
        return Integer.parseInt(view.getPageSize().getItemText(
                view.getPageSize().getSelectedIndex()));
    }

    private int getDemandIndex(int clickedRow) {
        int index = clickedRow / this.getPageSize();
        return (index * this.getPageSize()) + (clickedRow % this.getPageSize());
    }

    public void onSetPagerLabel() {
        int upperValue = this.getUpperValue();
        if (upperValue > maxCount) {
            upperValue = maxCount;
        }
        view.getPagerLabel().setText(this.getBottomValue() + " - " + upperValue + " z " + maxCount);
    }

    public void onChangePagerSize() {
        LOGGER.info("Page Size Changed");
        eventBus.setRows();
    }

    public void onSetDemand(ClickEvent e) {
        LOGGER.info("FLEXTABLE CLICKED");
        Cell clickedCell = view.getFlexTable().getCellForEvent(e);
        DemandView dem = new DemandView();
        LOGGER.info(Integer.toString(this.getDemandIndex(clickedCell
                .getRowIndex())));

        DisclosurePanel panel = (DisclosurePanel) view.getFlexTable().getWidget(
                clickedCell.getRowIndex(), 0);
        Demand demand = new Demand();
        demand.setId(100L);
        demand.setDescription(this.demands.get(clickedCell.getRowIndex()).getDescription());
        demand.setPrice(new BigDecimal(10000));
        dem.setDemand(demand);

        panel.setContent(dem);
    }

    public void onSetRows() {
        eventBus.clearFlexTable();
        LOGGER.info("setRows");
        int bottomValue = this.getBottomValue();
        LOGGER.info("bottomValue: " + bottomValue);
        int upperValue = this.getUpperValue();
        LOGGER.info("upperValue: " + upperValue);

        if (upperValue > maxCount) {
            upperValue = maxCount;
        }

        for (int i = bottomValue; i <= upperValue; i++) {
            LOGGER.info("demand: " + (i - 1));
            view.getFlexTable().setWidget(i - (page * getPageSize()), 0,
                    new DisclosurePanel(demands.get(i - 1).getTitle()));
        }
        eventBus.setPagerLabel();
    }

    public void onDisplayDemands(Collection<Demand> demands) {
        this.demands = new ArrayList<Demand>(demands);
        LOGGER.info("displaying " + demands.size());
        maxCount = demands.size();
        eventBus.setRows();
    }

    public void onGetResultsCriteria(ResultCriteria resultCriteria) {
        //List<String> columnToOrder = new ArrayList<String>();

        ResultCriteria criteria = new ResultCriteria.Builder()
            .maxResults(this.getPageSize())
            .firstResult(this.getBottomValue())
            .build();

        resultCriteria = criteria;
    }
}
