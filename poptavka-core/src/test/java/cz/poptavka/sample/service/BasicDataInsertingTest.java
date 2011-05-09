package cz.poptavka.sample.service;

import cz.poptavka.sample.base.RealDbTest;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.settings.Settings;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.util.date.DateUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * Test class which can connect to the real db and create some basic data.
 * @author Juraj Martinka
 *         Date: 9.5.11
 */
public class BasicDataInsertingTest extends RealDbTest {


    @Autowired
    private ClientService clientService;

    @Autowired
    private DemandService demandService;



    // This test should be ignored by default. It should be run only after creating of clean database to fill
    // basic data.
    @Ignore
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    // DO NOT ROLLBACK! We really do want to store some sample data into the database
    @Rollback(false)
    public void createTestDemands() {


        final Demand demand = new Demand();
        demand.setTitle("Title poptavka");
        demand.setType(this.demandService.getDemandType(DemandType.Type.NORMAL.getValue()));
        final BigDecimal price = BigDecimal.valueOf(10000);
        demand.setPrice(price);
        demand.setMaxSuppliers(20);
        demand.setMinRating(99);
        demand.setStatus(DemandStatus.NEW);
        final Date endDate = DateUtils.parseDate("2011-05-01");
        demand.setEndDate(endDate);
        final Date validTo = DateUtils.parseDate("2011-06-01");
        demand.setValidTo(validTo);


        final Client newClient = new Client();
        newClient.getBusinessUser().setEmail("test@poptavam.com");
        final String clientSurname = "Client";
        newClient.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("Test").personLastName(clientSurname).build());
        newClient.getBusinessUser().setSettings(new Settings());
        this.clientService.create(newClient);

        demand.setClient(clientService.getById(newClient.getId()));
        demandService.create(demand);

        final Demand createdDemand = this.demandService.getById(demand.getId());
        Assert.assertNotNull(createdDemand);
        Assert.assertEquals(price, createdDemand.getPrice());
        Assert.assertEquals(DemandStatus.NEW, createdDemand.getStatus());
        Assert.assertEquals(validTo, createdDemand.getValidTo());
        Assert.assertEquals(clientSurname,
                createdDemand.getClient().getBusinessUser().getBusinessUserData().getPersonLastName());
    }
}
