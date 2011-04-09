package cz.poptavka.sample.client.home.demands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.domain.demand.Demand;
/**
 *
 * @author Martin Slavkovsky
 *
 */
public class MyPager extends Composite implements
        DemandsPresenter.DemandsPagerInterface {

    private static MyPagerUiBinder uiBinder = GWT.create(MyPagerUiBinder.class);
    private static final Logger LOGGER = Logger.getLogger(MyPager.class
            .getName());

    interface MyPagerUiBinder extends UiBinder<Widget, MyPager> {
    }

    private int page;
    private int maxCount;
    private List<Demand> demands;

    @UiField
    Button begin;
    @UiField
    Button less;
    @UiField
    Label pagerLabel;
    @UiField
    Button more;
    @UiField
    Button end;
    @UiField
    ListBox pagesize;

    @UiField
    FlexTable flexTable;

    public MyPager() {
        initWidget(uiBinder.createAndBindUi(this));
        pagesize.addItem("10");
        pagesize.addItem("15");
        pagesize.addItem("20");
        pagesize.addItem("25");
        pagesize.addItem("30");
        pagesize.setSelectedIndex(2);
        page = 0;

    }

    /**
     * Returns this view instance.
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**
     * Set FlexTable to display
     */
    @Override
    public void display(FlexTable flexTable) {
        this.flexTable = flexTable;
    }

    public void setRows() {
        int a = this.getA();
        int b = this.getB();

        if (b > maxCount) {
            b = maxCount;
        }
        for (int i = a; i <= b; i++) {
            flexTable.setWidget(i - (page * getPageSize()), 0,
                    new DisclosurePanel(demands.get(i - 1).getTitle()));
        }
        this.setPagerLabel();
    }

    // HELP Methods
    private int getA() {
        return page * getPageSize() + 1;
    }

    private int getB() {
        return (page + 1) * getPageSize();
    }

    private int getPageSize() {
        return Integer.parseInt(pagesize.getItemText(pagesize
                .getSelectedIndex()));
    }

    // ACTIONS
    @UiHandler("less")
    void handleClickLess(ClickEvent e) {
        if (page != 0) {
            page -= 1;
            flexTable.clear();
            this.setRows();
        }
    }

    @UiHandler("more")
    void handleClickMore(ClickEvent e) {
        if (this.getB() < maxCount) {
            page += 1;
            flexTable.clear();
            this.setRows();
        }
    }

    @UiHandler("begin")
    void handleClickBegin(ClickEvent e) {
        page = 0;
        flexTable.clear();
        this.setRows();
    }

    @UiHandler("end")
    void handleClickEnd(ClickEvent e) {
        page = (int) maxCount / getPageSize();
        if ((maxCount % getPageSize()) == 0) {
            page -= 1;
        }

        flexTable.clear();
        this.setRows();
    }

    @UiHandler("pagesize")
    void pageSizeChanged(ClickEvent e) {
        this.handleClickBegin(e);
    }

    private void setPagerLabel() {
        int b = this.getB();
        if (b > maxCount) {
            b = maxCount;
        }
        pagerLabel.setText(this.getA() + " - " + b + " of " + maxCount);
    }

    @Override
    public void displayDemandsList(List<Demand> demands) {
        this.demands = demands;
        LOGGER.info(demands.toString());
        maxCount = demands.size();
        this.setRows();
    }

    @Override
    public void displayDemandsSet(Set<Demand> demands) {
        this.demands = new ArrayList<Demand>(demands);
        LOGGER.info(demands.toString());
        maxCount = demands.size();
        this.setRows();
    }

    @UiHandler("flexTable")
    void tableClicked(ClickEvent e) {
        LOGGER.info("FLEXTABLE CLICKED");
        Cell clickedCell = flexTable.getCellForEvent(e);
        DemandView dem = new DemandView();
        LOGGER.info(Integer.toString(this.getDemandIndex(clickedCell
                .getRowIndex())));

        DisclosurePanel panel = (DisclosurePanel) flexTable.getWidget(
                clickedCell.getRowIndex(), 0);
        Demand demand = new Demand();
        demand.setId(100L);
        demand.setDescription(this.demands.get(clickedCell.getRowIndex()).getDescription());
        demand.setPrice(new BigDecimal(10000));
        dem.setDemand(demand);

        panel.setContent(dem);
    }

    private int getDemandIndex(int clickedRow) {
        int index = clickedRow / this.getPageSize();
        return (index * this.getPageSize()) + (clickedRow % this.getPageSize());
    }

}
