/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.usermessage;

import cz.poptavka.sample.base.RealDbTest;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.service.GeneralService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class UserMessageServiceDbTest extends RealDbTest {

    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private GeneralService generalService;


    @Ignore // DO NOT update concrete User message in live DB every time this test is run
    // - that record even might not be exist!
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testSetMessageReadStatus() {
        final UserMessage unreadUserMessage = this.userMessageService.getById(602L);
        unreadUserMessage.setRead(false);
        this.userMessageService.update(unreadUserMessage);
        System.out.println(unreadUserMessage);
    }


    @Ignore // DO NOT update concrete User message in live DB every time this test is run
    // - that record even might not be exist!
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testSetMessageReadStatusWithGeneralService() {
        final UserMessage unreadUserMessage = this.generalService.find(UserMessage.class, 602L);
        unreadUserMessage.setRead(true);
        this.generalService.save(unreadUserMessage);
        System.out.println(unreadUserMessage);
    }

}
