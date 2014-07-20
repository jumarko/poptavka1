/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.client.common.validation.Email;
import com.eprovement.poptavka.client.common.validation.Extended;

import javax.validation.constraints.Size;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Martin
 */
public class ContactUsDetail implements IsSerializable {

    /** Enums. **/
    public enum Field {

        RECIPIENT("recipient"),
        SUBJECT("subject"),
        EMAIL_FROM("emailFrom"),
        MESSAGE("message");

        private String value;

        private Field(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String recipient;
    private String subject;
    @NotBlank(message = "{emailNotBlank}")
    @Email(message = "{patternEmail}", groups = Extended.class)
    @Size(max = 255, message = "{emailSize}", groups = Extended.class)
    private String emailFrom;

    @NotBlank(message = "{emailDialogMessageNotBlank}")
    @Size(max = 1000, message = "{emailDialogMessageSize}", groups = Extended.class)
    private String message;

    public ContactUsDetail() {
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
