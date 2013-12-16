package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.UrgentImageCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn.TableDisplayValidTo;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * @author Martin Slavkovsky
 */
public class UrgencyColumn extends Column<TableDisplayValidTo, Date> {

    private static final DataGridResources GRSCS = GWT.create(DataGridResources.class);

    public interface TableDisplayValidTo {

        Date getValidTo();
    }

    public UrgencyColumn() {
        super(new UrgentImageCell());
        setCellStyleNames(GRSCS.dataGridStyle().cellStyleUrgency());
        setSortable(true);
    }

    @Override
    public Date getValue(TableDisplayValidTo object) {
        return object.getValidTo();
    }
}
