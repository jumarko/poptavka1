package cz.poptavka.sample.client.user.demands.develmodule.s.list;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

public class SupplierList extends Composite implements SupplierListPresenter.IList {

    private static DemandListUiBinder uiBinder = GWT.create(DemandListUiBinder.class);
    interface DemandListUiBinder extends UiBinder<Widget, SupplierList> {
    }

    @UiField(provided = true)
    SupplierListGrid<PotentialDemandMessage> demandGrid;

    @UiField(provided = true)
    SimplePager pager;

    @Override
    public void createView() {
        //demandGrid init
        demandGrid = new SupplierListGrid<PotentialDemandMessage>(PotentialDemandMessage.KEY_PROVIDER);
        // Add a selection model so we can select cells.
        final SelectionModel<PotentialDemandMessage> selectionModel =
            new MultiSelectionModel<PotentialDemandMessage>(PotentialDemandMessage.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
            .<PotentialDemandMessage>createCheckboxManager());

        demandGrid.initTableColumns(selectionModel);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(demandGrid);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public SupplierListGrid<PotentialDemandMessage> getGrid() {
        return demandGrid;
    }

    @Override
    public ListDataProvider<PotentialDemandMessage> getDataProvider() {
        return demandGrid.getDataProvider();
    }

}
