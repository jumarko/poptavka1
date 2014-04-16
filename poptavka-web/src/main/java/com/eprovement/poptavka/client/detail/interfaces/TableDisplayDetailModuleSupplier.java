/*
 * Copyright 2014 (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail.interfaces;

/**
 *
 * @author Martin Slavkovsky
 */
public interface TableDisplayDetailModuleSupplier {

    //Demand detail tab
    long getDemandId();

    //Supplier detail tab
    long getSupplierId();

    //Conversation detail tab
    long getThreadRootId();

    long getSenderId();
}
