/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.UrgentImageCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn.TableDisplayValidTo;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * Use to create <b>valid to column</b> in table.
 * Object must implemnets <b>TableDisplayValidTo</b> to be displayable in table with valid to column.
 *
 * @author Martin Slavkovsky
 */
public class UrgencyColumn extends Column<TableDisplayValidTo, Date> {

    private static final DataGridResources GRSCS = GWT.create(DataGridResources.class);

    public interface TableDisplayValidTo {

        Date getValidTo();
    }

    /**
     * Creates ValidToColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: dataGridStyle.cellStyleUrgency</li>
     *   <li>fieldUpdater: none</li>
     * </ul>
     * @param fieldUpdater
     */
    public UrgencyColumn() {
        super(new UrgentImageCell());
        setCellStyleNames(GRSCS.dataGridStyle().cellStyleUrgency());
        setSortable(true);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Date getValue(TableDisplayValidTo object) {
        return object.getValidTo();
    }
}
