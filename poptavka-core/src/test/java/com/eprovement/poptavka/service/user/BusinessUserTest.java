package com.eprovement.poptavka.service.user;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Partner;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.service.GeneralService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 9.5.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class BusinessUserTest extends DBUnitIntegrationTest {

    private static final BusinessUser TEST_USER = new BusinessUser("myemail@email.com", "passwd");

    @Autowired
    private GeneralService generalService;

    @Test
    public void testBusinessUserWithMultipleRoles() {
        final BusinessUser userWithMultipleRoles = this.generalService.find(BusinessUser.class, 111111114L);
        Assert.assertNotNull(userWithMultipleRoles);
        Assert.assertEquals(3, userWithMultipleRoles.getBusinessUserRoles().size());
        checkUserRole(Client.class, userWithMultipleRoles);
        checkUserRole(Supplier.class, userWithMultipleRoles);
        checkUserRole(Partner.class, userWithMultipleRoles);

        // for sure, check other user data
        Assert.assertEquals("My Fourth Company", userWithMultipleRoles.getBusinessUserData().getCompanyName());
        Assert.assertEquals("elviraM@email.com", userWithMultipleRoles.getEmail());
    }


    @Test
    public void testUserSettings() {

    }

    @Test
    public void userWithoutOriginIsNotExternalUser() throws Exception {
        assertFalse("user without origin should not be considered as external one",
                TEST_USER.isUserFromExternalSystem());
    }

    @Test
    public void userWithOtherOriginIsNotExternalUser() throws Exception {
        final Origin origin = new Origin();
        origin.setUrl("want-something.com");
        origin.setName("internal user");
        origin.setDescription("Internal user for want-something.com");
        origin.setCode("internal");
        TEST_USER.setOrigin(origin);
        assertFalse("user with origin other than external should not be considered as external user",
                TEST_USER.isUserFromExternalSystem());
    }

    @Test
    public void userWithProperOriginIsExternalUser() throws Exception {
        final Origin origin = new Origin();
        origin.setUrl("partner.com");
        origin.setName("external user");
        origin.setDescription("external user from partner.com");
        origin.setCode(Origin.EXTERNAL_ORIGIN_CODE + ".partner.com");
        TEST_USER.setOrigin(origin);
        assertTrue("user with proper origin should be considered as external one",
                TEST_USER.isUserFromExternalSystem());
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------

    /**
     * Checks if given user <code>businessUser</code> has role specified by
     *
     * @param businessRoleClass class
     * @param businessUser
     */
    private void checkUserRole(final Class<? extends BusinessUserRole> businessRoleClass, BusinessUser businessUser) {
        Assert.assertNotNull(businessUser);
        Assert.assertNotNull(businessRoleClass);
        Assert.assertTrue("User does not have the role: " + businessRoleClass,
                CollectionUtils.exists(businessUser.getBusinessUserRoles(), new Predicate() {
                    @Override
                    public boolean evaluate(Object user) {
                        return businessRoleClass.equals((user).getClass());
                    }
                }));
    }
}

