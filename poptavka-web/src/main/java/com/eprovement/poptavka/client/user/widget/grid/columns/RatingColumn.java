package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.RatingCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn.TableDisplayRating;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 *
 * @author Mato
 */
public class RatingColumn extends Column<TableDisplayRating, Integer> {

    public interface TableDisplayRating {

        Integer getOveralRating();
    }

    public RatingColumn(FieldUpdater fieldUpdater) {
        super(new RatingCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public Integer getValue(TableDisplayRating object) {
        return object.getOveralRating();
    }
}
