/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.selectors.catLocSelector;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This is a lesser format of Category detail object that contains only neccessary data for HomeWelcome view.
 * @author ivlcek
 */
public interface ILesserCatLocDetail extends IsSerializable {

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    long getId();

    String getCategoryName();

    long getDemandsCount();

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    void setId(long id);

    void setCategoryName(String name);

    void setDemandsCount(long demandsCount);

}
