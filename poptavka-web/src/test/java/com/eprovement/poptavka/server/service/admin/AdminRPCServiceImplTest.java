/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.service.admin;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.demand.PotentialDemandService;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminRPCServiceImplTest {

    private static final Long DEMAND_ID = 123L;
    private static final Demand DEMAND = new Demand();

    private AdminRPCServiceImpl adminRPCService;
    private PotentialDemandService potentialDemandService;

    @Before
    public void setUp() throws Exception {

        this.adminRPCService = new AdminRPCServiceImpl();

        final DemandService demandService = mock(DemandService.class);
        when(demandService.getById(DEMAND_ID)).thenReturn(DEMAND);
        adminRPCService.setDemandService(demandService);
        this.potentialDemandService = mock(PotentialDemandService.class);
        adminRPCService.setPotentialDemandService(potentialDemandService);
    }

    @Test
    public void testApproveDemands() throws Exception {
        final FullDemandDetail demandDetail = new FullDemandDetail();
        demandDetail.setDemandId(DEMAND_ID);
        adminRPCService.approveDemands(new HashSet<FullDemandDetail>() { { add(demandDetail); } });
        verify(potentialDemandService).sendDemandToPotentialSuppliers(DEMAND);
    }
}
