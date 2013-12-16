/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.MessageTextColumn.TableDisplayMessageText;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Martin Slavkovsky
 */
public class MessageTextColumn extends Column<TableDisplayMessageText, String> {

    public interface TableDisplayMessageText {

        String getMessageText();
    }

    public MessageTextColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(false);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public String getValue(TableDisplayMessageText object) {
        return object.getMessageText();
    }
}
