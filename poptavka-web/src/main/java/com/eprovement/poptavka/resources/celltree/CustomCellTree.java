/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.celltree;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;

/**
 * Defines CellTree styles for CatLocSelector module - TreeBrowser widget.
 *
 * @author Jaro
 */
public interface CustomCellTree extends CellTree.Resources {
    @Override
    @Source("cellTreeClosedItem.png")
    ImageResource cellTreeClosedItem();

    @Override
    @Source("cellTreeOpenItem.png")
    ImageResource cellTreeOpenItem();

    @Override
    @Source("customCellTree.css")
    CellTree.Style cellTreeStyle();
}
