package com.eprovement.poptavka.dao.message;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.enums.MessageContext;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;

/**
 * Class represents a filter that can be used for applying various restrictions on messages returned
 * from service methods.
 *
 * @see com.eprovement.poptavka.service.message.MessageService
 * @author Juraj Martinka
 *         Date: 12.5.11
 */
public final class MessageFilter {

    public static final MessageFilter EMPTY_FILTER = new MessageFilter();

    private MessageUserRoleType messageUserRoleType;
    private MessageContext messageContext;

    private ResultCriteria resultCriteria;


    private MessageFilter() {
        // use builder instead
    }

    //---------------------------------- GETTERS -----------------------------------------------------------

    public MessageUserRoleType getMessageUserRoleType() {
        return messageUserRoleType;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }

    public ResultCriteria getResultCriteria() {
        return resultCriteria;
    }

    //----------------------------------  Builder ----------------------------------------------------------------------
    public static final class MessageFilterBuilder {
        private MessageFilter messageFilter;

        private MessageFilterBuilder() {
            messageFilter = new MessageFilter();
        }

        public MessageFilterBuilder withMessageUserRoleType(MessageUserRoleType messageUserRoleType) {
            messageFilter.messageUserRoleType = messageUserRoleType;
            return this;
        }

        public MessageFilterBuilder withMessageContext(MessageContext messageContext) {
            messageFilter.messageContext = messageContext;
            return this;
        }

        public MessageFilterBuilder withResultCriteria(ResultCriteria resultCriteria) {
            messageFilter.resultCriteria = resultCriteria;
            return this;
        }

        public static MessageFilterBuilder messageFilter() {
            return new MessageFilterBuilder();
        }

        public MessageFilter build() {
            return messageFilter;
        }
    }
}
