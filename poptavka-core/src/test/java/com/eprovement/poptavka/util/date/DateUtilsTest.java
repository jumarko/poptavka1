package com.eprovement.poptavka.util.date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Juraj Martinka
 *         Date: 25.4.11
 */
public class DateUtilsTest {

    @Test
    public void testParseDate() {
        checkDay(2011, 9, 10);
        checkDay(1899, 0, 1);
        checkDay(2099, 0, 1);
        checkDay(2011, 11, 31);
        // check if DateUtils are lenient - the actual result date should be 2012-01-10 !
        checkDay(2011, 12, 10);
    }

    /**
     * Check if given date is parsed correctly with DateUtils.
     * Watch out, months are numbered from zero (in Java).
     *
     * @param year
     * @param month
     * @param day
     */
    private void checkDay(int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.clear(Calendar.MILLISECOND);
        final Date parsedDate = DateUtils.parseDate(year + "-" + (month + 1) + "-" + day);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals(calendar.getTime(), parsedDate);
    }
}
