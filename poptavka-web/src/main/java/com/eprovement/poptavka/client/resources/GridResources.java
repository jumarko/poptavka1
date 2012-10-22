package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 * Custom stylesheet for our cellTables/Datagrids.
 *
 * @author beho
 */
public interface GridResources extends CssResource {

    @ClassName("cell-table-logo-column")
    String cellTableLogoColumn();

    @ClassName("cell-table-icon-column")
    String cellTableIconColumn();

    @ClassName("cell-table-hand-cursor")
    String cellTableHandCursor();
}
