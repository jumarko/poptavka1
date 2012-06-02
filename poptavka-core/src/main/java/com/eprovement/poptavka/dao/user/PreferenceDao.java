package com.eprovement.poptavka.dao.user;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.settings.Preference;

/**
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public interface PreferenceDao extends GenericDao<Preference> {

    Preference getPreference(String key);

}
