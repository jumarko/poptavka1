package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.settings.Preference;

/**
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public interface PreferenceService {

    /**
     * Retrieves user preference identified by <code>key</code>.
     *
     * @param key
     * @return
     */
    Preference getPreference(String key);
}
