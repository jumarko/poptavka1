package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
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
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class BusinessUserTest extends DBUnitIntegrationTest {

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
                    public boolean evaluate(Object object) {
                        return businessRoleClass.equals(((BusinessUserRole) object).getClass());
                    }
                }));
    }
}

