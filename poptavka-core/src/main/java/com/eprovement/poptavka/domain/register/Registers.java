package com.eprovement.poptavka.domain.register;

public final class Registers {



    public static enum Notification {

        NEW_MESSAGE("new.message"),
        NEW_INFO ("new.info"),
        NEW_MESSAGE_OPERATOR("new.message.operator"),

        NEW_DEMAND("new.demand"),
        OFFER_STATUS_CHANGED("offer.status.changed"),
        NEW_OFFER("new.offer"),
        DEMAND_STATUS_CHANGED("demand.status.changed");

        private final String code;

        private Notification(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }



    public static final class Service {
        public static final String VALIDITY_1MONTH = "validity.1month";
        public static final String VALIDITY_3MONTH = "validity.3month";
        public static final String VALIDITY_1YEAR = "validity.1year";
        public static final String CLASSIC = "classic";
        public static final String PARTNER_3MONTH = "partner.3month";
    }

    /**
     * This method exists only for satisfaction of DomainObjectTest. No real meaning.
     * @return
     */
    public String toString() {
        return "";
    }
}
