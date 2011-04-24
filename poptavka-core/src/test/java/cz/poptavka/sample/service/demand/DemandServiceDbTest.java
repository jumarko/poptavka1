package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Person;
import cz.poptavka.sample.service.user.ClientService;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * Test that communicates with real db!
 *
 * <p> All methods that alter data in database should be marked as @Ignored.
 *
 *
 *
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@Transactional
public class DemandServiceDbTest {

    @Autowired
    private DemandService demandService;

    @Autowired
    private ClientService clientService;


    @Test
    @Ignore // do not create clients in live DB every time this test is run
    public void testCreateDemand() {
        final Demand demand = new Demand();
        demand.setTitle("Title poptavka");
        demand.setType(this.demandService.getDemandType(DemandType.Type.NORMAL.getValue()));
        demand.setPrice(BigDecimal.valueOf(10000));
        demand.setMaxSuppliers(20);
        demand.setMinRating(99);
        demand.setStatus(DemandStatus.NEW);
        final String[] dateFormat = new String[] {"yyyy-mm-dd" };
        try {
            demand.setEndDate(DateUtils.parseDate("2011-05-01", dateFormat));
            demand.setValidTo(DateUtils.parseDate("2011-06-01", dateFormat));
        } catch (ParseException e) {
            Assert.fail("Incorrect date format in test data.");
        }

        demand.setClient(createNewClient());
        demandService.create(demand);
    }

    private Client createNewClient() {
        final Client newClient = new Client();
        newClient.setEmail("test@poptavam.com");
        newClient.setPerson(new Person("Test", "Client"));
        return this.clientService.create(newClient);
    }

}
