package cz.poptavka.sample.messaging.crawler;

import cz.poptavka.crawldemands.demand.Demand;
import cz.poptavka.sample.base.integration.BasicIntegrationTest;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.util.messaging.demand.TestingDemand;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
public class DemandConverterTest extends BasicIntegrationTest {

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

        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_LINK, domainDemand.getForeignLink());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_DESCRIPTION, domainDemand.getDescription());

        // check address - street and city from source are stored into the street field in target domain demand
        Assert.assertEquals(1, businessUser.getAddresses().size());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_STREET + ", " + TestingDemand.TEST_DEMAND_1_CITY,
                businessUser.getAddresses().get(0).getStreet());

        // properties that should not be converted (at least now)
        Assert.assertNull(domainDemand.getEndDate());
    }
}
