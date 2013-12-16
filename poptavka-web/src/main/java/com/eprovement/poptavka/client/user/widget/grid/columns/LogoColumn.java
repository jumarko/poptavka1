package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.columns.LogoColumn.TableDisplayLogo;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Martin Slavkovsky
 */
public class LogoColumn extends Column<TableDisplayLogo, ImageResource> {

    public interface TableDisplayLogo {
        // TODO LATER Martin implement logos
    }

    public LogoColumn() {
        super(new ImageResourceCell());
        setSortable(false);
        setCellStyleNames("logo");
    }

    @Override
    public ImageResource getValue(TableDisplayLogo object) {
        return StyleResource.INSTANCE.images().contactImage();
    }
}
