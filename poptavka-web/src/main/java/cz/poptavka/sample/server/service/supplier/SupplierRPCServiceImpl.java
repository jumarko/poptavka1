package cz.poptavka.sample.server.service.supplier;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.SupplierRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.Status;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.product.Service;
import cz.poptavka.sample.domain.product.UserService;
import cz.poptavka.sample.domain.settings.Notification;
import cz.poptavka.sample.domain.settings.NotificationItem;
import cz.poptavka.sample.domain.settings.Period;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

public class SupplierRPCServiceImpl extends AutoinjectingRemoteService implements SupplierRPCService {
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 6985305269091931821L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierRPCServiceImpl.class);
    private SupplierService supplierService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private GeneralService generalService;
    private ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Override
    //TODO add description support
    //TODO setService
    public UserDetail createNewSupplier(UserDetail supplier) {
        Supplier newSupplier = new Supplier();
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(supplier.getCompanyName())
                .taxId(supplier.getTaxId())
                .identificationNumber(supplier.getIdentifiacationNumber())
                .phone(supplier.getPhone())
                .personFirstName(supplier.getFirstName())
                .personLastName(supplier.getLastName())
                //.description(supplier.getDescription());
                .build();
        newSupplier.getBusinessUser().setBusinessUserData(businessUserData);
        newSupplier.getBusinessUser().setEmail(supplier.getEmail());
        newSupplier.getBusinessUser().setPassword(supplier.getPassword());
        /** address **/
        Address address = null;
        Locality search = new Locality();
        search.setName(supplier.getAddress().getCityName());
        Locality cityLoc = localityService.findByExample(search).get(0);
        if (cityLoc != null) {
            address = new Address();
            address.setCity(cityLoc);
            address.setStreet(supplier.getAddress().getStreet());
            address.setZipCode(supplier.getAddress().getZipCode());
        }
        List<Address> addresses = new ArrayList<Address>();
        addresses.add(address);
        newSupplier.getBusinessUser().setAddresses(addresses);
        /** localities **/
        List<Locality> locs = new ArrayList<Locality>();
        for (String loc : supplier.getSupplier().getLocalities()) {
            locs.add(getLocalityByExample(loc));
        }
        newSupplier.setLocalities(locs);
        /** categories **/
        List<Category> categories = new ArrayList<Category>();
        for (String cat : supplier.getSupplier().getCategories()) {
            categories.add(getCategoryByExample(cat));
        }
        newSupplier.setCategories(categories);
        /** Service selection **/
        List<UserService> us = new ArrayList<UserService>();

        ArrayList<Integer> userServicesId = supplier.getSupplier().getServices();
        for (Integer serviceId : userServicesId) {
            Service service = generalService.find(Service.class, Long.valueOf(serviceId));
            UserService userService = new UserService();
            userService.setService(service);
            userService.setStatus(Status.INACTIVE);
            userService.setUser(newSupplier.getBusinessUser());
            us.add(userService);
        }
        /** Set service for new client as well **/
        UserService userServiceClient = new UserService();
        userServiceClient.setUser(newSupplier.getBusinessUser());
        userServiceClient.setStatus(Status.INACTIVE);
        userServiceClient.setService(this.generalService.find(Service.class, 4L));
        us.add(userServiceClient);

        newSupplier.getBusinessUser().setUserServices(us);

        /** Notifications for new Supplier role. **/
        List<NotificationItem> notificationItems = new ArrayList<NotificationItem>();
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

        /** assign business role to the new supplier. **/
        newSupplier.getBusinessUser().getBusinessUserRoles().add(newSupplier);
        /** registration process **/
        newSupplier.setVerification(Verification.UNVERIFIED);

        Supplier supplierFromDB = supplierService.create(newSupplier);

        /** Brand new supplier has automatically the role of a client as well. **/
        Client client = new Client();
        client.setBusinessUser(supplierFromDB.getBusinessUser());
        client.getBusinessUser().getBusinessUserRoles().add(client);
        client.setVerification(Verification.UNVERIFIED);

        if (clientService == null) {
            System.out.println("service is null");
        }

        clientService.create(client);

        return this.toUserDetail(supplierFromDB.getBusinessUser().getBusinessUserRoles());
    }

    private Locality getLocalityByExample(String searchString) {
        Locality loc = new Locality();
        loc.setCode(searchString);
        Locality resultLoc = localityService.findByExample(loc).get(0);
        return resultLoc;
    }

    private Category getCategoryByExample(String searchString) {
        Category resultsCat = categoryService.getById(Long.parseLong(searchString));
        return resultsCat;
    }

    @Override
    public ArrayList<ServiceDetail> getSupplierServices() {
        List<Service> services = this.generalService.findAll(Service.class);
        if (services != null) {
            System.out.println("Services count: " + services.size());
        } else {
            System.out.println("NNULLLLLLLL");
        }
        return convertToServiceDetails(services);
    }
}
