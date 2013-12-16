/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.cell.StarImageCell;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>user message column</b> in table.
 * Object must implemnets <b>TableDisplayUserMessage</b> to be displayable in table with user message column.
 *
 * @author Martin Slavkovsky
 */
public class StarColumn extends Column<TableDisplayUserMessage, Boolean> {

    private static final DataGridResources GRSCS = GWT.create(DataGridResources.class);

    /**
     * Creates StarColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: dataGridStyle.cellStyleStar</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public StarColumn(FieldUpdater fieldUpdater) {
        super(new StarImageCell());
        setSortable(true);
        setCellStyleNames(GRSCS.dataGridStyle().cellStyleStar());
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Boolean getValue(TableDisplayUserMessage object) {
        return object.isStarred();
    }
}
