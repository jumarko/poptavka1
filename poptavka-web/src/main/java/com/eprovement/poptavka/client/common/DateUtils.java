/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;

/**
 * Provides variety dates or date operations for GWT frontend.
 *
 * @author Martin Slavkovsky
 */
public final class DateUtils {

    private DateUtils() {
    }

    public static long diffDaysInMillis(Date date) {
        Date now = new Date();
        return now.getTime() - date.getTime();
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static Date getYesterdayDate() {
        Date yesterday = new Date();
        CalendarUtil.addDaysToDate(yesterday, -1);
        return yesterday;
    }
}
