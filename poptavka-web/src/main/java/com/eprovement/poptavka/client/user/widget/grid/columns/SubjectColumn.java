package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.columns.SubjectColumn.TableDisplaySubject;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Martin Slavkovsky
 */
public class SubjectColumn extends Column<TableDisplaySubject, String> {

    public interface TableDisplaySubject {

        String getSubject();
    }

    public SubjectColumn(FieldUpdater fieldUpdater) {
        super(new ClickableTextCell());
        setSortable(true);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public String getValue(TableDisplaySubject object) {
        return object.getSubject();
    }
}
