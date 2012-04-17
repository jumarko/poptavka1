package cz.poptavka.sample.messaging.crawler;

import com.google.common.base.Preconditions;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.crawldemands.demand.Demand;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.AddressType;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.util.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.List;

/**
 * TODO: extract logic related to particular domain object into the separated converters - good example
 * is Client or Supplier.
 *
 * Converter for convert crawled demands objects of type {@link Demand}
 * to domain object {@link cz.poptavka.sample.domain.demand.Demand}.
 *
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
public class DemandConverter implements Converter<Demand, cz.poptavka.sample.domain.demand.Demand> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandConverter.class);

    private static final String STREET_CITY_SEPARATOR = ", ";
    /** Seperates person names - one or more spaces */
    private static final String PERSON_NAMES_SEPARATOR_REGEX = " +";
    private GeneralService generalService;

    private DemandService demandService;
    private LocalityService localityService;

    public DemandConverter(GeneralService generalService, DemandService demandService,
                           LocalityService localityService) {
        this.generalService = generalService;
        this.demandService = demandService;
        this.localityService = localityService;
    }

    @Override
    public cz.poptavka.sample.domain.demand.Demand convert(Demand sourceDemand) {
        if (sourceDemand == null) {
            return null;
        }

        final cz.poptavka.sample.domain.demand.Demand domainDemand = new cz.poptavka.sample.domain.demand.Demand();

        domainDemand.setStatus(DemandStatus.CRAWLED);
        domainDemand.setTitle(sourceDemand.getName());
        domainDemand.setDescription(sourceDemand.getDescription());
        domainDemand.setForeignLink(sourceDemand.getLink());
        setDemandType(sourceDemand, domainDemand);
        // TODO: Crawler must set origin by itself
//        domainDemand.setOrigin(this.demandService.getDemandOrigin("epoptavka.cz"));
        domainDemand.setForeignCategory(sourceDemand.getCategory());
        setLocalities(sourceDemand, domainDemand);

        // TODO: test edge cases - such as January or December
        // TODO: make contract with crawler about stable date format
        // set demand validTo
        setValidTo(sourceDemand, domainDemand);

        // assign client and supplier to the new demand
        setClientAndSupplier(domainDemand, sourceDemand);

        // set verification to EXTERNAL because this demand came from crawler (which downloaded the data from some
        // external system).
        setVerification(domainDemand);

        // set client's overal rating
        domainDemand.getClient().setOveralRating(0);

        Preconditions.checkState(domainDemand.getClient().getBusinessUser() != null,
                "BusinessUser must be already set for new Demand when demand's address is being set.");

        setClientAddress(sourceDemand, domainDemand);


        // set business user's data information
        setBusinessUserData(sourceDemand, domainDemand);


        return domainDemand;

        //----------------------------------  Attributes that are not filled (converted) -------------------------------

        // TODO:  www is not used at this moment
        //  sourceDemand.getWww()


        // clientDescription is not used at this moment
        // sourceDemand.getClientDescription()

        // originalHtml is not interesting for us -> WON'T BE SAVED
//        sourceDemand.getOriginalHtml()

        // demand dateOfCreation is not stored at this time
//        sourceDemand.getDateOfCreation()

        // linkToCommercialRegister is not stored at this time - It is not set by crawler (at least now), either.
//        sourceDemand.getLinkToCommercialRegister()

        // demand endDate is not stored -> it should be ended by supplier
//        sourceDemand.getEndDate()
    }


    //---------------------------------------------- HELPER METHODS ----------------------------------------------------

    private void setClientAndSupplier(cz.poptavka.sample.domain.demand.Demand domainDemand, Demand sourceDemand) {

        final String userEmail = sourceDemand.getEmail();
        Preconditions.checkArgument(StringUtils.isNotBlank(userEmail),
                "User's email must be specified for demand conversion");
        final BusinessUser userByEmail =
                (BusinessUser) generalService.searchUnique(new Search(BusinessUser.class)
                        .addFilterEqual("email", userEmail));

        if (userByEmail != null) {
            // existing business user
            // TODO: make different decisions based on user's role (client or supplier) ?

            for (BusinessUserRole businessUserRole : userByEmail.getBusinessUserRoles()) {
                if (businessUserRole.getClass() == Client.class) {
                    domainDemand.setClient((Client) businessUserRole);
                } else if (businessUserRole.getClass() == Supplier.class) {
                    domainDemand.setSuppliers(Arrays.asList((Supplier) businessUserRole));
                }
            }
        }
        // fill client and supplier if they haven not been filled yet
        if (domainDemand.getClient() == null) {
            final Client client = new Client();
            if (userByEmail != null) {
                client.setBusinessUser(userByEmail);
            }
            if (StringUtils.isBlank(client.getBusinessUser().getEmail())) {
                client.getBusinessUser().setEmail(userEmail);
            }
            domainDemand.setClient(client);
        } else if (CollectionUtils.isEmpty(domainDemand.getSuppliers())) {
            final Supplier supplier = new Supplier();
            if (userByEmail != null) {
                supplier.setBusinessUser(userByEmail);
            }
            if (StringUtils.isBlank(supplier.getBusinessUser().getEmail())) {
                supplier.getBusinessUser().setEmail(userEmail);
            }
            domainDemand.setSuppliers(Arrays.asList(supplier));
        }
    }

    private void setVerification(cz.poptavka.sample.domain.demand.Demand domainDemand) {
        domainDemand.getClient().setVerification(Verification.EXTERNAL);
        if (CollectionUtils.isNotEmpty(domainDemand.getSuppliers())) {
            for (Supplier supplier : domainDemand.getSuppliers()) {
                supplier.setVerification(Verification.EXTERNAL);
            }
        }
    }

    private void setDemandType(Demand sourceDemand, cz.poptavka.sample.domain.demand.Demand domainDemand) {
        if (sourceDemand.getAttractive() != null) {
            // set "attractive" type - otherwise the "normal" type is implicit -> see {@link DemandServiceImpl#create}
            domainDemand.setType(this.demandService.getDemandType(DemandType.Type.ATTRACTIVE.getValue()));
        }
    }

    private void setClientAddress(Demand sourceDemand, cz.poptavka.sample.domain.demand.Demand domainDemand) {
        if (CollectionUtils.isEmpty(domainDemand.getClient().getBusinessUser().getAddresses())) {
            final Address newClientAddress = new Address();
            // TODO: at this moment, all address info from crawler is stored in Address#street field
            // --> extract address information into the more meaningful fields.
            newClientAddress.setStreet(sourceDemand.getStreet() + STREET_CITY_SEPARATOR + sourceDemand.getCity());
            newClientAddress.setAddressType(AddressType.FOREIGN);
            domainDemand.getClient().getBusinessUser().setAddresses(Arrays.asList(newClientAddress));
        }
    }

    private void setBusinessUserData(Demand sourceDemand, cz.poptavka.sample.domain.demand.Demand domainDemand) {
        if (domainDemand.getClient().getBusinessUser().getBusinessUserData() == null) {
            domainDemand.getClient().getBusinessUser().setBusinessUserData(new BusinessUserData());
        }

        final BusinessUserData clientBusinessUserData =
                domainDemand.getClient().getBusinessUser().getBusinessUserData();
        if (StringUtils.isBlank(clientBusinessUserData.getCompanyName())) {
            clientBusinessUserData.setCompanyName(sourceDemand.getCompany());
        }
        if (StringUtils.isBlank(clientBusinessUserData.getIdentificationNumber())) {
            clientBusinessUserData.setIdentificationNumber(sourceDemand.getIco());
        }
        if (StringUtils.isBlank(clientBusinessUserData.getTaxId())) {
            clientBusinessUserData.setTaxId(sourceDemand.getDic());
        }
        if (StringUtils.isBlank(clientBusinessUserData.getPhone())) {
            clientBusinessUserData.setPhone(sourceDemand.getPhone());
        }
        // TODO: parse contact person's names and store them into the separated fields
        if (StringUtils.isBlank(clientBusinessUserData.getPersonLastName())) {
            // try to parse the contact name
            final String contactPerson = sourceDemand.getContactPerson();
            if (StringUtils.isNotBlank(contactPerson)) {
                final String[] personNames = contactPerson.split(PERSON_NAMES_SEPARATOR_REGEX);

                // last name is the last part of person names
                clientBusinessUserData.setPersonLastName(personNames[personNames.length - 1]);

                // first name is any string before last name
                if (personNames.length > 1) {
                    final String[] personFirstNames = Arrays.copyOf(personNames, personNames.length - 1);
                    // join all parts of person first name into the one string
                    clientBusinessUserData.setPersonFirstName(
                            StringUtils.join(personFirstNames, PERSON_NAMES_SEPARATOR_REGEX));
                }
            }
        }
    }

    /**
     * Set localities which correspond to the locality of sourceDemand to the domainDemand.
     */
    private void setLocalities(Demand sourceDemand, cz.poptavka.sample.domain.demand.Demand domainDemand) {
        if (StringUtils.isNotBlank(sourceDemand.getLocality())) {
            // find correct locality (-ies) by its name
            final Search localitySearch = new Search(Locality.class);
            localitySearch.addFilterEqual("name", sourceDemand);
            final List<Locality> localities = (List<Locality>) this.generalService.search(localitySearch);
            if (CollectionUtils.isNotEmpty(localities)) {
                domainDemand.setLocalities(localities);
            } else {
                LOGGER.warn("Locality [" + sourceDemand.getLocality() + "] is not between Poptavka's localities.");
            }
        }
    }

    private void setValidTo(Demand sourceDemand, cz.poptavka.sample.domain.demand.Demand domainDemand) {
        if (StringUtils.isBlank(sourceDemand.getValidTo())) {
            // do not bother to set validTo date becuase it is empty
            return;
        }
        try {
            domainDemand.setValidTo(DateUtils.parseDate(sourceDemand.getValidTo()));
        } catch (IllegalArgumentException iae) {
            // incorrect date format
            LOGGER.warn("Incorrect date format for validTo property: [" + sourceDemand.getValidTo() + "].");
        }
    }

}
