package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.LocalityColumn.TableDisplayLocality;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.ArrayList;

/**
 * @author Martin Slavkovsky
 */
public class LocalityColumn extends Column<TableDisplayLocality, String> {

    public interface TableDisplayLocality {

        ArrayList<ICatLocDetail> getLocalities();
    }

    public LocalityColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(false);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

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
