package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;

/**
 * Custom stylesheet for our cellTables/Datagrids.
 *
 * @author beho
 */
public interface GridTableResources extends CssResource {

    @ClassName("cell-table-logo-column")
    String cellTableLogoColumn();

    @ClassName("cell-table-icon-column")
    String cellTableIconColumn();

    @ClassName("cell-table-hand-cursor")
    String cellTableHandCursor();

    @ClassName("unread")
    String unread();
}
