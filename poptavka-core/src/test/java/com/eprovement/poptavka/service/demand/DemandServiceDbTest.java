package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.base.RealDbTest;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.util.date.DateUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
public class DemandServiceDbTest extends RealDbTest {

    @Autowired
    private DemandService demandService;

    @Autowired
    private ClientService clientService;


    @Test
    @Ignore // do not use live DB if it is not neccessary
    @Transactional(propagation = Propagation.REQUIRED)
    public void testCreateDemand() {
        final Demand demand = new Demand();
        demand.setTitle("TEST poptavka");
        demand.setType(this.demandService.getDemandType(DemandTypeType.NORMAL.getValue()));
        demand.setPrice(BigDecimal.valueOf(10000));
        demand.setMaxSuppliers(20);
        demand.setMinRating(99);
        demand.setStatus(DemandStatus.NEW);
        demand.setEndDate(DateUtils.parseDate("2011-05-01"));
        demand.setValidTo(DateUtils.parseDate("2011-06-01"));

        demand.setClient(createNewClient());
        demandService.create(demand);
    }

    private Client createNewClient() {
        final Client newClient = new Client();
        newClient.getBusinessUser().setEmail("test@poptavam.com");
        newClient.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("Test").personLastName("Client").build());
        return this.clientService.create(newClient);
    }

}
