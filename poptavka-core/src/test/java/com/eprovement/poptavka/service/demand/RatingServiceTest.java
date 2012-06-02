package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.Before;

/**
 * @author Vojtech Hubr
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml" },
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
