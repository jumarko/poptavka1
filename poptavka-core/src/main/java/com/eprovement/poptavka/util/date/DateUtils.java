package com.eprovement.poptavka.util.date;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Juraj Martinka
 *         Date: 25.4.11
 */
public final class DateUtils {

    private static final String[] DATE_FORMATS = new String[] {
        "yyyy-MM-dd",
    };


    private DateUtils() {
        // utility class - DO NOT INSTANTIATE!
    }

    /**
     * Parse given <code>dateString</code> and return Date object which the string represents.
     * Supported formats are:
     * <ul>
     *     <li>yyyy-MM-dd</li>
     * </ul>
     *
     * @param dateString
     * @return Date represented by given string
     * @throws  IllegalArgumentException if dateString is in
     */
    public static Date parseDate(String dateString) {
        try {
            return org.apache.commons.lang.time.DateUtils.parseDate(dateString, DATE_FORMATS);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date string is not in supported date format. Allowed formats: "
                    + Arrays.toString(DATE_FORMATS));
        }
    }
}
