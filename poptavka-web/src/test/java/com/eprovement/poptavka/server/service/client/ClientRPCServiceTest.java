/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.service.client;

import com.eprovement.poptavka.base.BasicIntegrationTest;
import com.eprovement.poptavka.client.service.demand.ClientRPCService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore
public class ClientRPCServiceTest extends BasicIntegrationTest {

    private ClientRPCService clientRPCService;

    @Autowired
    private ClientService clientService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private LocalityService localityService;


    @Before
    public void setUp() {
        Preconditions.checkNotNull(this.generalService);
        Preconditions.checkNotNull(this.clientService);
        Preconditions.checkNotNull(this.localityService);

        final ClientRPCServiceImpl clientRpcService = new ClientRPCServiceImpl();
        clientRpcService.setGeneralService(this.generalService);
        clientRpcService.setClientService(this.clientService);
        clientRpcService.setLocalityService(this.localityService);

        this.clientRPCService = clientRpcService;
    }

    @Test
    public void testCreateNewClient() throws Exception {
        final BusinessUserDetail testUser = new BusinessUserDetail();
        testUser.setEmail("test@poptavka.com");
        testUser.setPassword("testpasswd");

        this.clientRPCService.createNewClient(testUser);
    }
}
