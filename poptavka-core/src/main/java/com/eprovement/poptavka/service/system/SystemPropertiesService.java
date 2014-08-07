/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.service.system;

/**
 * @author Martin Slavkovsky
 * @since 4.8.2014
 */
public interface SystemPropertiesService {

    String IMMEDIATE_DEMAND_COUNTS = "immediateIncrementalDemandCounts";
    String IMMEDIATE_SUPPLIER_COUNTS = "immediateIncrementalSupplierCounts";

    /**
     * @return get <code>{@value SystemProperties#IMEDIATE_DEMANDS_COUNTS}</code> property
     */
    boolean isImediateDemandCount();

    /**
     * @return get <code>{@value SystemProperties#IMMEDIATE_SUPPLIER_COUNTS}</code> property
     */
    boolean isImediateSupplierCount();
}
