package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.CreatedDateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * @author Martin Slavkovsky
 */
public class CreatedDateColumn extends Column<CreatedDateColumn.TableDisplayCreatedDate, Date> {

    public interface TableDisplayCreatedDate {

        Date getCreated();
    }

    public CreatedDateColumn(FieldUpdater fieldUpdater) {
        super(new CreatedDateCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public Date getValue(TableDisplayCreatedDate object) {
        return object.getCreated();
    }
}
