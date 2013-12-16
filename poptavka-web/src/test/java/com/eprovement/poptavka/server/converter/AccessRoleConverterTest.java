/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.base.BasicIntegrationTest;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.Validate;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AccessRoleConverterTest extends BasicIntegrationTest {

    @Autowired
    @Qualifier("accessRoleConverter")
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;

    @Test
    public void testConvertToTarget() throws Exception {
        Validate.notNull(accessRoleConverter);
        final AccessRole accessRole = new AccessRole();
        accessRole.setCode("admin");
        accessRole.setDescription("Admin access role");
        accessRole.setName("Admin");
        final List<Permission> expectedPermissions =
                Arrays.asList(createPermission(1), createPermission(2), createPermission(3));
        accessRole.setPermissions(expectedPermissions);
        final AccessRoleDetail accessRoleDetail = accessRoleConverter.convertToTarget(accessRole);
        assertNotNull(accessRoleDetail);
        assertThat(accessRoleDetail.getCode(), is("admin"));
        assertThat(accessRoleDetail.getDescription(), is("Admin access role"));
        assertThat(accessRoleDetail.getName(), is("Admin"));
        checkPermissions(accessRoleDetail.getPermissions(), expectedPermissions);
    }

    private void checkPermissions(List<PermissionDetail> actualPermissions, List<Permission> expectedPermissions) {
        assertTrue(actualPermissions.size() == expectedPermissions.size());
        for (int i = 0; i < expectedPermissions.size(); i++) {
            expectedPermissions.get(i).getCode().equals(actualPermissions.get(i).getCode());
            expectedPermissions.get(i).getName().equals(actualPermissions.get(i).getName());
            expectedPermissions.get(i).getDescription().equals(actualPermissions.get(i).getDescription());
            expectedPermissions.get(i).getId().equals(actualPermissions.get(i).getId());
        }
    }

    private Permission createPermission(int permissionNumber) {
        final Permission permission1 = new Permission();
        permission1.setCode("admin.permission." + permissionNumber);
        permission1.setDescription("Administrator permission 1 allowing him to do...");
        permission1.setName("Admin permission");
        permission1.setId(Long.valueOf(permissionNumber));
        return permission1;
    }
}
