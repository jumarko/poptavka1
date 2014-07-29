/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.userservice.UserServiceService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ivan.vlcek
 * Date 25.7.2014
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UserServiceDataSet.xml" },
        dtd = "classpath:test.dtd")
public class UserServiceTest extends DBUnitIntegrationTest {

    @Autowired
    private GeneralService generalService;

    @Autowired
    private UserServiceService userServiceService;

    @Test
    public void testAddCredits() {
        int newCredits = 50;
        UserService userService = generalService.find(UserService.class, 1L);
        userServiceService.addCredits(userService.getId(), newCredits);
        BusinessUserData businessUserData = this.generalService.find(BusinessUserData.class, 111111111L);
        Assert.assertEquals("Current credits were different than expected",
                80, businessUserData.getCurrentCredits());
        userServiceService.addCredits(userService.getId(), newCredits);
        BusinessUserData businessUserData2 = this.generalService.find(BusinessUserData.class, 111111111L);
        Assert.assertEquals("Current credits were different than expected",
                130, businessUserData2.getCurrentCredits());
    }

    @Test
    public void testGetPromotionUserServices() {
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, 111111111L);
        List<UserService> promotionServices = userServiceService.getPromotionUserServices(businessUser);
        Assert.assertEquals("Size of promotion user service was different than expected",
                1, promotionServices.size());
    }

    @Test
    public void testHasUserHadPromoService() {
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, 111111111L);
        Assert.assertTrue("BusinessUser with id=" + businessUser.getId() + " already had promo Service with code="
                + Registers.Service.FREE,
                userServiceService.hasUserHadPromoService(businessUser, Registers.Service.FREE));
    }
}
