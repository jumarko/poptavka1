package com.eprovement.poptavka.shared.domain.adminModule;

import com.eprovement.poptavka.shared.domain.demand.OriginDetail;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import java.util.Date;

/**
 * @author Martin Slavkovsky
 * @since 21.5.2014
 */
public class AdminClientDetail implements IsSerializable {

    private Long id;
    private Long userId;
    private Date created;
    private String email;
    private String firstName;
    private String lastName;
    private OriginDetail origin;

    public static final ProvidesKey<AdminClientDetail> KEY_PROVIDER = new ProvidesKey<AdminClientDetail>() {

        @Override
        public Object getKey(AdminClientDetail item) {
            return item == null ? null : item.getId();
        }
    };

    /** for serialization. **/
    public AdminClientDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public OriginDetail getOrigin() {
        return origin;
    }

    public void setOrigin(OriginDetail origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "AdminClientDetail{"
            + "id=" + id
            + ", created=" + created
            + ", email=" + email
            + ", firstName=" + firstName
            + ", lastName=" + lastName
            + ", origin=" + origin + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdminClientDetail other = (AdminClientDetail) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
