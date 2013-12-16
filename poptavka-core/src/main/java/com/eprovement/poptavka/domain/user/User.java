package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.application.security.aspects.Encrypted;
import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.settings.Settings;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Common class representing a user that can log in the system
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
// use slightly different name because "User" is a reserved word
@Table(name = "UserT")
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends DomainObject {

    @Column(length = 64)
    @NotBlank
    private String password;

    @Column(nullable = false, unique = true, length = 128)
    /** User's email, serves also as a login.  */
    @Email
    private String email;

    /** Roles assigned to this user in the application. */
    @ManyToMany
    @NotAudited
    // Each user must have at least one access role!
    @NotEmpty
    private List<AccessRole> accessRoles;

    /**
     * Settings of this user. Each user has some settings. If user is removed there is no reason to store his
     * settings anymore.
     */
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @NotAudited
    @NotNull
    private Settings settings = new Settings();


    @OneToOne
    @NotAudited
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private ActivationEmail activationEmail;


    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    @Encrypted
    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<AccessRole> getAccessRoles() {
        return accessRoles;
    }

    public void setAccessRoles(List<AccessRole> accessRoles) {
        this.accessRoles = accessRoles;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ActivationEmail getActivationEmail() {
        return activationEmail;
    }

    public void setActivationEmail(ActivationEmail activationEmail) {
        this.activationEmail = activationEmail;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("User");
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
