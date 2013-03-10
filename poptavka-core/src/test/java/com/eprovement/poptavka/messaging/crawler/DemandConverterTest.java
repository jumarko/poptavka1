package com.eprovement.poptavka.messaging.crawler;

import com.eprovement.crawldemands.demand.Demand;
import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.enums.AddressType;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.util.messaging.demand.TestingDemand;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class DemandConverterTest extends DBUnitIntegrationTest {

    @Autowired
    private Converter<Demand, com.eprovement.poptavka.domain.demand.Demand> demandConverter;

    @Test
    public void testConvert() throws Exception {
        final com.eprovement.poptavka.domain.demand.Demand domainDemand = this.demandConverter.convert(
                TestingDemand.generateDemands()[0]);
        Assert.assertNotNull(domainDemand);
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_TITLE, domainDemand.getTitle());

        // demand type should not be set - setup of demand type is deffered to the creation time, see
        // DemandServiceImpl#create()
        Assert.assertNull(domainDemand.getType());

        Assert.assertEquals("Demand status for crawled demand must be set to 'TEMPORARY'",
                DemandStatus.CRAWLED, domainDemand.getStatus());

        // TODO LATER: origin should be filled, but it depends on the crawler
//        Assert.assertNotNull("Origin should be filled for crawled demand", domainDemand.getOrigin());

        Assert.assertNotNull(domainDemand.getClient());
        final BusinessUser businessUser = domainDemand.getClient().getBusinessUser();
        Assert.assertNotNull(businessUser);
        Assert.assertNotNull(businessUser.getBusinessUserData());

        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_EMAIL, businessUser.getEmail());
        Assert.assertEquals(Verification.EXTERNAL, domainDemand.getClient().getVerification());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_CONTACT_PERSON.split(" ")[1],
                businessUser.getBusinessUserData().getPersonLastName());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_CONTACT_PERSON.split(" ")[0],
                businessUser.getBusinessUserData().getPersonFirstName());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_PHONE, businessUser.getBusinessUserData().getPhone());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_COMPANY, businessUser.getBusinessUserData().getCompanyName());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_DIC, businessUser.getBusinessUserData().getTaxId());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_IC,
                businessUser.getBusinessUserData().getIdentificationNumber());

        // check address
        Assert.assertTrue("External client for crawled demand must have at least one address",
                CollectionUtils.isNotEmpty(businessUser.getAddresses()));
        for (Address address : businessUser.getAddresses()) {
            Assert.assertEquals("Type of client address for crawled demand must be 'FOREIGN'",
                    AddressType.FOREIGN, address.getAddressType());
        }

        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_LINK, domainDemand.getForeignLink());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_DESCRIPTION, domainDemand.getDescription());

        // check address - street and city from source are stored into the street field in target domain demand
        Assert.assertEquals(1, businessUser.getAddresses().size());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_STREET + ", " + TestingDemand.TEST_DEMAND_1_CITY,
                businessUser.getAddresses().get(0).getStreet());

        // properties that should not be converted (at least now)
        Assert.assertNull(domainDemand.getEndDate());
    }


    @Test
    public void testConvertAttractiveDemand() throws Exception {
        final Demand attractiveDemand = TestingDemand.generateDemands()[0];
        attractiveDemand.setAttractive("attractive");
        final com.eprovement.poptavka.domain.demand.Demand domainDemand =
                this.demandConverter.convert(attractiveDemand);
        Assert.assertEquals(DemandTypeType.ATTRACTIVE, domainDemand.getType().getType());
    }
}
