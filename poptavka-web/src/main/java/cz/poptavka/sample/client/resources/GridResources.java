package cz.poptavka.sample.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 * Custom stylesheet for our cellTables/Datagrids.
 *
 * @author beho
 */
//public interface GridResources extends CellTable.Resources {

public interface GridResources extends CssResource {

//    @Override
//    @Source({ CellTable.Style.DEFAULT_CSS, "GridTable.css" })
//    GridStyle cellTableStyle();
//
//    public interface GridStyle extends CellTable.Style {

    @ClassName("cell-table-hand-cursor")
    String cellTableHandCursor();
//    }

}
