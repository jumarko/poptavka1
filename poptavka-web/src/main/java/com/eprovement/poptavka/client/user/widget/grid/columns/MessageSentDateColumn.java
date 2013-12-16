/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.MessageSentDateColumn.TableDisplayMessageSentDate;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * @author Martin Slavkovsky
 */
public class MessageSentDateColumn extends Column<TableDisplayMessageSentDate, String> {

    public interface TableDisplayMessageSentDate {

        Date getMessageSentDate();
    }

    public MessageSentDateColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public String getValue(TableDisplayMessageSentDate object) {
        return Storage.get().getDateTimeFormat().format(object.getMessageSentDate());
    }
}
