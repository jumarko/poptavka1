package cz.poptavka.sample.client.user.widget.grid;

import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import cz.poptavka.sample.client.main.Storage;

public class UniversalGrid<T> extends DataGrid<T> {

    private ListDataProvider<T> dataProvider = new ListDataProvider<T>();
    private ListHandler<T> sortHandler = new ListHandler<T>(dataProvider.getList());


    public UniversalGrid() {
        super();
    }

    public UniversalGrid(ProvidesKey<T> keyProvider) {
        super(keyProvider);
        this.addColumnSortHandler(getSortHandler());
        dataProvider.addDataDisplay(this);

    }

    public void setDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public ListHandler<T> getSortHandler() {
        return sortHandler;
    }

    public void displayEmptyTable() {
        this.setEmptyTableWidget(new HTML("<div style=\"text-align: center;\">"
                + Storage.MSGS.emptyTable() + "</div>"));
    }

}
