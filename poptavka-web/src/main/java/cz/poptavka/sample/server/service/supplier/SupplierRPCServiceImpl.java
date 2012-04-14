package cz.poptavka.sample.server.service.supplier;

import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import cz.poptavka.sample.client.service.demand.SupplierRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.common.Status;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.product.Service;
import cz.poptavka.sample.domain.product.ServiceType;
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
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.UserDetail.Role;
import cz.poptavka.sample.shared.exceptions.CommonException;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    public UserDetail createNewSupplier(UserDetail supplier) throws CommonException {
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

        // TODO Beho+Vojto rework according to GWT Spring Security function.
        return this.toUserDetail(newSupplier);
//        return this.toUserDetail(supplierFromDB.getBusinessUser().getId(),
//                supplierFromDB.getBusinessUser().getBusinessUserRoles());
    }

    private void assignBusinessRoleToNewSupplier(Supplier newSupplier) {
        newSupplier.getBusinessUser().getBusinessUserRoles().add(newSupplier);
    }

    private void setNewSupplierBusinessUserData(UserDetail supplier, Supplier newSupplier) {
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(supplier.getCompanyName()).taxId(supplier.getTaxId())
                .identificationNumber(supplier.getIdentificationNumber())
                .phone(supplier.getPhone()).personFirstName(supplier.getFirstName())
                .personLastName(supplier.getLastName()) //.description(supplier.getDescription());
                .build();
        newSupplier.getBusinessUser().setBusinessUserData(businessUserData);
    }

    private void setNewSupplierAddresses(UserDetail supplier, Supplier newSupplier) {
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

    private void setNewSupplierUserServices(UserDetail supplier, Supplier newSupplier) {
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

    private void setNewSupplierCategories(UserDetail supplier, Supplier newSupplier) {
        final List<Category> categories = new ArrayList<Category>();
        for (String categoryId : supplier.getSupplier().getCategories()) {
            categories.add(this.getCategory(categoryId));
        }
        newSupplier.setCategories(categories);
    }

    private void setNewSupplierLocalities(UserDetail supplier, Supplier newSupplier) {
        final List<Locality> localities = new ArrayList<Locality>();
        for (String localityCode : supplier.getSupplier().getLocalities()) {
            localities.add(this.getLocality(localityCode));
        }
        newSupplier.setLocalities(localities);
    }

    private List<Address> getAddressesFromSupplierCityName(UserDetail supplier) {
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
        return addresses;
    }

    @Override
    public ArrayList<ServiceDetail> getSupplierServices() throws CommonException {
        List<Service> services = this.generalService.findAll(Service.class);
        if (services != null) {
            System.out.println("Services count: " + services.size());
        } else {
            System.out.println("NNULLLLLLLL");
        }
        return convertToServiceDetails(services);
    }

    protected ArrayList<ServiceDetail> convertToServiceDetails(List<Service> services) {
        ArrayList<ServiceDetail> details = new ArrayList<ServiceDetail>();
        for (Service sv : services) {
            if (sv.isValid() && sv.getServiceType().equals(ServiceType.SUPPLIER)) {
                ServiceDetail detail = new ServiceDetail();
                detail.setId(sv.getId());
                detail.setTitle(sv.getTitle());
                detail.setPrice(sv.getPrice());
                detail.setPrepaidMonths(sv.getPrepaidMonths().intValue());
                detail.setDescription(sv.getDescription());
                details.add(detail);
            }
        }
        return details;
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
    public ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID, String localityCode)
        throws CommonException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();
        Category[] categories = {categoryService.getById(categoryID)};
        Locality[] localities = {localityService.getLocality(localityCode)};
        return this.createSupplierDetailList(supplierService.getSuppliers(
                resultCriteria, categories, localities));
//                this.getAllSubcategories(categoryID),
//                this.getAllSublocalities(localityCode)));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID) throws CommonException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();

        return this.createSupplierDetailList(supplierService.getSuppliers(
                resultCriteria, categoryService.getById(categoryID)));
//                this.getAllSubcategories(categoryID)));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSuppliers(int start, int count) throws CommonException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();

        return this.createSupplierDetailList(supplierService.getAll(resultCriteria));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns)
        throws CommonException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start)
                .maxResults(count).orderByColumns(orderColumns).build();
        return this.createSupplierDetailList(supplierService.getAll(resultCriteria));
    }

    /**
     * Get All suppliers count.
     */
    @Override
    public Integer getSuppliersCount() throws CommonException {
        return (int) supplierService.getCount();
    }

    @Override
    public Long getSuppliersCount(Long categoryID) throws CommonException {
        return supplierService.getSuppliersCountQuick(categoryService.getById(categoryID));
//        return supplierService.getSuppliersCount(this.getAllSubcategories(categoryID));
    }

    @Override
    public Long getSuppliersCount(Long categoryID, String localityCode) throws CommonException {
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
    public FullSupplierDetail updateSupplier(FullSupplierDetail supplierDetail) throws CommonException {
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
                detail.setIdentificationNumber(
                        supplier.getBusinessUser().getBusinessUserData().getIdentificationNumber());
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
    public long filterSuppliersCount(SearchModuleDataHolder detail) throws CommonException {
        return this.filter(detail, null).size();
    }

    @Override
    public List<FullSupplierDetail> filterSuppliers(
            int start, int count, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns)
        throws CommonException {
        List<FullSupplierDetail> searchResult = this.filter(detail, orderColumns);
        if (searchResult.size() < (start + count)) {
            return searchResult.subList(start, searchResult.size());
        } else {
            return searchResult.subList(start, count);
        }
    }

    private List<FullSupplierDetail> filter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        //1 0
        if (detail.getCategories() != null
                && detail.getLocalities() == null) {
            Search search = this.getCategoryFilter(detail, orderColumns);
            return this.createSupplierDetailListCat(this.generalService.searchAndCount(search).getResult());
        }
        //0 1
        if (detail.getCategories() == null
                && detail.getLocalities() != null) {
            Search search = this.getLocalityFilter(detail, orderColumns);
            return this.createSupplierDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (detail.getCategories() != null
                && detail.getLocalities() != null) {
            List<FullSupplierDetail> suppliersCat = this.createSupplierDetailListCat(
                    this.generalService.searchAndCount(this.getCategoryFilter(detail, orderColumns)).getResult());

            List<FullSupplierDetail> suppliersLoc = this.createSupplierDetailListLoc(
                    this.generalService.searchAndCount(this.getLocalityFilter(detail, orderColumns)).getResult());

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

//    private Search getFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
//        Search search = null;
//
//        /** simple **/
//        if (detail.getHomeSuppliers().getSupplierCategory() != null) {
//            search = new Search(SupplierCategory.class);
//            final List<Category> allSubCategories = Arrays.asList(
//                    this.getAllSubCategories(detail.getHomeSuppliers().getSupplierCategory().getId()));
//            search.addFilterIn("category", allSubCategories);
//        } else if (detail.getHomeSuppliers().getSupplierLocality() != null) {
//            search = new Search(SupplierLocality.class);
//            final List<Locality> allSubLocalities = Arrays.asList(
//                    this.getAllSublocalities(detail.getHomeSuppliers().getSupplierLocality().getCode()));
//            search.addFilterIn("locality", allSubLocalities);
//        }
//        if (detail.getHomeSuppliers().getSupplierName() != null) {
//            Collection<BusinessUserData> data = generalService.search(
//                    new Search(BusinessUserData.class).addFilterLike("companyName",
//                    "%" + detail.getAdminSuppliers().getSupplierName() + "%"));
//            search.addFilterIn("businessUser.businessUserData", data);
//        }
//
//        //                if (detail.isAdditionalInfo()) {
//        if (detail.getHomeSuppliers().getRatingFrom() != null) {
//            search.addFilterGreaterOrEqual(prefix + "overalRating", detail.getHomeSuppliers().getRatingFrom());
//        }
//        if (detail.getHomeSuppliers().getRatingTo() != null) {
//            search.addFilterLessOrEqual(prefix + "overalRating", detail.getHomeSuppliers().getRatingTo());
//        }
//        if (detail.getHomeSuppliers().getSupplierDescription() != null) {
//            Collection<BusinessUserData> descsData = new ArrayList<BusinessUserData>();
//            String[] descs = detail.getHomeSuppliers().getSupplierDescription().split("\\s+");
//
//            for (int i = 0; i < descs.length; i++) {
//                descsData.addAll(generalService.search(new Search(
//    BusinessUserData.class).addFilterLike("description",
//"%" + detail.getAdminSuppliers().getSupplierDescription() + "%")));
//            }
//
//            search.addFilterIn("businessUser.businessUserData", descsData);
//        }
//        /** sort **/
//        if (orderColumns != null) {
//            for (String item : orderColumns.keySet()) {
//                if (orderColumns.get(item).getValue().equals(OrderType.ASC.getValue())) {
//                    search.addSortAsc(prefix + item, true);
//                } else {
//                    search.addSortDesc(prefix + item, true);
//                }
//            }
//        }
//        return search;
//    }
    private Search getCategoryFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Search categorySearch = new Search(SupplierCategory.class);
        List<Category> allSubCategories = new ArrayList<Category>();
        for (CategoryDetail cat : detail.getCategories()) {
            allSubCategories = Arrays.asList(this.getAllSubCategories(cat.getId()));
        }
        categorySearch.addFilterIn("category", allSubCategories);

        Search supplierSearch = this.getSupplierFilter(detail, orderColumns);
        if (supplierSearch != null) {
            categorySearch.addFilterIn("supplier", generalService.search(supplierSearch));
        }

        return categorySearch;
    }

    private Search getLocalityFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Search localitySearch = new Search(SupplierLocality.class);
        List<Locality> allSubLocalities = new ArrayList<Locality>();
        for (LocalityDetail loc : detail.getLocalities()) {
            allSubLocalities = Arrays.asList(
                    this.getAllSublocalities(loc.getCode()));
        }
        localitySearch.addFilterIn("locality", allSubLocalities);

        Search suppSearch = this.getSupplierFilter(detail, orderColumns);
        if (suppSearch != null) {
            localitySearch.addFilterIn("supplier", generalService.search(suppSearch));
        }

        return localitySearch;
    }

    private Search getSupplierFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Boolean filterApplied = false;
        Search search = new Search(Supplier.class);
        for (FilterItem item : detail.getAttibutes()) {
            if (item.getItem().equals("comapnyName")) {
                Collection<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), "", item));
                search.addFilterIn("businessUser.businessUserData", data);
                filterApplied = true;
            } else if (item.getItem().equals("description")) {
                Collection<BusinessUserData> descsData = new ArrayList<BusinessUserData>();
                String[] descs = item.getValue().toString().split("\\s+");

                for (int i = 0; i < descs.length; i++) {
                    descsData.addAll(generalService.search(
                            this.filter(new Search(BusinessUserData.class), "", item)));
                }
                search.addFilterIn("businessUser.businessUserData", descsData);
                filterApplied = true;
            } else {
                this.filter(search, "", item);
            }

        }
        if (filterApplied) {
            return this.getSortSearch(search, orderColumns, "supplier");
        } else {
            return null;
        }
    }

    private Search getSortSearch(Search search, Map<String, OrderType> orderColumns, String prefix) {
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

    private Search filter(Search search, String prefix, FilterItem item) {
        prefix += ".";
        switch (item.getOperation()) {
            case FilterItem.OPERATION_EQUALS:
                search.addFilterEqual(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_LIKE:
                search.addFilterLike(prefix + item.getItem(), "%" + item.getValue().toString() + "%");
                break;
            case FilterItem.OPERATION_IN:
                search.addFilterIn(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_FROM:
                search.addFilterGreaterOrEqual(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_TO:
                search.addFilterLessOrEqual(prefix + item.getItem(), item.getValue());
                break;
            default:
                break;
        }
        return search;
    }
}
