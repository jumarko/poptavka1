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
    }

    /**
     * Note - used in ClientDemand and SupplierDemands modules in all widgets
     * except parent tables in ClientDemands and ClientOffers.
     * Messages count and read flag is available.
     * If MessagesCount > 0, its value is displayed and if isRead value == false, row is consider as unread.
     */
    public interface TableDisplayDemandTitleMessages extends TableDisplayDemandTitle {

        int getMessagesCount();

        boolean isRead();
    }

    /**
     * Note - used in ClientDemands and ClientOffers use cases in parent tables.
     * Unread messages count is available.
     * If UnreadMessagesCount > 0, its value is displayed and row is consider as unread.
     */
    public interface TableDisplayDemandTitleUnreadMessages extends TableDisplayDemandTitle {

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
        if (object instanceof TableDisplayDemandTitleMessages) {
            return getCellTextAccordingToMessagesCount(
                ((TableDisplayDemandTitleMessages) object).getMessagesCount(), object.getDemandTitle());
        } else if (object instanceof TableDisplayDemandTitleUnreadMessages) {
            return getCellTextAccordingToMessagesCount(
                ((TableDisplayDemandTitleUnreadMessages) object).getUnreadMessagesCount(), object.getDemandTitle());
        } else {
            return getCellTextAccordingToMessagesCount(0, object.getDemandTitle());
        }
    }

    /**
     * Formats demand title text by adding messages count after it if messages count is not 0.
     *
     * @param messagesCount value
     * @param cellText - demand title text
     * @return formated cell value
     */
    private String getCellTextAccordingToMessagesCount(int messagesCount, String cellText) {
        if (messagesCount > 0) {
            StringBuilder title = new StringBuilder();
            title.append(cellText);
            title.append(" (");
            title.append(messagesCount);
            title.append(")");
            return title.toString();
        } else {
            return cellText;
        }
    }
}
