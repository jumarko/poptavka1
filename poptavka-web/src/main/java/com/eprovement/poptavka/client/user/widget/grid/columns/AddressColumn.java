/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import java.util.ArrayList;

/**
 * Use to create <b>address column</b> in table.
 * Object must implemnets <b>TableDisplayAddress</b> to be displayable in table with address column.
 *
 * @author Martin Slavkovsky
 */
public class AddressColumn extends Column<AddressColumn.TableDisplayAddress, String> {

    public interface TableDisplayAddress {

        ArrayList<AddressDetail> getAddresses();
    }

    /**
     * Creates AddressColumn with:
     * <ul>
     *   <li>sortable: false</li>
     *   <li>cellStyleNames: ellipsis</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public AddressColumn(FieldUpdater fieldUpdater) {
        super(new TextCell());
        setSortable(false);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * Formats addresses to more readable string.
     * @param object implementing TableDisplayAddresses
     * @return formated addresses string
     */
    @Override
    public String getValue(TableDisplayAddress object) {
        StringBuilder str = new StringBuilder();
        for (AddressDetail addr : object.getAddresses()) {
            str.append(addr.getCity());
            str.append(", ");
        }
        if (!str.toString().isEmpty()) {
            str.delete(str.length() - 2, str.length());
        }
        return str.toString();
    }
}
