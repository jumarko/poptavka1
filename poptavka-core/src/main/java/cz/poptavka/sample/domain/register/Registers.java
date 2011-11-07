/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.domain.register;

public final class Registers {


    public static final class Notification {
        public static final String NONREGISTERED_CLIENT = "nonregistered.client";
        public static final String NONREGISTERED_SUPPLIER = "nonregistered.supplier";
        public static final String NEW_MESSAGE_SUPPLIER = "new.message.supplier";
        public static final String NEW_DEMAND = "new.demand";
        public static final String NEW_OFFER = "new.offer";
        public static final String NEW_INFO = "new.info";
        public static final String NEW_OPERATOR = "new.operator";
        public static final String DEMAND_STATUS_CHANGED = "demand.status.changed";
        public static final String OFFER_STATUS_CHANGED = "offer.status.changed";
        public static final String NEW_MESSAGE_CLIENT = "new.message.client";
        public static final String NEW_INFO_CLIENT = "new.info.client";
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
