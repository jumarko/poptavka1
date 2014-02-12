/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.suppliercreation;

import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.client.service.demand.SupplierCreationRPCService;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This RPC handles all requests from SupplierCreation module such as create and register new supplier.
 *
 * @author Praso
 * TODO LATER ivlcek: vytvorit predka pre checkFreeEmail if possible, optimalizovat backend
 */
@Configurable
public class SupplierCreationRPCServiceImpl extends AutoinjectingRemoteService implements SupplierCreationRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private SupplierService supplierService;
    private ClientService clientService;
    private GeneralService generalService;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Locality, ICatLocDetail> localityConverter;
    private Converter<Category, ICatLocDetail> categoryConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
    /**************************************************************************/
    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setBusinessUserConverter(
            @Qualifier("supplierConverter") Converter<Supplier, FullSupplierDetail> supplierConverter) {
        this.supplierConverter = supplierConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, ICatLocDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, ICatLocDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Create new supplier from detail object constructed by SupplierCreation forms. New Supplier automatically
     * contains a Client role so that he can create new Demands.
     *
     * TODO LATER - Ak uz existuje Klient tj User a BusinessUser
     * tak musime pridat kontrolu a vytvarat len rolu Dodavatela pre tohto existujuceho
     * Usera v resp. BusinessUsera.
     *
     * @param supplier
     * @return
     */
    @Override
    public FullSupplierDetail createNewSupplier(FullSupplierDetail supplier) throws RPCException {
        final Supplier newSupplier = new Supplier();
        setNewSupplierBusinessUserData(supplier.getUserData(), newSupplier);
        newSupplier.getBusinessUser().setEmail(supplier.getUserData().getEmail());
        newSupplier.getBusinessUser().setPassword(supplier.getUserData().getPassword());

        setNewSupplierAddresses(supplier.getUserData(), newSupplier);
        setNewSupplierLocalities(supplier, newSupplier);
        setNewSupplierCategories(supplier, newSupplier);
        setNewSupplierUserServices(supplier, newSupplier);
        assignBusinessRoleToNewSupplier(newSupplier);
        newSupplier.setOveralRating(0);
        /** registration process **/
        final Supplier supplierFromDB = supplierService.create(newSupplier);

        /** Brand new Supplier has automatically the role of a Client as well. **/
        // TODO LATER vojto - Client creation should be moved to one client block.
        // TODO LATER vojto - Supplier creation should be in Supplier block. Zgrupovat kod!
        final Client client = new Client();
        client.setBusinessUser(supplierFromDB.getBusinessUser());
        client.getBusinessUser().getBusinessUserRoles().add(client);

        clientService.create(client);

        return supplierConverter.convertToTarget(supplierFromDB);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Fills supplier's business user data with given business user detail using BusinessUserData.Builder().
     * @param supplier to be update
     * @param newSupplier
     */
    private void setNewSupplierBusinessUserData(BusinessUserDetail supplier, Supplier newSupplier) {
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(supplier.getCompanyName())
                .description(supplier.getDescription())
                .personFirstName(supplier.getPersonFirstName())
                .personLastName(supplier.getPersonLastName())
                .phone(supplier.getPhone())
                .identificationNumber(supplier.getIdentificationNumber())
                .taxId(supplier.getTaxId())
                .website(supplier.getWebsite())
                .build();
        newSupplier.getBusinessUser().setBusinessUserData(businessUserData);
    }

    /**
     * Fills supplier's addresses with given business user detail.
     * @param supplier
     * @param newSupplier
     */
    private void setNewSupplierAddresses(BusinessUserDetail supplier, Supplier newSupplier) {
        final List<Address> addresses = getAddressesFromSupplierCityName(supplier);

        newSupplier.getBusinessUser().setAddresses(addresses);
    }

    /**
     * Fills supplier's services with given supplier detail.
     * @param supplier
     * @param newSupplier
     */
    private void setNewSupplierUserServices(FullSupplierDetail supplier, Supplier newSupplier) {
        final List<UserService> us = new ArrayList<>();

        for (ServiceDetail serviceDetail : supplier.getServices()) {
            Service service = generalService.find(Service.class, serviceDetail.getId());
            UserService userService = new UserService();
            userService.setService(service);
            userService.setStatus(Status.INACTIVE);
            userService.setUser(newSupplier.getBusinessUser());
            us.add(userService);
        }

        newSupplier.getBusinessUser().setUserServices(us);
    }

    /**
     * Fills supplier's categories with given supplier detail.
     * @param supplier
     * @param newSupplier
     */
    private void setNewSupplierCategories(FullSupplierDetail supplier, Supplier newSupplier) {
        newSupplier.setCategories(categoryConverter.convertToSourceList(supplier.getCategories()));
    }

    /**
     * Fills supplier's localities with given supplier detail.
     * @param supplier
     * @param newSupplier
     */
    private void setNewSupplierLocalities(FullSupplierDetail supplier, Supplier newSupplier) {
        newSupplier.setLocalities(localityConverter.convertToSourceList(supplier.getLocalities()));
    }


    /**
     * Assigns business role to new supplier.
     * @param newSupplier role
     */
    private void assignBusinessRoleToNewSupplier(Supplier newSupplier) {
        newSupplier.getBusinessUser().getBusinessUserRoles().add(newSupplier);
    }

    /**
     * Get addresses from supplier city name.
     * @param supplier object
     * @return list of addresses
     */
    private List<Address> getAddressesFromSupplierCityName(BusinessUserDetail supplier) {
        final List<Address> addresses = new ArrayList<>();
        for (AddressDetail detail : supplier.getAddresses()) {
            //Ziskaj mesto typu Locality (String -> Locality)
            final Locality cityLoc = generalService.find(Locality.class, detail.getCityId());

            final Address address = new Address();
            address.setCity(cityLoc);
            address.setStreet(detail.getStreet());
            address.setHouseNum(detail.getHouseNum());
            address.setZipCode(detail.getZipCode());
            addresses.add(address);
        }
        return addresses;
    }
}
