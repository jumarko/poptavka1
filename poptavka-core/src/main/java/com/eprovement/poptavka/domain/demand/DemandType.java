package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.register.Register;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * Type of demand. It is closely connected to the business relation to the
 * {@link com.eprovement.poptavka.domain.user.User}
 * and {@link com.eprovement.poptavka.domain.product.UserService}.
 *
 * @author Juraj Martinka
 *         Date: 31.1.11
 */
@Entity
@Audited
public class DemandType extends Register {

    /**
     * Enum for handy work with demand type.
     * @see #getType()
     */
    public static enum Type {
        NORMAL("normal"),

        /** Special demand at top positions. */
        ATTRACTIVE("attractive");

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

        public String getValue() {
            return value;
        }
    }


    private String description;


    public DemandType() {
    }

    public DemandType(String code, String description) {
        super(code);
        this.description = description;
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
        sb.append("{code='").append(getCode()).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
