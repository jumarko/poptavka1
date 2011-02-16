package cz.poptavka.sample.service.user;

import cz.poptavka.sample.dao.user.PreferenceDao;
import cz.poptavka.sample.domain.user.Preference;
import cz.poptavka.sample.service.GenericServiceImpl;

/**
 * Provides methods for accessing different user's preferences in a system.
 *
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public class PreferenceServiceImpl extends GenericServiceImpl<Preference, PreferenceDao> implements PreferenceService {

    private PreferenceDao preferenceDao;

    public void setPreferenceDao(PreferenceDao preferenceDao) {
        this.preferenceDao = preferenceDao;
    }


    @Override
    public Preference getPreference(String key) {
        return this.preferenceDao.getPreference(key);
    }
}
