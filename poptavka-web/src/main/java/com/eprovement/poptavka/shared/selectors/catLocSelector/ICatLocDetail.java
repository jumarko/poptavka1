/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.selectors.catLocSelector;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author mato
 */
public interface ICatLocDetail extends IsSerializable {

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    long getId();

    String getName();

    String getParentName();

    long getDemandsCount();

    long getSuppliersCount();

    int getLevel();

    boolean isLeaf();

    boolean isLeafsParent();

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    void setId(long id);

    void setName(String name);

    void setParentName(String name);

    void setDemandsCount(long demandsCount);

    void setSuppliersCount(long suppliersCount);

    void setLevel(int level);

    void setLeaf(boolean isLeaf);

    void setLeafsParent(boolean leafsParent);
}
