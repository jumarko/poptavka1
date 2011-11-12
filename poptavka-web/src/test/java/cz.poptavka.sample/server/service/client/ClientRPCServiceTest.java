/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.server.service.client;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.client.service.demand.ClientRPCService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.register.RegisterService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.UserDetail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    @Autowired
    private RegisterService registerService;


    @Before
    public void setUp() {
        Preconditions.checkNotNull(this.generalService);
        Preconditions.checkNotNull(this.clientService);
        Preconditions.checkNotNull(this.localityService);
        Preconditions.checkNotNull(this.registerService);

        final ClientRPCServiceImpl clientRpcService = new ClientRPCServiceImpl();
        clientRpcService.setGeneralService(this.generalService);
        clientRpcService.setClientService(this.clientService);
        clientRpcService.setLocalityService(this.localityService);
        clientRpcService.setRegisterService(this.registerService);

        this.clientRPCService = clientRpcService;
    }

    @Test
    public void testCreateNewClient() throws Exception {
        final UserDetail testUser = new UserDetail("test@poptavka.com", "testpasswd");

        this.clientRPCService.createNewClient(testUser);
    }
}
