package cz.poptavka.sample.server.service.supplier;

import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchDataHolder;
import cz.poptavka.sample.client.service.demand.SupplierRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.common.Status;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.product.Service;
import cz.poptavka.sample.domain.product.UserService;
import cz.poptavka.sample.domain.settings.Notification;
import cz.poptavka.sample.domain.settings.NotificationItem;
import cz.poptavka.sample.domain.settings.Period;
import cz.poptavka.sample.domain.user.BusinessType;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.SupplierCategory;
import cz.poptavka.sample.domain.user.SupplierLocality;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.common.TreeItemService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.shared.domain.AddressDetail;
import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.UserDetail.Role;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SupplierRPCServiceImpl extends AutoinjectingRemoteService implements SupplierRPCService {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 6985305269091931821L;
    private SupplierService supplierService;
    private GeneralService generalService;
    private ClientService clientService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private TreeItemService treeItemService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
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
    public void setTreeItemService(TreeItemService treeItemService) {
        this.treeItemService = treeItemService;
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
    public UserDetail createNewSupplier(UserDetail supplier) {
        Supplier newSupplier = new Supplier();
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(supplier.getCompanyName()).taxId(supplier.getTaxId())
                .identificationNumber(supplier.getIdentificationNumber())
                .phone(supplier.getPhone()).personFirstName(supplier.getFirstName())
                .personLastName(supplier.getLastName()) //.description(supplier.getDescription());
                .build();
        newSupplier.getBusinessUser().setBusinessUserData(businessUserData);
        newSupplier.getBusinessUser().setEmail(supplier.getEmail());
        newSupplier.getBusinessUser().setPassword(supplier.getPassword());
        /** address **/
        //get locality according to City Name
        List<Address> addresses = new ArrayList<Address>();
        for (AddressDetail detail : supplier.getAddresses()) {
            Locality cityLoc = (Locality) generalService.searchUnique(
                    new Search(Locality.class).addFilterEqual("name", detail.getCityName()));

            if (cityLoc != null) {
                Address address = new Address();
                address.setCity(cityLoc);
                address.setStreet(detail.getStreet());
                address.setZipCode(detail.getZipCode());
                addresses.add(address);
            }
        }

        newSupplier.getBusinessUser().setAddresses(addresses);
        /** localities **/
        List<Locality> locs = new ArrayList<Locality>();
        for (String localityCode : supplier.getSupplier().getLocalities()) {
            locs.add(this.getLocality(localityCode));
        }
        newSupplier.setLocalities(locs);
        /** categories **/
        List<Category> categories = new ArrayList<Category>();
        for (String categoryId : supplier.getSupplier().getCategories()) {
            categories.add(this.getCategory(categoryId));
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
        // TODO nacitanie hodnot z ciselnikov
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
        // TODO - verification will be set after activation link has been received
        newSupplier.setVerification(Verification.UNVERIFIED);

        Supplier supplierFromDB = supplierService.create(newSupplier);

        /** Brand new supplier has automatically the role of a client as well. **/
        // TODO VOjto - Client creation should be moved to one client block.
        // SUpplier creation should be in Supplier block. Zgrupovat kod!
        Client client = new Client();
        client.setBusinessUser(supplierFromDB.getBusinessUser());
        client.getBusinessUser().getBusinessUserRoles().add(client);
        client.setVerification(Verification.UNVERIFIED);

        // TODO Vojto remove this bullshit. It doesn't do anything
        if (clientService == null) {
            System.out.println("service is null");
        }

        clientService.create(client);

        // TODO Beho+Vojto rework according to GWT Spring Security function.
        return this.toUserDetail(supplierFromDB.getBusinessUser().getId(),
                supplierFromDB.getBusinessUser().getBusinessUserRoles());
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

    // TODO FIX this, it's not working nullPointerException.
    public Locality getLocality(String code) {
        System.out.println("Locality code value: " + code + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(code);
//        return localityService.getById(10);
    }

    public Category getCategory(String id) {
        return categoryService.getById(Long.parseLong(id));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID, String localityCode) {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();
        Category[] categories = {categoryService.getById(categoryID)};
        Locality[] localities = {localityService.getLocality(localityCode)};
        return this.createSupplierDetailList(supplierService.getSuppliers(
                resultCriteria, categories, localities));
//                this.getAllSubcategories(categoryID),
//                this.getAllSublocalities(localityCode)));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID) {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();

        return this.createSupplierDetailList(supplierService.getSuppliers(
                resultCriteria, categoryService.getById(categoryID)));
//                this.getAllSubcategories(categoryID)));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSuppliers(int start, int count) {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();

        return this.createSupplierDetailList(supplierService.getAll(resultCriteria));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns) {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(start).maxResults(count).orderByColumns(orderColumns).build();
        return this.createSupplierDetailList(supplierService.getAll(resultCriteria));
    }

    /**
     * Get All suppliers count.
     */
    @Override
    public Integer getSuppliersCount() {
        return (int) supplierService.getCount();
    }

    @Override
    public Long getSuppliersCount(Long categoryID) {
        return supplierService.getSuppliersCountQuick(categoryService.getById(categoryID));
//        return supplierService.getSuppliersCount(this.getAllSubcategories(categoryID));
    }

    @Override
    public Long getSuppliersCount(Long categoryID, String localityCode) {
        return supplierService.getSuppliersCount(
                new Category[]{categoryService.getById(categoryID)},
                new Locality[]{localityService.getLocality(localityCode)});
    }

    /**
     * Method updates supplier object in database.
     *
     * @param supplierDetail - updated supplierDetail from front end
     * @param updateWhat - define data to update. Values: supplier, categories, localities, address, userdata, all.
     * @return supplierDetail
     */
    @Override
    public FullSupplierDetail updateSupplier(FullSupplierDetail supplierDetail) {
        Supplier supplier = supplierService.getById(supplierDetail.getSupplierId());

        //Supplier
        if (supplierDetail.getOverallRating() == -1) {
            supplier.setOveralRating(null);
        } else {
            supplier.setOveralRating(supplierDetail.getOverallRating());
        }
        supplier.setCertified(supplierDetail.isCertified());
        supplier.setVerification(Verification.valueOf(supplierDetail.getVerification()));

        // -- categories
        List<Category> newCategories = new ArrayList<Category>();
        for (Category category : supplier.getCategories()) {
            if (supplierDetail.getCategories().containsKey(category.getId())) {
                //add category - if there already is data, don't go to DB
                newCategories.add(category);
                //remove if added, the rest will be obtained from DB
                supplierDetail.getCategories().remove(category.getId());
            }
        }
        for (Long id : supplierDetail.getCategories().keySet()) {
            newCategories.add(categoryService.getById(id));
        }
        // -- localities
        List<Locality> newLocalities = new ArrayList<Locality>();
        for (Locality locality : supplier.getLocalities()) {
            if (supplierDetail.getLocalities().containsKey(locality.getCode())) {
                newLocalities.add(locality);
                supplierDetail.getLocalities().remove(locality.getCode());
            }
        }
        for (String code : supplierDetail.getLocalities().keySet()) {
            newLocalities.add(localityService.getLocality(code));
        }

        //TODO Martin - how to update addresses???
        List<Address> newAddresses = new ArrayList<Address>();
        for (AddressDetail addr : supplierDetail.getAddresses()) {
//            Address address = new Address();
//            supplier.getBusinessUser().getAddresses()
        }
//        supplier.getBusinessUser().getBusinessUserData().
//        descriptionBox.setText(supplier.getDescription());

        //Busines data
        supplier.getBusinessUser().setEmail(supplierDetail.getEmail());

        supplier.getBusinessUser().getBusinessUserData().setDescription(supplierDetail.getDescription());
        if (supplierDetail.getBusinessType() != null && !supplierDetail.getBusinessType().equals("")) {
            supplier.getBusinessUser().setBusinessType(BusinessType.valueOf(supplierDetail.getBusinessType()));
        }
        supplier.getBusinessUser().getBusinessUserData().setCompanyName(supplierDetail.getCompanyName());
        supplier.getBusinessUser().getBusinessUserData().
                setIdentificationNumber(supplierDetail.getIdentificationNumber());

        //-- Contact
        supplier.getBusinessUser().getBusinessUserData().setPersonFirstName(supplierDetail.getFirstName());
        supplier.getBusinessUser().getBusinessUserData().setPersonLastName(supplierDetail.getLastName());
        supplier.getBusinessUser().getBusinessUserData().setPhone(supplierDetail.getPhone());

        supplierService.update(supplier);
        return supplierDetail;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailList(Collection<Supplier> suppliers) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (Supplier supplier : suppliers) {
            userDetails.add(FullSupplierDetail.createFullSupplierDetail(supplier));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailListCat(Collection<SupplierCategory> suppliersCat) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierCategory supplierCat : suppliersCat) {
            userDetails.add(FullSupplierDetail.createFullSupplierDetail(supplierCat.getSupplier()));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailListLoc(Collection<SupplierLocality> suppliersLoc) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierLocality supplierLoc : suppliersLoc) {
            userDetails.add(FullSupplierDetail.createFullSupplierDetail(supplierLoc.getSupplier()));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    protected UserDetail toUserDetail(Supplier supplier) {

        UserDetail detail = new UserDetail();
        detail.setUserId(supplier.getBusinessUser().getId());

        // Set UserDetail according to his roles
        for (BusinessUserRole role : supplier.getBusinessUser().getBusinessUserRoles()) {
            if (role instanceof Client) {
                Client clientRole = (Client) role;
                detail.setClientId(clientRole.getId());
                detail.addRole(Role.CLIENT);

                detail.setVerified(clientRole.getVerification().equals(Verification.VERIFIED));
            }
            if (role instanceof Supplier) {
                Supplier supplierRole = (Supplier) role;
                detail.setSupplierId(supplierRole.getId());
                detail.addRole(Role.SUPPLIER);
                SupplierDetail supplierDetail = new SupplierDetail();

                // supplierServices
                List<UserService> services = supplierRole.getBusinessUser().getUserServices();
                for (UserService us : services) {
                    supplierDetail.addService(us.getId().intValue());
                }

                // categories
                ArrayList<String> categories = new ArrayList<String>();
                List<Category> cats = supplierRole.getCategories();
                for (Category cat : cats) {
                    categories.add(cat.getId() + "");
                }
                supplierDetail.setCategories(categories);

                // localities
                ArrayList<String> localities = new ArrayList<String>();
                List<Category> locs = supplierRole.getCategories();
                for (Category loc : locs) {
                    localities.add(loc.getId() + "");
                }

                // Other
                detail.setCompanyName(supplier.getBusinessUser().getBusinessUserData().getCompanyName());
                detail.setEmail(supplier.getBusinessUser().getEmail());
                detail.setFirstName(supplier.getBusinessUser().getBusinessUserData().getPersonFirstName());
                detail.setLastName(supplier.getBusinessUser().getBusinessUserData().getPersonLastName());
                detail.setIdentificationNumber(supplier.getBusinessUser()
                        .getBusinessUserData().getIdentificationNumber());
//                detail.setPassword(supplier.getBusinessUser().getPassword());
                detail.setPhone(supplier.getBusinessUser().getBusinessUserData().getPhone());
                detail.setTaxId(supplier.getBusinessUser().getBusinessUserData().getTaxId());

                supplierDetail.setLocalities(localities);

                detail.setSupplier(supplierDetail);

                detail.setVerified(supplierRole.getVerification().equals(Verification.VERIFIED));
            }
        }

        // TODO Beho fix this user ID
//        System.out.println("ID is: " + userRoles.get(0).getBusinessUser().getId());
        return detail;
    }
    private List<Category> categoriesHistory = new ArrayList<Category>();
    private List<Locality> localitiesHistory = new LinkedList<Locality>();

    private Category[] getAllSubCategories(Long id) {
        final Category cat = this.generalService.find(Category.class, id);
        final List<Category> allSubCategories = this.treeItemService.getAllDescendants(cat, Category.class);
        allSubCategories.add(cat);
        return allSubCategories.toArray(new Category[allSubCategories.size()]);
    }

    private Locality[] getAllSublocalities(String code) {
        final Locality loc = this.localityService.getLocality(code);
        final List<Locality> allSubLocalites = this.treeItemService.getAllDescendants(loc, Locality.class);
        allSubLocalites.add(loc);
        return allSubLocalites.toArray(new Locality[allSubLocalites.size()]);
    }

    @Override
    public long filterSuppliersCount(SearchDataHolder detail) {
        return this.filter(detail, null).size();
    }

    @Override
    public List<FullSupplierDetail> filterSuppliers(
            int start, int count, SearchDataHolder detail, Map<String, OrderType> orderColumns) {
        List<FullSupplierDetail> searchResult = this.filter(detail, orderColumns);
        if (searchResult.size() < (start + count)) {
            return searchResult.subList(start, searchResult.size());
        } else {
            return searchResult.subList(start, count);
        }
    }

    private List<FullSupplierDetail> filter(SearchDataHolder detail, Map<String, OrderType> orderColumns) {
        //null
        if (detail == null) {
            Search search = this.getFilter(null, null, orderColumns);
            return this.createSupplierDetailList(this.generalService.search(search));
        }
        //0 0
        if (detail.getCategory() == null && detail.getLocality() == null) {
            Search search = this.getFilter("else", detail, orderColumns);
            return this.createSupplierDetailList(this.generalService.search(search));
        }
        //1 0
        if (detail.getCategory() != null && detail.getLocality() == null) {
            Search search = this.getFilter("category", detail, orderColumns);
            return this.createSupplierDetailListCat(this.generalService.searchAndCount(search).getResult());
        }
        //0 1
        if (detail.getCategory() == null && detail.getLocality() != null) {
            Search search = this.getFilter("locality", detail, orderColumns);
            return this.createSupplierDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (detail.getCategory() != null && detail.getLocality() != null) {
            List<FullSupplierDetail> suppliersCat = this.createSupplierDetailListCat(
                    this.generalService.searchAndCount(this.getFilter("category", detail, orderColumns)).getResult());

            List<FullSupplierDetail> suppliersLoc = this.createSupplierDetailListLoc(
                    this.generalService.searchAndCount(this.getFilter("locality", detail, orderColumns)).getResult());

            List<FullSupplierDetail> suppliers = new ArrayList<FullSupplierDetail>();
            for (FullSupplierDetail supplierCat : suppliersCat) {
                if (suppliersLoc.contains(supplierCat)) {
                    suppliers.add(supplierCat);
                }
            }
            return suppliers;
        }
        return null;
    }

    private Search getFilter(String type, SearchDataHolder detail, Map<String, OrderType> orderColumns) {
        Search search = null;
        String prefix = "";
        if (detail != null) {

            /** simple **/
            if (type.equals("category")) {
                search = new Search(SupplierCategory.class);
                prefix = "supplier.";
                if (detail.getCategory() != null) {
                    final List<Category> allSubCategories = Arrays.asList(
                            this.getAllSubCategories(detail.getCategory().getId()));
                    search.addFilterIn("category", allSubCategories);
                }
            } else if (type.equals("locality")) {
                search = new Search(SupplierLocality.class);
                prefix = "supplier.";
                if (detail.getLocality() != null) {
                    final List<Locality> allSubLocalities = Arrays.asList(
                            this.getAllSublocalities(detail.getLocality().getCode()));
                    search.addFilterIn("locality", allSubLocalities);
                }
            } else {
                search = new Search(Supplier.class);
            }
            if (!detail.getText().equals("")) {
                search.addFilterLike(prefix + "businessUser.businessUserData.companyName",
                        "%" + detail.getText() + "%");
            }

            /** additional **/
            if (detail.isAdditionalInfo()) {
                if (detail.getRatingFrom() != -1) {
                    search.addFilterGreaterOrEqual(prefix + "rating", detail.getRatingFrom());
                }
                if (detail.getRatingTo() != -1) {
                    search.addFilterLessOrEqual(prefix + "rating", detail.getRatingTo());
                }
                if (detail.getSupplierDescription() != null) {
                    search.addFilterLike(prefix + "businessUser.businessUserData.description",
                            "%" + detail.getSupplierDescription() + "%");
                }
            }
        } else {
            search = new Search(Supplier.class);
        }
        /** sort **/
        if (orderColumns != null) {
            for (String item : orderColumns.keySet()) {
                if (orderColumns.get(item).getValue().equals(OrderType.ASC.getValue())) {
                    search.addSortAsc(prefix + item, true);
                } else {
                    search.addSortDesc(prefix + item, true);
                }
            }
        }
        return search;
    }
}
