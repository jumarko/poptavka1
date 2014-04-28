/*
 * Copyright 2014 (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail.interfaces;

/**
 *
 * @author Martin Slavkovsky
 */
public interface TableDisplayDetailModuleClient {

    //Demand detail tab
    long getDemandId();

    //Client detail tab
    long getClientId();

    //Conversation detail tab
    long getThreadRootId();

    long getSenderId();
}
