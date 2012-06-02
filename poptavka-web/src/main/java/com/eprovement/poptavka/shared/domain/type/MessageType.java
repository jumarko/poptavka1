package com.eprovement.poptavka.shared.domain.type;

/**
 * Enum to divide messageDetail hiding behind common MessageDetail interface
 * @author Beho
 *
 */
public enum MessageType {
    CONVERSATION("conversation"),

    POTENTIAL_DEMAND("potential"),

    OFFER("offer");
    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
