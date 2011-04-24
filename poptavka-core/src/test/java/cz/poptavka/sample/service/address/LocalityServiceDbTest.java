package cz.poptavka.sample.service.address;

/**
 * @author Juraj Martinka
 *         Date: 1.3.11
 */

import cz.poptavka.sample.base.RealDbTest;
import cz.poptavka.sample.domain.address.Locality;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * This test DEPENDS on the state of database.
 * But in this case this is a correct approach because the main task of this test is to test functionality directly
 * with concrete data in database.
 */
public class LocalityServiceDbTest extends RealDbTest {

    private static final String STREDOCESKY_KRAJ_CODE = "CZ020";
    private static final String BENESOV_CODE = "CZ0201";

    @Autowired
    private LocalityService localityService;

    @Test
    public void testGetLocalities() {
        final Locality stredoceskyKraj = localityService.getLocality(STREDOCESKY_KRAJ_CODE);
        final List<Locality> stredoceskyOkresy = stredoceskyKraj.getChildren();

        Assert.assertEquals(12, stredoceskyOkresy.size());

        for (Locality locality : stredoceskyOkresy) {
            if (BENESOV_CODE.equalsIgnoreCase(locality.getCode())) {
                // everything OK
                return;
            }
        }
        // Benesov was not in - error
        Assert.fail("Benesov should belongs to Stredocesky kraj.");
    }
}
