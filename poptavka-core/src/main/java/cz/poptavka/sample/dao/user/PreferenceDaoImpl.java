package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.settings.Preference;
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
