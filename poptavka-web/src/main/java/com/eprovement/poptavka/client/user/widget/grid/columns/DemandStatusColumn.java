/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.DemandStatusImageCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandStatusColumn.TableDisplayDemandStatus;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>demand status column</b> in table.
 * Object must implemnets <b>TableDisplayDemandStatus</b> to be displayable in table with demand status column.
 *
 * @author Martin Slavkovsky
 */
public class DemandStatusColumn extends Column<TableDisplayDemandStatus, DemandStatus> {

    public interface TableDisplayDemandStatus {

        DemandStatus getDemandStatus();
    }

    /**
     * Creates DemandStatusColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: none</li>
     * </ul>
     * @param fieldUpdater
     */
    public DemandStatusColumn() {
        super(new DemandStatusImageCell());
        setSortable(true);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public DemandStatus getValue(TableDisplayDemandStatus object) {
        return object.getDemandStatus();
    }
}
