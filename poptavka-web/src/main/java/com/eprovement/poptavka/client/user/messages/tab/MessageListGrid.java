package com.eprovement.poptavka.client.user.messages.tab;

import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

public class MessageListGrid<T> extends DataGrid<T> {

    private ListDataProvider<T> dataProvider = new ListDataProvider<T>();
    private ListHandler<T> sortHandler = new ListHandler<T>(dataProvider.getList());


    public MessageListGrid() {
        super();
        //insert widget to be displayed, where no relevant data are available
        this.setEmptyTableWidget(new HTML("<div style=\"text-align: center;\">No relevant data</div>"));
    }

    public MessageListGrid(ProvidesKey<T> keyProvider) {
        super(keyProvider);
        //insert widget to be displayed, where no relevant data are available
        this.setEmptyTableWidget(new HTML("<div style=\"text-align: center;\">No relevant data</div>"));
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

}
