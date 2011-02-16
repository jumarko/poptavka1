package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.domain.common.DomainObject;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Type of demand. It is closely connected to the business relation to the {@link cz.poptavka.sample.domain.user.User}
 * and {@link cz.poptavka.sample.domain.product.UserService}.
 *
 * @author Juraj Martinka
 *         Date: 31.1.11
 */
@Entity
@Audited
public class DemandType extends DomainObject {


    /**
     * Enum for handy work with demand type.
     * @see #getType()
     */
    public static enum Type {
        NORMAL("normal"),

        /** Special demand at top positions. */
        ATTRACTIVE("attractive"),

        /** Encapsulates all other types of demands. */
        ALL("all");


        private final String value;

        Type(String value) {
            this.value = value;
        }

        /**
         * Get enum constant for given type code
         *
          * @param
         * @return
         */
        public static Type fromValue(String typeCode) {
            for (Type type : Type.values()) {
                if (type.value.equals(typeCode)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No DemandType enum constant for type code [" + typeCode + "].");
        }
    }


    @Column(length = 32, nullable = false)
    private String code;

    private String description;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Type getType() {
        return Type.fromValue(getCode());
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DemandType");
        sb.append("{code='").append(code).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
