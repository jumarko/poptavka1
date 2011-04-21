package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.settings.Preference;

/**
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public interface PreferenceDao extends GenericDao<Preference> {

    Preference getPreference(String key);

}
