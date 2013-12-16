package com.eprovement.poptavka.client.detail;

public enum DetailModuleTabs {

    DEMAND_DETAIL_TAB(1),
    USER_DETAIL_TAB(2),
    CONVERSATION_TAB(3),
    ADVERTISEMENT_TAB(4);

    private final int value;

    private DetailModuleTabs(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DetailModuleTabs toDetailModuleTabs(int value) {
        for (DetailModuleTabs tabValue : DetailModuleTabs.values()) {
            if (value == tabValue.getValue()) {
                return tabValue;
            }
        }
        return null;
    }
}