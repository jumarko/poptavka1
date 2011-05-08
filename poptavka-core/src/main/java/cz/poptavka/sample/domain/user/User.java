package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.activation.EmailActivation;
import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.settings.Settings;
import cz.poptavka.sample.domain.user.rights.AccessRole;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * Represents ordinary user in application without further specification.
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
    private String password;

    @Column(nullable = false, unique = true, length = 128)
    /** User's email, serves also as a login.  */
    private String email;

    /** Roles assigned to this user in the application. */
    @ManyToMany
    @NotAudited
    private List<AccessRole> accessRoles;

    @OneToOne(optional = false)
    @NotAudited
    private Settings settings;


    @OneToOne
    @NotAudited
    private EmailActivation emailActivation;


    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

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

    public EmailActivation getEmailActivation() {
        return emailActivation;
    }

    public void setEmailActivation(EmailActivation emailActivation) {
        this.emailActivation = emailActivation;
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
