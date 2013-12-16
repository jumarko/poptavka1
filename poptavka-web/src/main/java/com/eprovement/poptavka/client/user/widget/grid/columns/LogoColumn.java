/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.columns.LogoColumn.TableDisplayLogo;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>logo column</b> in table.
 * Object must implemnets <b>TableDisplayLogo</b> to be displayable in table with logo column.
 *
 * @author Martin Slavkovsky
 */
public class LogoColumn extends Column<TableDisplayLogo, ImageResource> {

    public interface TableDisplayLogo {
        // TODO LATER Martin implement logos
    }

    /**
     * Creates LogoColumn with:
     * <ul>
     *   <li>sortable: false</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: none</li>
     * </ul>
     * @param fieldUpdater
     */
    public LogoColumn() {
        super(new ImageResourceCell());
        setSortable(false);
        setCellStyleNames("logo");
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public ImageResource getValue(TableDisplayLogo object) {
        return StyleResource.INSTANCE.images().contactImage();
    }
}
