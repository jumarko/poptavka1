/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    long getDemandsCount();

    long getSuppliersCount();

    int getLevel();

    boolean isLeaf();

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    void setId(long id);

    void setName(String name);

    void setDemandsCount(long demandsCount);

    void setSuppliersCount(long suppliersCount);

    void setLevel(int level);

    void setLeaf(boolean isLeaf);
}
