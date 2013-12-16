package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import java.util.ArrayList;

/**
 * @author Martin Slavkovsky
 */
public class AddressColumn extends Column<AddressColumn.TableDisplayAddress, String> {

    public interface TableDisplayAddress {

        ArrayList<AddressDetail> getAddresses();
    }

    public AddressColumn(FieldUpdater fieldUpdater) {
        super(new TextCell());
        setSortable(false);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

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
