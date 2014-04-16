/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.admin;

import com.eprovement.poptavka.application.ApplicationContextHolder;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.server.security.PoptavkaAuthenticationProvider;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.demand.PotentialDemandService;
import com.eprovement.poptavka.service.user.LoginService;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

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

        mockSecurity();

        this.adminRPCService = new AdminRPCServiceImpl();

        final DemandService demandService = mock(DemandService.class);
        when(demandService.getById(DEMAND_ID)).thenReturn(DEMAND);
        adminRPCService.setDemandService(demandService);
        this.potentialDemandService = mock(PotentialDemandService.class);
        adminRPCService.setPotentialDemandService(potentialDemandService);
    }

    private void mockSecurity() {
        if (ApplicationContextHolder.getApplicationContext() != null) {
            // TODO LATER juraj: there is some application context set by integration tests
            // we should strive for more clear separation of integration tests and unit tests, see task:
            // https://bitbucket.org/poptavka/poptavka-app/issue/201/separate-integration-tests-from-unit-tests
            final Map<String, PoptavkaAuthenticationProvider> authenticationProvider =
                ApplicationContextHolder.getApplicationContext().getBeansOfType(PoptavkaAuthenticationProvider.class);
            final PoptavkaAuthenticationProvider poptavkaAuthenticationProvider =
                    authenticationProvider.entrySet().iterator().next().getValue();
            final LoginService loginService = mock(LoginService.class);
            final String adminUsername = "ja@nieco.com";
            final String adminPassword = "ty";
            final User admin = new User(adminUsername, adminPassword);
            final AccessRole accessRole = new AccessRole();
            accessRole.setCode(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE);
            admin.setAccessRoles(Arrays.asList(accessRole));
            when(loginService.loginUser(adminUsername, adminPassword))
                    .thenReturn(admin);
            poptavkaAuthenticationProvider.setLoginService(loginService);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(adminUsername, adminPassword));
        }
    }

    @Test
    public void testApproveDemands() throws Exception {
        final AdminDemandDetail demandDetail = new AdminDemandDetail();
        demandDetail.setDemandId(DEMAND_ID);
        adminRPCService.approveDemands(new HashSet<AdminDemandDetail>() { { add(demandDetail); } });
        verify(potentialDemandService).sendDemandToPotentialSuppliers(DEMAND);
    }
}
