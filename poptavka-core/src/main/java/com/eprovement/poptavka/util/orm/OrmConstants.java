package com.eprovement.poptavka.util.orm;

/**
 * Wrapper for constants related to the peristent entities and their fields.
 *
 * @author Juraj Martinka
 *         Date: 7.5.11
 */
public final class OrmConstants {
    public static final int TEXT_LENGTH = 65535;
    public static final int ENUM_FIELD_LENGTH = 64;
    public static final int ENUM_SHORTINT_FIELD_LENGTH = 2;

    private OrmConstants() {
        // utility class - DO NOT INSTANTIATE!
    }
}
