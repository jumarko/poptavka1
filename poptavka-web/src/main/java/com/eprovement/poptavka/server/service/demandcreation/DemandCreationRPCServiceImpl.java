/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.demandcreation;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.server.converter.Converter;
import com.google.common.base.Preconditions;
import com.eprovement.poptavka.client.service.demand.DemandCreationRPCService;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This RPC handles all requests for DemandCreation module such as registration of new Client and creating new Demand.
 * <b><i>Note:<\i><\b>
 * RPC class, which creates interface between GWT a Hibernate. It handlers all request from module DemandCreation.
 * All methods for this module will be in this RPC lass.
 * Detail objects can be shared between more RPC services.
 *
 * @author ivlcek
 */
@Configurable
public class DemandCreationRPCServiceImpl extends AutoinjectingRemoteService
        implements DemandCreationRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private DemandService demandService;
    private LocalityService localityService;
    private ClientService clientService;
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;
    private Converter<Locality, ICatLocDetail> localityConverter;
    private Converter<Category, ICatLocDetail> categoryConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
    /**************************************************************************/
    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }


    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setDemandConverter(
            @Qualifier("fullDemandConverter") Converter<Demand, FullDemandDetail> demandConverter) {
        this.demandConverter = demandConverter;
    }

    @Autowired
    public void setBusinessUserConverter(@Qualifier("businessUserConverter")
            Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        this.businessUserConverter = businessUserConverter;
    }

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, ICatLocDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, ICatLocDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    /**************************************************************************/
    /* Business events`                                                       */
    /**************************************************************************/
    /**
     * Create new entity Demand based on the passed FullDemandDetail object.
     *
     * @param detail object created by user in DemandCreation view
     * @param cliendId is used in case the Demand is created by registered and
     * logged in client
     * @return crated demand detail
     */
    @Override
    public FullDemandDetail createNewDemand(FullDemandDetail detail, Long cliendId) throws RPCException {
        final Demand demand = new Demand();
        demand.setTitle(detail.getDemandTitle());
        demand.setDescription(detail.getDescription());
        demand.setType(this.demandService.getDemandType(detail.getDemandType()));
        demand.setPrice(detail.getPrice());
        // if max suppliers has not been specified, default value is used. @See Demand#DEFAULT_MAX_SUPPLIERS
        if (maxOffersSpecified(detail)) {
            demand.setMaxSuppliers(detail.getMaxSuppliers());
        }
        demand.setMinRating(detail.getMinRating());
        demand.setEndDate(detail.getEndDate());
        demand.setValidTo(detail.getValidTo());
        demand.setClient(this.clientService.getById(cliendId));

        /** localities **/
        demand.setLocalities(localityConverter.convertToSourceList(detail.getLocalities()));
        /** categories **/
        demand.setCategories(categoryConverter.convertToSourceList(detail.getCategories()));

        final Demand newDemandFromDB = demandService.create(demand);

        return demandConverter.convertToTarget(newDemandFromDB);
    }

    private boolean maxOffersSpecified(FullDemandDetail detail) {
        return detail.getMaxSuppliers() > 0;
    }

    /**
     * Creates new client.
     * @param clientDetail object
     * @return business user detail
     * @throws RPCException
     */
    @Override
    public BusinessUserDetail createNewClient(BusinessUserDetail clientDetail) throws RPCException {
        Preconditions.checkNotNull(clientDetail);
        final Client newClient = new Client();
        /** Person is mandatory for person client and for company client as well. **/
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(clientDetail.getCompanyName())
                .description(clientDetail.getDescription())
                .personFirstName(clientDetail.getPersonFirstName())
                .personLastName(clientDetail.getPersonLastName())
                .phone(clientDetail.getPhone())
                .identificationNumber(clientDetail.getIdentificationNumber())
                .taxId(clientDetail.getTaxId())
                .website(clientDetail.getWebsite())
                .build();

        newClient.getBusinessUser().setBusinessUserData(businessUserData);

        setAddresses(clientDetail, newClient);

        /** Login & pwd information. **/
        newClient.getBusinessUser().setEmail(clientDetail.getEmail());
        newClient.getBusinessUser().setPassword(clientDetail.getPassword());

        final Client newClientFromDB = clientService.create(newClient);

        return businessUserConverter.convertToTarget(newClientFromDB.getBusinessUser());
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Updates client's addresses.
     * @param clientDetail
     * @param newClient to be updated
     */
    private void setAddresses(BusinessUserDetail clientDetail, Client newClient) {
        final List<Address> addresses = new ArrayList<Address>();
        if (clientDetail.getAddresses() != null) {
            for (AddressDetail detail : clientDetail.getAddresses()) {
                Locality city = localityService.getLocality(detail.getCityId());
                Address address = new Address();
                address.setCity(city);
                address.setStreet(detail.getStreet());
                address.setZipCode(detail.getZipCode());
                addresses.add(address);
            }
        }

        newClient.getBusinessUser().setAddresses(addresses);
    }

    /**
     * Checks mail availability.
     * @param email to be checked
     * @return true if available, false otherwise
     * @throws RPCException
     */
    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }
}
