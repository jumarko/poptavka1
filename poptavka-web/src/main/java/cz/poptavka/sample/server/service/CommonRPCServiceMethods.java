/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service;

import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.product.UserService;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Praso
 */
public class CommonRPCServiceMethods {

    protected UserDetail toUserDetail(Long userId, List<BusinessUserRole> userRoles) {
        if (userRoles == null) {
            throw new NullPointerException("These roles are not defined");
        }

        UserDetail detail = new UserDetail();
        detail.setUserId(userId);

        // Set UserDetail according to his roles
        for (BusinessUserRole role : userRoles) {
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
                supplierDetail.setLocalities(localities);

                detail.setSupplier(supplierDetail);

                detail.setVerified(supplierRole.getVerification().equals(Verification.VERIFIED));
            }
        }
        // TODO Beho fix this user ID
        System.out.println("ID is: " + userRoles.get(0).getBusinessUser().getId());
        return detail;
    }
}
