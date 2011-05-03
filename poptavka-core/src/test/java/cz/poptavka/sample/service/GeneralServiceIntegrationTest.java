package cz.poptavka.sample.service;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Only very simple test of {@link GeneralService}.
 * The main goal is verify if configuration of {@link GeneralService} and its internal
 * {@link com.googlecode.genericdao.dao.jpa.GeneralDAO} is OK.
 *
 * @author Juraj Martinka
 *         Date: 3.5.11
 */
@DataSet(path = "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        dtd = "classpath:test.dtd")
public class GeneralServiceIntegrationTest extends DBUnitBaseTest {


    @Autowired
    private GeneralService generalService;

    @Test
    public void testFindAllLocalities() {
        final List<Locality> allLocalities = this.generalService.findAll(Locality.class);

        Assert.assertEquals("All localities must be found - nothing less nothing more.", 12, allLocalities.size());
    }
}
