/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.usermessage;

import com.eprovement.poptavka.base.RealDbTest;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.service.GeneralService;
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

    @Test
    @Ignore // DO NOT update concrete User message in live DB every time this test is run
            // - that record even might not be exist!
    @Transactional(propagation = Propagation.REQUIRED)
    public void testSetMessageReadStatus() {
        final UserMessage unreadUserMessage = this.userMessageService.getById(602L);
        unreadUserMessage.setRead(false);
        this.userMessageService.update(unreadUserMessage);
        System.out.println(unreadUserMessage);
    }

}
