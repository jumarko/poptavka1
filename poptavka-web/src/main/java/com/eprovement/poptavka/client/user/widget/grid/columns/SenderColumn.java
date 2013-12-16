package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.columns.SenderColumn.TableDisplaySender;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Martin Slavkovsky
 */
public class SenderColumn  extends Column<TableDisplaySender, String> {

    public interface TableDisplaySender {

        String getSender();
    }

    public SenderColumn(FieldUpdater fieldUpdater) {
        super(new ClickableTextCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public String getValue(TableDisplaySender object) {
        return object.getSender();
    }
}
