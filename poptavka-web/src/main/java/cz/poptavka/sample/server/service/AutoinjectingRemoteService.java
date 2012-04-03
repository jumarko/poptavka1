package cz.poptavka.sample.server.service;

import net.sf.gilead.gwt.PersistentRemoteService;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;


/**
 * Expose Spring services to GWT app.
 * From http://pgt.de/2009/07/17/non-invasive-gwt-and-spring-integration-reloaded/
 */
public abstract class AutoinjectingRemoteService extends PersistentRemoteService  {

    private static final long serialVersionUID = 237077627422062352L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
                this.getServletContext());
        AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
    }

    // TODO Praso - moved to DemandRPCService. Probably not used. Remove it.
//    protected ArrayList<FullDemandDetail> toDemandDetailList(List<Demand> list) {
//        ArrayList<FullDemandDetail> details = new ArrayList<FullDemandDetail>();
//        for (Demand demand : list) {
//            details.add(FullDemandDetail.createDemandDetail(demand));
//        }
//        return details;
//    }

    // TODO Praso - moved to demandsRPCServiceImpl
//    protected ArrayList<OfferDetail> toOfferDetailList(List<Offer> offerList) {
//        ArrayList<OfferDetail> details = new ArrayList<OfferDetail>();
//        for (Offer offer : offerList) {
//            OfferDetail detail = new OfferDetail();
//            detail.setDemandId(offer.getDemand().getId());
//            detail.setFinishDate(offer.getFinishDate());
//            detail.setPrice(offer.getPrice());
//            detail.setSupplierId(offer.getSupplier().getId());
//            if (offer.getSupplier().getBusinessUser().getBusinessUserData() != null) {
//                detail.setSupplierName(offer.getSupplier().getBusinessUser().getBusinessUserData().getCompanyName());
//            } else {
//                detail.setSupplierName("unknown");
//            }
//
//            details.add(detail);
//        }
//        return details;
//    }

    // TODO Praso - method moved to CommonRPCServiceMethods
//    protected UserDetail toUserDetail(Long userId, List<BusinessUserRole> userRoles) {
//        if (userRoles == null) {
//            throw new NullPointerException("These roles are not defined");
//        }
//
//        UserDetail detail = new UserDetail();
//        detail.setUserId(userId);
//
//        // Set UserDetail according to his roles
//        for (BusinessUserRole role : userRoles) {
//            if (role instanceof Client) {
//                Client clientRole = (Client) role;
//                detail.setClientId(clientRole.getId());
//                detail.addRole(Role.CLIENT);
//
//                detail.setVerified(clientRole.getVerification().equals(Verification.VERIFIED));
//            }
//            if (role instanceof Supplier) {
//                Supplier supplierRole = (Supplier) role;
//                detail.setSupplierId(supplierRole.getId());
//                detail.addRole(Role.SUPPLIER);
//                SupplierDetail supplierDetail = new SupplierDetail();
//
//                // supplierServices
//                List<UserService> services = supplierRole.getBusinessUser().getUserServices();
//                for (UserService us : services) {
//                    supplierDetail.addService(us.getId().intValue());
//                }
//
//                // categories
//                ArrayList<String> categories = new ArrayList<String>();
//                List<Category> cats = supplierRole.getCategories();
//                for (Category cat : cats) {
//                    categories.add(cat.getId() + "");
//                }
//                supplierDetail.setCategories(categories);
//
//                // localities
//                ArrayList<String> localities = new ArrayList<String>();
//                List<Category> locs = supplierRole.getCategories();
//                for (Category loc : locs) {
//                    localities.add(loc.getId() + "");
//                }
//                supplierDetail.setLocalities(localities);
//
//                detail.setSupplier(supplierDetail);
//
//                detail.setVerified(supplierRole.getVerification().equals(Verification.VERIFIED));
//            }
//        }
//        // TODO Beho fix this user ID
//        System.out.println("ID is: " + userRoles.get(0).getBusinessUser().getId());
//        return detail;
//    }

    // TODO Praso - moved to SupplierCreationRPCServiceImpl
//    protected ArrayList<ServiceDetail> convertToServiceDetails(List<Service> services) {
//        ArrayList<ServiceDetail> details = new ArrayList<ServiceDetail>();
//        for (Service sv : services) {
//            if (sv.isValid() && sv.getServiceType().equals(ServiceType.SUPPLIER)) {
//                ServiceDetail detail = new ServiceDetail();
//                detail.setId(sv.getId());
//                detail.setTitle(sv.getTitle());
//                detail.setPrice(sv.getPrice());
//                detail.setPrepaidMonths(sv.getPrepaidMonths().intValue());
//                detail.setDescription(sv.getDescription());
//                details.add(detail);
//            }
//        }
//        return details;
//    }

}
