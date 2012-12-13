/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.suppliercreation;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.server.converter.Converter;
import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.client.service.demand.SupplierCreationRPCService;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Praso
 * TODO praso - pridat komenty, vytvorit predka pre checkFreeEmail, optimalizovat backend
 */
@Configurable
public class SupplierCreationRPCServiceImpl extends AutoinjectingRemoteService implements SupplierCreationRPCService {

    private SupplierService supplierService;
    private ClientService clientService;
    private GeneralService generalService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;
    private Converter<Category, CategoryDetail> categoryConverter;

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
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setBusinessUserConverter(
            @Qualifier("businessUserConverter") Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        this.businessUserConverter = businessUserConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    /**
     * Metoda vytvara uzivatela -> BusinessUsera -> rolu klienta a dodavatela pretoze
     * kazdy dodavatel moze byt aj klient.
     *
     * TODO Vojto - Ak uz existuje Klient tj User a BusinessUser
     * tak musime pridat kontrolu a vytvarat len rolu Dodavatela pre tohto existujuceho
     * Usera v resp. BusinessUsera.
     *
     * @param supplier
     * @return
     */
    @Override
    public BusinessUserDetail createNewSupplier(BusinessUserDetail supplier) throws RPCException {
        final Supplier newSupplier = new Supplier();
        setNewSupplierBusinessUserData(supplier, newSupplier);
        newSupplier.getBusinessUser().setEmail(supplier.getEmail());
        newSupplier.getBusinessUser().setPassword(supplier.getPassword());

        setNewSupplierAddresses(supplier, newSupplier);
        setNewSupplierLocalities(supplier, newSupplier);
        setNewSupplierCategories(supplier, newSupplier);
        setNewSupplierUserServices(supplier, newSupplier);
        setNewSuppliersNotificationItems(newSupplier);
        assignBusinessRoleToNewSupplier(newSupplier);
        /** registration process **/
        // TODO - verification will be set after activation link has been received
        final Supplier supplierFromDB = supplierService.create(newSupplier);

        // TODO jumar - WTF ? why new supplier is also a new client?!?
        /** Brand new supplier has automatically the role of a client as well. **/
        // TODO VOjto - Client creation should be moved to one client block.
        // SUpplier creation should be in Supplier block. Zgrupovat kod!
        final Client client = new Client();
        client.setBusinessUser(supplierFromDB.getBusinessUser());
        client.getBusinessUser().getBusinessUserRoles().add(client);

        clientService.create(client);

        return businessUserConverter.convertToTarget(supplierFromDB.getBusinessUser());
    }

    private void setNewSupplierBusinessUserData(BusinessUserDetail supplier, Supplier newSupplier) {
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(supplier.getCompanyName())
                .taxId(supplier.getTaxId())
                .identificationNumber(supplier.getIdentificationNumber())
                .phone(supplier.getPhone()).personFirstName(supplier.getFirstName())
                .personLastName(supplier.getLastName()) //.description(supplier.getDescription());
                .build();
        newSupplier.getBusinessUser().setBusinessUserData(businessUserData);
    }

    private void setNewSupplierAddresses(BusinessUserDetail supplier, Supplier newSupplier) {
        final List<Address> addresses = getAddressesFromSupplierCityName(supplier);

        newSupplier.getBusinessUser().setAddresses(addresses);
    }

    private void setNewSuppliersNotificationItems(Supplier newSupplier) {
        final List<NotificationItem> notificationItems = new ArrayList<NotificationItem>();
        NotificationItem notificationItem = new NotificationItem();
        // TODO ivlcek - create constant for Notifications in DB
        notificationItem.setNotification(this.generalService.find(Notification.class, 6L));
        notificationItem.setEnabled(true);
        notificationItem.setPeriod(Period.INSTANTLY);
        notificationItems.add(notificationItem);
        /** This is notification for Client role that is automatically created herein. **/
        NotificationItem notificationItemClient = new NotificationItem();
        // TODO ivlcek - create constant for Notifications in DB
        notificationItemClient.setNotification(this.generalService.find(Notification.class, 10L));
        notificationItemClient.setEnabled(true);
        notificationItemClient.setPeriod(Period.INSTANTLY);
        notificationItems.add(notificationItemClient);
        newSupplier.getBusinessUser().getSettings().setNotificationItems(notificationItems);
    }

    private void setNewSupplierUserServices(BusinessUserDetail supplier, Supplier newSupplier) {
        final List<UserService> us = new ArrayList<UserService>();

        ArrayList<Integer> userServicesId = supplier.getSupplier().getServices();
        for (Integer serviceId : userServicesId) {
            Service service = generalService.find(Service.class, Long.valueOf(serviceId));
            UserService userService = new UserService();
            userService.setService(service);
            userService.setStatus(Status.INACTIVE);
            userService.setUser(newSupplier.getBusinessUser());
            us.add(userService);
        }

        newSupplier.getBusinessUser().setUserServices(us);
    }

    private void setNewSupplierCategories(BusinessUserDetail supplier, Supplier newSupplier) {
        newSupplier.setCategories(categoryConverter.convertToSourceList(supplier.getSupplier().getCategories()));
    }

    private void setNewSupplierLocalities(BusinessUserDetail supplier, Supplier newSupplier) {
        final List<Locality> localities = new ArrayList<Locality>();
        for (LocalityDetail localityDetail : supplier.getSupplier().getLocalities()) {
            localities.add(this.getLocality(localityDetail.getCode()));
        }
        newSupplier.setLocalities(localities);
    }

    private void assignBusinessRoleToNewSupplier(Supplier newSupplier) {
        newSupplier.getBusinessUser().getBusinessUserRoles().add(newSupplier);
    }

    private List<Address> getAddressesFromSupplierCityName(BusinessUserDetail supplier) {
        List<Address> addresses = new ArrayList<Address>();
        Search addrSearch;
        for (AddressDetail detail : supplier.getAddresses()) {
            //Ziskaj mesto typu Locality (String -> Locality)
            Locality cityLoc = (Locality) generalService.searchUnique(
                    new Search(Locality.class)
                    .addFilterEqual("name", detail.getCity())
                        .addFilterEqual("type", LocalityType.CITY));

            //Zisti, ci sa taka adresa nachadza v DB.
            addrSearch = new Search(Address.class);
            //ktore lepsie pouzit?
//            addrSearch.addFilterEqual("city.name", detail.getCity());
            addrSearch.addFilterEqual("city", cityLoc);
            addrSearch.addFilterEqual("street", detail.getStreet());
            addrSearch.addFilterEqual("houseNum", detail.getHouseNum());
            Address existingAddress = (Address) generalService.searchUnique(addrSearch);

            if (existingAddress == null) {
                //Ak sa taka adresa nenachadza v DB, vytvor ju
                Address address = new Address();
                address.setCity(cityLoc);
                address.setStreet(detail.getStreet());
                address.setHouseNum(detail.getHouseNum());
                address.setZipCode(detail.getZipCode());
                addresses.add(address);
            } else {
                //Ak ano, prirad ju
                addresses.add(existingAddress);
            }
        }
        return addresses;
    }

    // TODO FIX this, it's not working nullPointerException.
    public Locality getLocality(String code) throws RPCException {
        System.out.println("Locality code value: " + code + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(code);
//        return localityService.getById(10);
    }

    public Category getCategory(String id) throws RPCException {
        return categoryService.getById(Long.parseLong(id));
    }

    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }
}
