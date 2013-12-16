package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.DisplayNameColumn.TableDisplayDisplayName;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 *
 * @author Martin Slavkovsky
 */
public class DisplayNameColumn extends Column<TableDisplayDisplayName, String> {

    public interface TableDisplayDisplayName {

        String getDisplayName();

        /**
         * Provide 0 if count unavailable or if only display name alone needed.
         */
        int getUnreadMessagesCount();
    }

    public DisplayNameColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(false);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public String getValue(TableDisplayDisplayName object) {
        return getCellTextAccordingToMessageCount(object.getUnreadMessagesCount(), object.getDisplayName());
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
