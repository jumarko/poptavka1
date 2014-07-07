package com.eprovement.poptavka.rest.payment;

public interface SystemUtil {
    /**
     * Returns true, if the application is running in a production environment.
     * @return true, if the application is running in a production environment otherwise false.
     */
    boolean isProduction();
}
