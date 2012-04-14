package cz.poptavka.sample.server.service.settings;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;

import cz.poptavka.sample.client.service.demand.SettingsRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.shared.domain.AddressDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import cz.poptavka.sample.shared.domain.settings.SettingsDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

public class SettingsRPCServiceImpl extends AutoinjectingRemoteService
        implements SettingsRPCService {

    private static final long serialVersionUID = 113266708108432157L;

    private GeneralService generalService;

    @Override
    public SettingsDetail getUserSettings(long userId) throws RPCException {
        GWT.log("Getting user settings for user:" + userId);
        final BusinessUser user = (BusinessUser) generalService
                .searchUnique(new Search(User.class).addFilterEqual("id",
                        userId));

        SettingsDetail settingsDetail = new SettingsDetail();

        List<BusinessUserRole> roles = user.getBusinessUserRoles();
        for (BusinessUserRole role : roles) {
            if (role instanceof Client) {
                settingsDetail.setEmail(role.getBusinessUser().getEmail());
                Client client = (Client) role;
                if (client.getOveralRating() != null) {
                    settingsDetail.setClientRating(client.getOveralRating());
                }

            }
            if (role instanceof Supplier) {
                Supplier supplier = (Supplier) role;
                SupplierDetail supplierDetail = new SupplierDetail();
                if (supplier.getOveralRating() != null) {
                    supplierDetail.setOverallRating(supplier.getOveralRating());
                }
                ArrayList<String> localities = new ArrayList<String>();
                for (Locality locality : supplier.getLocalities()) {
                    localities.add(locality.getName());
                }
                supplierDetail.setLocalities(localities);
                ArrayList<String> categories = new ArrayList<String>();
                for (Category category : supplier.getCategories()) {
                    categories.add(category.getName());
                }
                supplierDetail.setCategories(categories);
                settingsDetail.setSupplier(supplierDetail);
            }

        }
        settingsDetail.setFirstName(user.getBusinessUserData()
                .getPersonFirstName());
        settingsDetail.setLastName(user.getBusinessUserData()
                .getPersonLastName());
        settingsDetail.setPhone(user.getBusinessUserData().getPhone());
        settingsDetail.setIdentificationNumber(user.getBusinessUserData()
                .getIdentificationNumber());
        settingsDetail.setCompanyName(user.getBusinessUserData()
                .getCompanyName());
        settingsDetail.setDescription(user.getBusinessUserData()
                .getDescription());
        settingsDetail.setTaxId(user.getBusinessUserData().getTaxId());
        List<AddressDetail> addresses = new ArrayList<AddressDetail>();
        for (Address address : user.getAddresses()) {
            AddressDetail detail = new AddressDetail();
            if (address.getCity() != null) {
                detail.setCityName(address.getCity().getName());
            }
            detail.setStreet(address.getStreet());
            detail.setZipCode(address.getZipCode());
            addresses.add(detail);
        }
        settingsDetail.setAddresses(addresses);
        GWT.log("User settings get:" + settingsDetail.getFirstName());
        return settingsDetail;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

}
