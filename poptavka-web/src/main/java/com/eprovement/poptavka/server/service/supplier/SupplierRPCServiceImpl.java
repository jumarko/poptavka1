package com.eprovement.poptavka.server.service.supplier;

import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.server.converter.Converter;
import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.service.demand.SupplierRPCService;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.SupplierCategory;
import com.eprovement.poptavka.domain.user.SupplierLocality;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.common.TreeItemService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configurable
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
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Category, CategoryDetail> categoryConverter;
    private Converter<Locality, LocalityDetail> localityConverter;

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

    @Autowired
    public void setSupplierConverter(
            @Qualifier("supplierConverter") Converter<Supplier, FullSupplierDetail> supplierConverter) {
        this.supplierConverter = supplierConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, LocalityDetail> localityConverter) {
        this.localityConverter = localityConverter;
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
    public FullSupplierDetail createNewSupplier(FullSupplierDetail supplier) throws RPCException {
        final Supplier newSupplier = new Supplier();

        newSupplier.getBusinessUser().setEmail(supplier.getUserData().getEmail());
        newSupplier.getBusinessUser().setPassword(supplier.getUserData().getPassword());
        newSupplier.setLocalities(localityConverter.convertToSourceList(supplier.getLocalities()));
        newSupplier.setCategories(categoryConverter.convertToSourceList(supplier.getCategories()));

        setNewSupplierAddresses(supplier.getUserData(), newSupplier);
        setNewSupplierBusinessUserData(supplier.getUserData(), newSupplier);
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

        return supplierConverter.convertToTarget(newSupplier);
    }

    private void assignBusinessRoleToNewSupplier(Supplier newSupplier) {
        newSupplier.getBusinessUser().getBusinessUserRoles().add(newSupplier);
    }

    private void setNewSupplierBusinessUserData(BusinessUserDetail supplier, Supplier newSupplier) {
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(supplier.getCompanyName())
                .taxId(supplier.getTaxId())
                .identificationNumber(supplier.getIdentificationNumber())
                .phone(supplier.getPhone())
                .personFirstName(supplier.getFirstName())
                .personLastName(supplier.getLastName())
                //TODO RELEASE - chyba description v builderi BusinessUserData objektu?
                //                .description(supplier.getDescription());
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

    private void setNewSupplierUserServices(FullSupplierDetail supplier, Supplier newSupplier) {
        final List<UserService> us = new ArrayList<UserService>();

        ArrayList<Integer> userServicesId = supplier.getServices();
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

    private List<Address> getAddressesFromSupplierCityName(BusinessUserDetail supplier) {
        List<Address> addresses = new ArrayList<Address>();
        for (AddressDetail detail : supplier.getAddresses()) {
            Locality cityLoc = (Locality) generalService.searchUnique(
                    new Search(Locality.class).addFilterEqual("name", detail.getCity()));

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
    public ArrayList<ServiceDetail> getSupplierServices() throws RPCException {
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
    public Locality getLocality(Long id) throws RPCException {
        System.out.println("Locality code value: " + id + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(id);
//        return localityService.getById(10);
    }

    public Category getCategory(String id) throws RPCException {
        return categoryService.getById(Long.parseLong(id));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSuppliers(
            int start, int count, Long categoryID, Long localityId) throws RPCException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();
        Category[] categories = {categoryService.getById(categoryID)};
        Locality[] localities = {localityService.getLocality(localityId)};
        return this.createSupplierDetailList(supplierService.getSuppliers(
                resultCriteria, categories, localities));
//                this.getAllSubcategories(categoryID),
//                this.getAllSublocalities(localityCode)));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID) throws RPCException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();

        return this.createSupplierDetailList(supplierService.getSuppliers(
                resultCriteria, categoryService.getById(categoryID)));
//                this.getAllSubcategories(categoryID)));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSuppliers(int start, int count) throws RPCException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start).maxResults(count).build();

        return this.createSupplierDetailList(supplierService.getAll(resultCriteria));
    }

    @Override
    public ArrayList<FullSupplierDetail> getSortedSuppliers(
            int start, int count, Map<String, OrderType> orderColumns) throws RPCException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder().firstResult(start)
                .maxResults(count).orderByColumns(orderColumns).build();
        return this.createSupplierDetailList(supplierService.getAll(resultCriteria));
    }

    /**
     * Get All suppliers count.
     */
    @Override
    public Integer getSuppliersCount() throws RPCException {
        return (int) supplierService.getCount();
    }

    @Override
    public Long getSuppliersCount(Long categoryID) throws RPCException {
        return supplierService.getSuppliersCountQuick(categoryService.getById(categoryID));
//        return supplierService.getSuppliersCount(this.getAllSubcategories(categoryID));
    }

    @Override
    public Long getSuppliersCount(Long categoryID, Long localityId) throws RPCException {
        return supplierService.getSuppliersCount(
                new Category[]{categoryService.getById(categoryID)},
                new Locality[]{localityService.getLocality(localityId)});
    }

    /**
     * Method updates supplier object in database.
     *
     * @param supplierDetail - updated supplierDetail from front end
     * @param updateWhat - define data to update. Values: supplier, categories, localities, address, userdata, all.
     * @return supplierDetail
     */
    @Override
    public FullSupplierDetail updateSupplier(FullSupplierDetail supplierDetail) throws RPCException {
        Supplier supplier = supplierService.getById(supplierDetail.getSupplierId());

        //Supplier
        if (supplierDetail.getUserData().getOverallRating() == -1) {
            supplier.setOveralRating(null);
        } else {
            supplier.setOveralRating(supplierDetail.getUserData().getOverallRating());
        }
        supplier.setCertified(supplierDetail.isCertified());
        supplier.setVerification(supplierDetail.getUserData().getVerification());

        // -- categories
        List<Category> newCategories = categoryConverter.convertToSourceList(supplierDetail.getCategories());

        // -- localities
        List<Locality> newLocalities = localityConverter.convertToSourceList(supplierDetail.getLocalities());

        //TODO Martin - how to update addresses???
//        List<Address> newAddresses = new ArrayList<Address>();
//        for (AddressDetail addr : supplierDetail.getAddresses()) {
//            Address address = new Address();
//            supplier.getBusinessUser().getAddresses()
//        }
//        supplier.getBusinessUser().getBusinessUserData().
//        descriptionBox.setText(supplier.getDescription());

        //Busines data
        supplier.getBusinessUser().setEmail(supplierDetail.getUserData().getEmail());

        supplier.getBusinessUser().getBusinessUserData().setDescription(
                supplierDetail.getUserData().getDescription());
        if (supplierDetail.getUserData().getBusinessType() != null
                && !supplierDetail.getUserData().getBusinessType().equals("")) {
            supplier.getBusinessUser().setBusinessType(supplierDetail.getUserData().getBusinessType());
        }
        supplier.getBusinessUser().getBusinessUserData().setCompanyName(
                supplierDetail.getUserData().getCompanyName());
        supplier.getBusinessUser().getBusinessUserData().setIdentificationNumber(
                supplierDetail.getUserData().getIdentificationNumber());

        supplierService.update(supplier);
        return supplierDetail;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailList(Collection<Supplier> suppliers) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (Supplier supplier : suppliers) {
            userDetails.add(supplierConverter.convertToTarget(supplier));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailListCat(Collection<SupplierCategory> suppliersCat) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierCategory supplierCat : suppliersCat) {
            userDetails.add(supplierConverter.convertToTarget(supplierCat.getSupplier()));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailListLoc(Collection<SupplierLocality> suppliersLoc) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierLocality supplierLoc : suppliersLoc) {
            userDetails.add(supplierConverter.convertToTarget(supplierLoc.getSupplier()));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private Category[] getAllSubCategories(Long id) {
        final Category cat = this.generalService.find(Category.class, id);
        final List<Category> allSubCategories = this.treeItemService.getAllDescendants(cat, Category.class);
        allSubCategories.add(cat);
        return allSubCategories.toArray(new Category[allSubCategories.size()]);
    }

    private Locality[] getAllSublocalities(Long id) {
        final Locality loc = this.localityService.getLocality(id);
        final List<Locality> allSubLocalites = this.treeItemService.getAllDescendants(loc, Locality.class);
        allSubLocalites.add(loc);
        return allSubLocalites.toArray(new Locality[allSubLocalites.size()]);
    }

    @Override
    public long filterSuppliersCount(SearchModuleDataHolder detail) throws RPCException {
        return this.filter(detail, null).size();
    }

    @Override
    public List<FullSupplierDetail> filterSuppliers(int start, int count,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) throws RPCException {
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
//                    this.getAllSublocalities(detail.getHomeSuppliers().getSupplierLocality().getId()));
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
                    this.getAllSublocalities(loc.getId()));
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
        for (FilterItem item : detail.getAttributes()) {
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
