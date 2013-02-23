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

    private String recipient;
    private String subject;
    //TODO - zjednotit niektore popisky? NotBlankEmail vseobecne, alebo to chceme rozlisovat, v ktorom okne?
    //Pri moznej zmene alebo preklade bude treba menit na viacerych miestach
    @NotBlank(message = "{emailDialogNotBlankEmail}")
    @Email(message = "{emailDialogEmail}")
    private String emailFrom;
    @NotBlank(message = "{emailDialogNotNullMessage}")
    @Size(max = 1000, message = "{emailDialogSizeMessage}")
    //TODO - dorobit vlastne anotacie a rozpoznanie HTML a JavaScriptu ???
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
