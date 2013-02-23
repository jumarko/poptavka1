package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.dao.user.PreferenceDao;
import com.eprovement.poptavka.domain.settings.Preference;
import com.eprovement.poptavka.service.GenericServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides methods for accessing different user's preferences in a system.
 *
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public class PreferenceServiceImpl extends GenericServiceImpl<Preference, PreferenceDao> implements PreferenceService {

    public PreferenceServiceImpl(PreferenceDao preferenceDao) {
        setDao(preferenceDao);
    }

    @Override
    @Transactional(readOnly = true)
    public Preference getPreference(String key) {
        return this.getDao().getPreference(key);
    }
}
