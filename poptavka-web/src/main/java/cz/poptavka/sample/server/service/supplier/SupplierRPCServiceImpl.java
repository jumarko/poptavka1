package cz.poptavka.sample.server.service.supplier;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.SupplierRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.product.Service;
import cz.poptavka.sample.domain.product.ServiceType;
import cz.poptavka.sample.domain.product.UserService;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
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
    public long createNewSupplier(UserDetail supplier) {
        Supplier newSupplier = new Supplier();
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
            .companyName(supplier.getCompanyName())
            .taxId(supplier.getTaxId())
            .identificationNumber(supplier.getIdentifiacationNumber())
            .phone(supplier.getPhone())
            .personFirstName(supplier.getFirstName())
            .personLastName(supplier.getLastName())
//            .description(supplier.getDescription());
            .build();
        newSupplier.getBusinessUser().setBusinessUserData(businessUserData);
        newSupplier.getBusinessUser().setEmail(supplier.getEmail());
        newSupplier.getBusinessUser().setPassword(supplier.getPassword());
        /** address **/
        Address address = null;
        Locality cityLoc = getLocalityByExample(supplier.getAddress().getCityName());
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
            Service service = generalService.find(Service.class, Long.parseLong(serviceId + ""));
            UserService userService = new UserService();
            userService.setService(service);
            us.add(userService);
        }

        newSupplier.getBusinessUser().setUserServices(us);
        /** registration process **/
        newSupplier = supplierService.create(newSupplier);
        //del
        System.out.println("New Supplier id : " + newSupplier.getId());
        return newSupplier.getId();
    }


    private Locality getLocalityByExample(String searchString) {
        Locality loc = new Locality();
        loc.setName(searchString);
        List<Locality> results = localityService.findByExample(loc);
        return (results.size() != 0) ? results.get(0) : null;
    }

    private Category getCategoryByExample(String searchString) {
        Category loc = new Category();
        loc.setName(searchString);
        List<Category> results = categoryService.findByExample(loc);
        return (results.size() != 0) ? results.get(0) : null;
    }

    public ArrayList<ServiceDetail> getSupplierServices() {
        List<Service> services = this.generalService.findAll(Service.class);
        if (services != null) {
            System.out.println("Services count: " + services.size());
        } else {
            System.out.println("NNULLLLLLLL");
        }
        return convertToServiceDetails(services);
    }

    private ArrayList<ServiceDetail> convertToServiceDetails(List<Service> services) {
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
                if (detail == null) {
                    System.out.println("ServiceDetail inserted NULL");
                }
            }
        }
        if (details == null) {
            System.out.println("ServiceDetail RPC returns NULL");
        }
        return details;
    }

}
