package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;

public interface TreeResources extends CellTree.Resources {
    @Source("images/cellTreeClosedItem.gif")
    ImageResource cellTreeClosedItem();

    @Source("images/cellTreeOpenItem.gif")
    ImageResource cellTreeOpenItem();

    @Source("MyCellTree.css")
    CellTree.Style cellTreeStyle();
}
