package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.SupplierService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.Before;

/**
 * @author Vojtech Hubr
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/RatingDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class RatingServiceTest extends DBUnitBaseTest {
    private Client client11, client12, client13, client14;
    private Supplier supplier11, supplier14;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private SupplierService supplierService;

    @Before
    public void setUp() {
        client11 = clientService.getById(111111111L);
        client12 = clientService.getById(111111112L);
        client13 = clientService.getById(111111113L);
        client14 = clientService.getById(111111114L);

        supplier11 = supplierService.getById(1111111111L);
        supplier14 = supplierService.getById(1111111114L);
    }

    @Test
    public void getAvgRatingClientTest() {
        Integer result = ratingService.getAvgRating(client11);
        Assert.assertEquals((Integer) 98, result);

        result = ratingService.getAvgRating(client13);
        Assert.assertEquals((Integer) 60, result);
    }

    @Test
    public void getAvgRatingSupplierTest() {
        Integer result = ratingService.getAvgRating(supplier11);
        Assert.assertEquals((Integer) 90, result);

        result = ratingService.getAvgRating(supplier14);
        Assert.assertEquals((Integer) 40, (Integer) result);
    }

}
