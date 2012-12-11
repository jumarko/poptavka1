/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.service.user;

import com.eprovement.poptavka.base.BasicIntegrationTest;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.eprovement.poptavka.client.service.demand.UserRPCService;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.service.user.LoginService;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRPCServiceTest extends BasicIntegrationTest {
    protected static final String TEST_USER_MAIL = "test@poptavam.com";
    private UserRPCService userRPCService;
    private LoginService loginServiceMock;

    @Autowired
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;
    @Autowired
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    @Before
    public void setUp() {
        UserRPCServiceImpl userRPCService = new UserRPCServiceImpl();
        final LoginService loginServiceMock = mock(LoginService.class);
        userRPCService.setLoginService(loginServiceMock);
        userRPCService.setAccessRoleConverter(accessRoleConverter);
        userRPCService.setBusinessUserConverter(businessUserConverter);
        this.loginServiceMock = loginServiceMock;
        this.userRPCService = userRPCService;
    }


    @Test
    public void testLoginUser() throws RPCException {
        when(loginServiceMock.loginUser(eq(TEST_USER_MAIL), anyString()))
                .thenReturn(createTestUser());

        final UserDetail loggedUserDetail = userRPCService.loginUser(TEST_USER_MAIL, "myPassword");
        assertThat(loggedUserDetail.getUserId(), is(123L));
        assertThat(loggedUserDetail.getEmail(), is(TEST_USER_MAIL));
        assertThat(loggedUserDetail.getAccessRoles().size(), is(2));

    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private static User createTestUser() {
        final User user = new User();
        user.setId(123L);
        user.setEmail(TEST_USER_MAIL);
        createEditorRole();
        user.setAccessRoles(Arrays.asList(createEditorRole(), createViewRole()));
        return user;
    }

    private static AccessRole createEditorRole() {
        final AccessRole editorRole = new AccessRole();
        editorRole.setCode("editor");
        editorRole.setName("Editor");
        editorRole.setDescription("Can edit various demands, not only those created by him.");
        final Permission editDemandPermission = new Permission();
        editDemandPermission.setCode("edit.demand");
        editDemandPermission.setDescription("Permission for editing demand");
        editDemandPermission.setName("Edit demand");
        editDemandPermission.setId(111L);
        editorRole.setPermissions(Arrays.asList(editDemandPermission));

        return editorRole;
    }

    private static AccessRole createViewRole() {
        final AccessRole viewRole = new AccessRole();
        viewRole.setCode("view");
        viewRole.setName("Viewer");
        viewRole.setDescription("Can view various demands, not only those created by him.");
        final Permission editDemandPermission = new Permission();
        editDemandPermission.setCode("view.demand");
        editDemandPermission.setDescription("Permission for listing demands");
        editDemandPermission.setName("View demand");
        editDemandPermission.setId(112L);
        viewRole.setPermissions(Arrays.asList(editDemandPermission));

        return viewRole;
    }
}
