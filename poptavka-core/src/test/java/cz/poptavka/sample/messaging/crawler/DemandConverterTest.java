package cz.poptavka.sample.messaging.crawler;

import cz.poptavka.crawldemands.demand.Demand;
import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.AddressType;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.util.messaging.demand.TestingDemand;
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
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class DemandConverterTest extends DBUnitBaseTest {

    @Autowired
    private Converter<Demand, cz.poptavka.sample.domain.demand.Demand> demandConverter;

    @Test
    public void testConvert() throws Exception {
        final cz.poptavka.sample.domain.demand.Demand domainDemand = this.demandConverter.convert(
                TestingDemand.generateDemands()[0]);
        Assert.assertNotNull(domainDemand);
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_TITLE, domainDemand.getTitle());

        // demand type should not be set - setup of demand type is deffered to the creation time, see
        // DemandServiceImpl#create()
        Assert.assertNull(domainDemand.getType());

        Assert.assertEquals("Demand status for crawled demand must be set to 'TEMPORARY'",
                DemandStatus.TEMPORARY, domainDemand.getStatus());

        // TODO: origin should be filled, but it depends on the crawler
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
        final cz.poptavka.sample.domain.demand.Demand domainDemand = this.demandConverter.convert(attractiveDemand);
        Assert.assertEquals(DemandType.Type.ATTRACTIVE, domainDemand.getType().getType());
    }
}
