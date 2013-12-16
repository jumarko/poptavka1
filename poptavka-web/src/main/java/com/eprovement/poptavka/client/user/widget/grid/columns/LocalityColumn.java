/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.LocalityColumn.TableDisplayLocality;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.ArrayList;

/**
 * Use to create <b>locality column</b> in table.
 * Object must implemnets <b>TableDisplayLocality</b> to be displayable in table with locality column.
 *
 * @author Martin Slavkovsky
 */
public class LocalityColumn extends Column<TableDisplayLocality, String> {

    public interface TableDisplayLocality {

        ArrayList<ICatLocDetail> getLocalities();
    }

    /**
     * Creates LocalityColumn with:
     * <ul>
     *   <li>sortable: false</li>
     *   <li>cellStyleNames: ellipsis</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public LocalityColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(false);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * Formats localities to more readable string.
     * @param object implementing TableDisplayLocality
     * @return formated localities string
     */
    @Override
    public String getValue(TableDisplayLocality object) {
        StringBuilder str = new StringBuilder();
        for (ICatLocDetail loc : ((TableDisplayLocality) object).getLocalities()) {
            str.append(loc.getName());
            str.append(",\n");
        }
        if (str.length() > 0) {
            str.delete(str.length() - 2, str.length());
        }
        return str.toString();
    }
}
