/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>demand titel column</b> in table.
 * Object must implemnets <b>TableDisplayDemandTitle</b> to be displayable in table with demand title column.
 *
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

    /**
     * Creates DemandTitleColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: ellipsis</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public DemandTitleColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(true);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getValue(TableDisplayDemandTitle object) {
        return getCellTextAccordingToUnreadMessagesCount(object.getUnreadMessagesCount(), object.getDemandTitle());
    }

    /**
     * Formats demand title text by adding unread messages count abter it
     * if unread messages count is not 0.
     *
     * @param unreadMessageCount value
     * @param cellText - demand title text
     * @return formated cell value
     */
    private String getCellTextAccordingToUnreadMessagesCount(int unreadMessageCount, String cellText) {
        if (unreadMessageCount > 0) {
            StringBuilder title = new StringBuilder();
            title.append(cellText);
            title.append(" (");
            title.append(unreadMessageCount);
            title.append(")");
            return title.toString();
        } else {
            return cellText;
        }
    }
}
