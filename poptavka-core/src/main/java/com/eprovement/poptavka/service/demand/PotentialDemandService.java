/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;
import com.eprovement.poptavka.exception.MessageException;

/**
 * Methods related to potential demands messages ("notifications") handling/sending.
 */
public interface PotentialDemandService {


    /**
     * Send given demand to all potential suppliers.
     * @param demand
     * @throws com.eprovement.poptavka.exception.MessageException if some error occurs by sending message to suppliers.
     */
    void sendDemandToPotentialSuppliers(Demand demand) throws MessageException;


    /**
     * Send given demand to the given potential supplier.
     * @param demand demand to be sent
     * @param potentialSupplier supplier to which {@code demand} will be sent
     * @throws com.eprovement.poptavka.exception.MessageException if some error occurs by sending message to suppliers.
     */
    void sendDemandToPotentialSupplier(Demand demand, PotentialSupplier potentialSupplier)
        throws MessageException;
}
