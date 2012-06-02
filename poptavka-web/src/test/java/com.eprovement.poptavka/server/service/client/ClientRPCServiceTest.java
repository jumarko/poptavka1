/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.service.client;

import com.google.common.base.Preconditions;
import com.eprovement.poptavka.client.service.demand.ClientRPCService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.shared.domain.UserDetail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
public class ClientRPCServiceTest {

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
        final UserDetail testUser = new UserDetail("test@poptavka.com", "testpasswd");

        this.clientRPCService.createNewClient(testUser);
    }
}
