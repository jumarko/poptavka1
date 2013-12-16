/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Martin Slavkovsky
 */
public class DemandTitleColumn extends Column<TableDisplayDemandTitle, String> {

    public interface TableDisplayDemandTitle {

        String getDemandTitle();

        /**
         * Provide 0 if count unavailable or if only demand title alone needed.
         */
        int getUnreadMessagesCount();
    }

    public DemandTitleColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(true);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public String getValue(TableDisplayDemandTitle object) {
        return getCellTextAccordingToMessageCount(object.getUnreadMessagesCount(), object.getDemandTitle());
    }

    private String getCellTextAccordingToMessageCount(int messageCount, String cellText) {
        if (messageCount > 0) {
            StringBuilder title = new StringBuilder();
            title.append(cellText);
            title.append(" (");
            title.append(messageCount);
            title.append(")");
            return title.toString();
        } else {
            return cellText;
        }
    }
}
