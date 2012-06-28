/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.domain.enums;

import com.eprovement.poptavka.domain.register.Register;

import javax.persistence.Entity;

/**
 *
 * TODO ivlcek - prerobit na standardny ciselnik s kodmi
 * @author Vojtech Hubr
 *         Date 12.4.11

 */
@Entity
public class OfferState extends Register {

    /**
     * Enum for handy work with demand type.
     * @see #getType()
     */
    public static enum Type {

        ACCEPTED("ACCEPTED"),
        /** Special demand at top positions. */
        PENDING("PENDING"),
        DECLINED("DECLINED");
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
            throw new IllegalArgumentException("No OfferState enum constant for type code [" + typeCode + "].");
        }

        public String getValue() {
            return value;
        }
    }

    private String description;

    public OfferState() {
    }

    public Type getType() {
        return Type.fromValue(getCode());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OfferState");
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
