package com.eprovement.poptavka.resources.celltree;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;

public interface CustomCellTree extends CellTree.Resources {
    @Source("cellTreeClosedItem.gif")
    ImageResource cellTreeClosedItem();

    @Source("cellTreeOpenItem.gif")
    ImageResource cellTreeOpenItem();

    @Source("customCellTree.css")
    CellTree.Style cellTreeStyle();
}
