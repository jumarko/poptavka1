package com.eprovement.poptavka.domain.user.rights;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.user.User;
import org.junit.Test;

import java.util.Arrays;

public class AccessRoleTest {
    private final User admin = createUserWithAccessRole(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE);
    private final User client = createUserWithAccessRole(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE);
    private final User supplier = createUserWithAccessRole(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE);
    private final User user = createUserWithAccessRole(CommonAccessRoles.USER_ACCESS_ROLE_CODE);

    @Test
    public void testIsAdmin() throws Exception {
        assertTrue("User=" + admin + "  should be admin", AccessRole.isAdmin(admin));
        assertFalse("User=" + client + "  should NOT be admin", AccessRole.isAdmin(client));
        assertFalse("User=" + supplier + "  should NOT be admin", AccessRole.isAdmin(supplier));
        assertFalse("User=" + user + "  should NOT be admin", AccessRole.isAdmin(user));
    }

    @Test
    public void testIsUser() throws Exception {
        assertTrue("User=" + user + "  should be user", AccessRole.isUser(user));
        assertFalse("User=" + client + "  should NOT be user", AccessRole.isUser(client));
        assertFalse("User=" + supplier + "  should NOT be user", AccessRole.isUser(supplier));
        assertFalse("User=" + admin + "  should NOT be user", AccessRole.isUser(admin));
    }

    @Test
    public void testIsClient() throws Exception {
        assertTrue("User=" + client + "  should be client", AccessRole.isClient(client));
        assertFalse("User=" + user + "  should NOT be client", AccessRole.isClient(user));
        assertFalse("User=" + supplier + "  should NOT be client", AccessRole.isClient(supplier));
        assertFalse("User=" + admin + "  should NOT be client", AccessRole.isClient(admin));
    }

    @Test
    public void testIsSupplier() throws Exception {
        assertTrue("User=" + supplier + "  should be supplier", AccessRole.isSupplier(supplier));
        assertFalse("User=" + user + "  should NOT be supplier", AccessRole.isSupplier(user));
        assertFalse("User=" + client + "  should NOT be supplier", AccessRole.isSupplier(client));
        assertFalse("User=" + admin + "  should NOT be supplier", AccessRole.isSupplier(admin));
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private User createUserWithAccessRole(String accessRoleCode) {
        final User user = new User("test@email.com", "password");
        final AccessRole accessRole = new AccessRole();
        accessRole.setCode(accessRoleCode);
        user.setAccessRoles(Arrays.asList(accessRole));
        return user;
    }




}
