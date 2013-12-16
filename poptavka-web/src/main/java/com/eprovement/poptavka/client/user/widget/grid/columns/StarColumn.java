package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.cell.StarImageCell;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Martin Slavkovsky
 */
public class StarColumn extends Column<TableDisplayUserMessage, Boolean> {

    private static final DataGridResources GRSCS = GWT.create(DataGridResources.class);

    public StarColumn(FieldUpdater fieldUpdater) {
        super(new StarImageCell());
        setSortable(true);
        setCellStyleNames(GRSCS.dataGridStyle().cellStyleStar());
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public Boolean getValue(TableDisplayUserMessage object) {
        return object.isStarred();
    }
}
