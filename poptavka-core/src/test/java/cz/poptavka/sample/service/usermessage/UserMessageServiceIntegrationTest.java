/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.usermessage;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.service.GeneralService;
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/demand/RatingDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml",
        "classpath:cz/poptavka/sample/domain/message/MessageDataSet.xml" },
        dtd = "classpath:test.dtd",
        disableForeignKeyChecks = true)
public class UserMessageServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private GeneralService generalService;

    @Test
    public void testGetPotentialDemands() {
        final BusinessUser supplierWithPotentialDemands = new BusinessUser();
        supplierWithPotentialDemands.setId(111111114L);
        final List<UserMessage> potentialDemands =
                this.userMessageService.getPotentialDemands(supplierWithPotentialDemands);
        assertThat("Incorrect number of potential demands for supplier id=" + supplierWithPotentialDemands.getId(),
                potentialDemands.size(), is(2));
    }

    @Test
    public void testSetMessageReadStatus() {
        final UserMessage unreadUserMessage = this.generalService.find(UserMessage.class, 4L);
        unreadUserMessage.setRead(true);
        this.generalService.save(unreadUserMessage);
    }
}
