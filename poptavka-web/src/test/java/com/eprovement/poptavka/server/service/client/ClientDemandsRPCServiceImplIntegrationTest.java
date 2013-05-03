package com.eprovement.poptavka.server.service.client;

import com.eprovement.poptavka.base.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaAuthenticationProvider;
import com.eprovement.poptavka.server.service.clientdemands.ClientDemandsModuleRPCServiceImpl;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.LoginService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDashboardDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.googlecode.genericdao.search.Search;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Juraj Martinka
 *         Date: 20.12.10
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/message/OfferMessageDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/offer/OfferDataSet.xml" },
        dtd = "classpath:test.dtd",
        disableForeignKeyChecks = true)
public class ClientDemandsRPCServiceImplIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    @Qualifier("clientDemandConverter")
    private Converter<Demand, ClientDemandDetail> clientDemandConverter;

    @Autowired
    @Qualifier("fullDemandConverter")
    private Converter<Demand, FullDemandDetail> demandConverter;

    @Autowired
    @Qualifier("searchConverter")
    private Converter<Search, SearchDefinition> searchConverter;

    @Autowired
    @Qualifier("supplierConverter")
    private Converter<Supplier, FullSupplierDetail> supplierConverter;

    @Autowired
    @Qualifier("messageConverter")
    private Converter<Message, MessageDetail> messageConverter;

    @Autowired
    private ClientService clientService;

    @Autowired
    private GeneralService generalService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private LoginService loginService;


    private ClientDemandsModuleRPCServiceImpl clientDemandsRPCService;


    @Before
    public void setUp() {

        authenticateClient();

        clientDemandsRPCService = new ClientDemandsModuleRPCServiceImpl();
        // converters
        clientDemandsRPCService.setClientDemandConverter(clientDemandConverter);
        clientDemandsRPCService.setDemandConverter(demandConverter);
        clientDemandsRPCService.setSearchConverter(searchConverter);
        clientDemandsRPCService.setMessageConverter(messageConverter);
        clientDemandsRPCService.setSupplierConverter(supplierConverter);

        // services
        clientDemandsRPCService.setClientService(clientService);
        clientDemandsRPCService.setGeneralService(generalService);
        clientDemandsRPCService.setMessageService(messageService);
        clientDemandsRPCService.setUserMessageService(userMessageService);
        clientDemandsRPCService.setOfferService(offerService);

    }

    private void authenticateClient() {
        // TODO RELEASE juraj: authenticate correctly as client - make common methods in abstract parent
        // setup correct roles in UsersDataSet.xml
        final PoptavkaAuthenticationProvider authenticationProvider = new PoptavkaAuthenticationProvider(loginService);
        final Authentication clientAuthentication = authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken("elvira@email.com", "ahoj1"));
        SecurityContextHolder.getContext().setAuthentication(clientAuthentication);
    }


    @Test
    public void testGetAllClientDemands() {
        final List<ClientDemandDetail> allClientDemands = clientDemandsRPCService.getClientDemands(111111112L, null);
        assertNotNull(allClientDemands);
        // check that there is only one new demand - see DemandsDataSet.xml
        assertThat(allClientDemands.size(), is(4));
    }

    @Test
    public void testGetClientOfferedDemandOffers() {
        // final List<FullOfferDetail> allClientProjects = clientDemandsRPCService.getClientProjects(111111111L, null);
        SearchDefinition searchDefinition = new SearchDefinition();
        long count = clientDemandsRPCService.getClientOfferedDemandOffersCount(111111112L, 2L, searchDefinition);
        Assert.assertEquals("getClientOfferedDemandOffersCount [count=" + count + "] was not correct",
                Long.valueOf(3L), Long.valueOf(count));
        List<ClientOfferedDemandOffersDetail> offers = clientDemandsRPCService.getClientOfferedDemandOffers(
                111111112L, 2L, 1L, searchDefinition);
        assertThat(offers.size(), is(3));
        Assert.assertEquals("getClientOfferedDemandOffers size [size=" + offers.size() + "] was not correct",
                3, offers.size());

        checkClientOfferedDemandOffersDetailExists(offers, 11, 3);
        checkClientOfferedDemandOffersDetailExists(offers, 11, 7);
        checkClientOfferedDemandOffersDetailExists(offers, 12, 9);
        searchDefinition.setFilter(new SearchModuleDataHolder());
        searchDefinition.getFilter().setSearchText("Fourth");
        offers = clientDemandsRPCService.getClientOfferedDemandOffers(111111112L, 2L, 1L, searchDefinition);
        assertThat(offers.size(), is(3));
    }

    @Test
    public void testAcceptOffer() {
        long demandId = 2;
        long offerToBeAccepted = 12;
        Demand demand = generalService.find(Demand.class, demandId);
        Assert.assertEquals("Number of offers [offers.size="
                + demand.getOffers() + "] for given demand [demandId="
                + demandId + "] is not as expected", 1, demand.getOffers().size());
        for (Offer offer : demand.getOffers()) {
            Assert.assertThat("Unexpected offer state for offer.id=" + offer.getId(),
                    OfferStateType.PENDING.getValue(), is(offer.getState().getCode()));
        }
        clientDemandsRPCService.acceptOffer(offerToBeAccepted);

        for (Offer offer : demand.getOffers()) {
            if (offer.getId().longValue() != offerToBeAccepted) {
                Assert.assertEquals("Offer status [offer.state.code=" + offer.getState().getCode()
                        + "] for offer [offer.id=" + offer.getId() + "] is not in DECLINED state",
                        OfferStateType.DECLINED.getValue(), offer.getState().getCode());
            } else {
                Assert.assertEquals("Offer status [offer.state.code=" + offer.getState().getCode()
                        + "] for offer [offer.id=" + offer.getId() + "] is not in ACCEPTED state",
                        OfferStateType.ACCEPTED.getValue(), offer.getState().getCode());
            }
        }
    }

    @Test
    public void testGetClientDashboardDetail() {
        long userId = 111111112;
        ClientDashboardDetail dashboard = clientDemandsRPCService.getClientDashboardDetail(userId);
        Assert.assertEquals("Expected number of unread messages for myDemands doesn't match the result", 1,
                dashboard.getUnreadMessagesMyDemandsCount());
        Assert.assertEquals("Expected number of unread messages for Offered Demands doesn't match the result", 1,
                dashboard.getUnreadMessagesOfferedDemandsCount());
        Assert.assertEquals("Expected number of unread messages for Assigned Demands doesn't match the result", 0,
                dashboard.getUnreadMessagesAssignedDemandsCount());
        Assert.assertEquals("Expected number of unread messages for Closed Demands doesn't match the result", 0,
                dashboard.getUnreadMessagesClosedDemandsCount());
    }

    private void checkFullOfferDetailExists(List<FullOfferDetail> offers,
            final long offerId, final long userMessageId) {
        Assert.assertTrue(
                "FullOfferDetail [offerId=" + offerId + ", userMessageId ="
                        + userMessageId + "] expected to be in collection ["
                + offers + "] is not there.",
                CollectionUtils.exists(offers, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        return offerId == ((FullOfferDetail) object).getOfferDetail().getId()
                                && userMessageId == ((FullOfferDetail) object).getUserMessageId();
                    }
                }));
    }

    private void checkClientOfferedDemandOffersDetailExists(List<ClientOfferedDemandOffersDetail> offers,
            final long offerId, final long userMessageId) {
        Assert.assertTrue(
                "ClientOfferedDemandOffersDetail [offerId=" + offerId + ", userMessageId ="
                        + userMessageId + "] expected to be in collection ["
                + offers + "] is not there.",
                CollectionUtils.exists(offers, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        return offerId == ((ClientOfferedDemandOffersDetail) object).getOfferId();
                    }
                }));
    }
}
