/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.client.common.validation.Email;

import javax.validation.constraints.Size;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Martin
 */
public class EmailDialogDetail implements IsSerializable {

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
    // TODO RELEASE ivlcek: test it and resolve,
    // chceme rozlisovat, v ktorom okne?
    // Pri moznej zmene alebo preklade bude treba menit na viacerych miestach
    @NotBlank(message = "{emailNotBlank}")
    @Email(message = "{emailEmail}")
    private String emailFrom;
    @NotBlank(message = "{emailDialogNotNullMessage}")
    @Size(max = 1000, message = "{emailDialogSizeMessage}")
    private String message;

    public EmailDialogDetail() {
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
