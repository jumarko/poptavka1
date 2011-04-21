package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.activation.EmailActivation;
import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.settings.Preference;
import cz.poptavka.sample.domain.user.rights.AccessRole;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Represents ordinary user in application without further specification.
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends DomainObject {

    @Column(length = 32)
    private String login;

    /** TODO martinka password should be stored in an array of chars. */
    @Column(length = 64)
    private String password;

    private String email;

    /** Roles assigned to this user in the application. */
    @ManyToMany
    @NotAudited
    private List<AccessRole> accessRoles;

    /** User's preferences for improving user's experience. */
    @OneToMany
    private List<Preference> preferences;


    @OneToOne
    @NotAudited
    private EmailActivation emailActivation;


    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /** TODO martinka password should be stored in an array of chars. */
//    public char[] getPassword() {
//        if (password == null) {
//            return new char[] {};
//        }
//        return Arrays.copyOf(password, password.length);
//    }
//
//    public void setPassword(char[] password) {
//        if (password == null) {
//            this.password = new char[] {};
//        }
//
//        this.password = Arrays.copyOf(password, password.length);
//    }

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

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
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
        sb.append("{login='").append(login).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
