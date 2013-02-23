package com.eprovement.poptavka.util.strings;

import com.eprovement.poptavka.domain.common.DomainObject;

/**
 * @author Juraj Martinka
 *         Date: 16.4.11
 */
public final class ToStringUtils {

    private ToStringUtils() {
        // utility class - DO NOT INSTANTIATE!
    }


    /**
     * Check whether <code>domainObject</code> is not null and return its id as a string.
     * Otherwise the null string representation is returned.
     *
     * @param domainObject
     * @return id of <code>domainObject</code> or "[NULL]"
     */
    public static String printId(DomainObject domainObject) {
        if (domainObject != null) {
            return "" + domainObject.getId();
        }

        return "[null]";
    }
}
