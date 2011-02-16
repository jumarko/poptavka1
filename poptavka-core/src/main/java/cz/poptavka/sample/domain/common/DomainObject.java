package cz.poptavka.sample.domain.common;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class DomainObject implements Serializable {

    /** Id of the entity. */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null && id == null) {
            throw new NullPointerException("id");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DomainObject that = (DomainObject) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
