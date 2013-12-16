/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.DemandStatusImageCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandStatusColumn.TableDisplayDemandStatus;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Martin Slavkovsky
 */
public class DemandStatusColumn extends Column<TableDisplayDemandStatus, DemandStatus> {

    public interface TableDisplayDemandStatus {

        DemandStatus getDemandStatus();
    }

    public DemandStatusColumn() {
        super(new DemandStatusImageCell());
        setSortable(true);
    }

    @Override
    public DemandStatus getValue(TableDisplayDemandStatus object) {
        return object.getDemandStatus();
    }
}
