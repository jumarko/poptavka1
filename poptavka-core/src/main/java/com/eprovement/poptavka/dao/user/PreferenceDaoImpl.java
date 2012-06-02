package com.eprovement.poptavka.dao.user;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.settings.Preference;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public class PreferenceDaoImpl extends GenericHibernateDao<Preference> implements PreferenceDao {

    /** {@inheritDoc} */
    @Override
    public Preference getPreference(final String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return (Preference) runNamedQueryForSingleResult("getPreferencesByKey",
                new HashMap<String, Object>() { { put("key", key); } });
    }
}
