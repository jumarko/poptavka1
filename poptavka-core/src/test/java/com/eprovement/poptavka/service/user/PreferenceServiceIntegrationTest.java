package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.settings.Preference;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.GeneralService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 2.5.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/user/PreferencesDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class PreferenceServiceIntegrationTest extends DBUnitIntegrationTest {

    private static final String USER_COLOR_FAVOURITE = "user.color.favourite";
    private static final String USER_MAIL_DEMAND = "user.mail.demand.updated.notification";
    private static final long USER_WITH_PREFERECENCES = 111L;


    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private GeneralService generalService;

    @Test
    public void testGetPreference() throws Exception {

        final Preference preference = this.preferenceService.getPreference(USER_COLOR_FAVOURITE);
        Assert.assertEquals("RED", preference.getValue());

        final Preference noPreference = this.preferenceService.getPreference("user.color.unknown");
        Assert.assertNull(noPreference);
    }


    @Test
    public void testGetPreferenceForClient() throws Exception {

        final Client firstUser = this.generalService.find(Client.class, USER_WITH_PREFERECENCES);
        Assert.assertNotNull(firstUser);
        Assert.assertNotNull(firstUser.getBusinessUser().getSettings());
        final List<Preference> userPreferences = firstUser.getBusinessUser().getSettings().getPreferences();
        Assert.assertEquals(2, userPreferences.size());

        checkPreferenceExist(new Preference(USER_COLOR_FAVOURITE, "RED"), userPreferences);
        checkPreferenceExist(new Preference(USER_MAIL_DEMAND, "true"), userPreferences);
    }


    //---------------------------------------------- HELPER METHODS ---------------------------------------------------

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
