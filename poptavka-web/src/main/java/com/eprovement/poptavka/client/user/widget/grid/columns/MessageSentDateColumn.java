/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.MessageSentDateColumn.TableDisplayMessageSentDate;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * Use to create <b>message sent date column</b> in table.
 * Object must implemnets <b>TableDisplayMessageSentDate</b> to be displayable in table with message send date column.
 *
 * @author Martin Slavkovsky
 */
public class MessageSentDateColumn extends Column<TableDisplayMessageSentDate, String> {

    public interface TableDisplayMessageSentDate {

        Date getMessageSentDate();
    }

    /**
     * Creates MessageSentDateColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public MessageSentDateColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getValue(TableDisplayMessageSentDate object) {
        return Storage.get().getDateTimeFormat().format(object.getMessageSentDate());
    }
}
