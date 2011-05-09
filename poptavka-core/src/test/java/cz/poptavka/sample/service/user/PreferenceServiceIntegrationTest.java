package cz.poptavka.sample.service.user;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.settings.Preference;
import cz.poptavka.sample.domain.user.Client;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 2.5.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/user/PreferencesDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class PreferenceServiceIntegrationTest extends DBUnitBaseTest {

    private static final String USER_COLOR_FAVOURITE = "user.color.favourite";
    private static final String USER_MAIL_DEMAND = "user.mail.demand.updated.notification";
    private static final long USER_WITH_PREFERECENCES = 111L;


    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private ClientService clientService;

    @Test
    public void testGetPreference() throws Exception {

        final Preference preference = this.preferenceService.getPreference(USER_COLOR_FAVOURITE);
        Assert.assertEquals("RED", preference.getValue());

        final Preference noPreference = this.preferenceService.getPreference("user.color.unknown");
        Assert.assertNull(noPreference);
    }


    @Test
    public void testGetPreferenceForClient() throws Exception {

        final Client firstUser = this.clientService.getById(USER_WITH_PREFERECENCES);
        Assert.assertNotNull(firstUser);
        Assert.assertNotNull(firstUser.getBusinessUser().getSettings());
        final List<Preference> userPreferences = firstUser.getBusinessUser().getSettings().getPreferences();
        Assert.assertEquals(2, userPreferences.size());

        checkPreferenceExist(new Preference(USER_COLOR_FAVOURITE, "RED"), userPreferences);
        checkPreferenceExist(new Preference(USER_MAIL_DEMAND, "true"), userPreferences);
    }


    //---------------------------------------------- HELPER METHEODS ---------------------------------------------------

    /**
     * Checks whether given preference <code>preferenceToCheck</code> is between preferences in
     * <code>userPreferences</code>.
     * <p>
     * Key and value of given preference are compared.
     *
     * @param preferenceToCheck
     * @param userPreferences
     */
    private void checkPreferenceExist(final Preference preferenceToCheck, List<Preference> userPreferences) {
        Assert.assertTrue(CollectionUtils.exists(userPreferences, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Preference objectPref = (Preference) object;
                boolean result = true;
                result &= preferenceToCheck.getKey().equals(objectPref.getKey());
                result &= preferenceToCheck.getValue().equals(objectPref.getValue());
                return result;
            }
        }));
    }
}
