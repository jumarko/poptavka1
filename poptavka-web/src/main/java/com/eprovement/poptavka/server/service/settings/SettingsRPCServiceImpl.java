package com.eprovement.poptavka.server.service.settings;

import com.eprovement.poptavka.domain.settings.NotificationItem;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;

import com.eprovement.poptavka.client.service.demand.SettingsRPCService;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.SupplierDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.settings.NotificationDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

@Configurable
public class SettingsRPCServiceImpl extends AutoinjectingRemoteService
        implements SettingsRPCService {

    private static final long serialVersionUID = 113266708108432157L;
    private GeneralService generalService;
    private Converter<Locality, LocalityDetail> localityConverter;
    private Converter<Category, CategoryDetail> categoryConverter;
    private Converter<Address, AddressDetail> addressConverter;

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, LocalityDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setAddressConverter(
            @Qualifier("addressConverter") Converter<Address, AddressDetail> addressConverter) {
        this.addressConverter = addressConverter;
    }

    //TODO Nahradit konverterom???
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public SettingDetail getUserSettings(long userId) throws RPCException, ApplicationSecurityException {
        GWT.log("Getting user settings for user:" + userId);
        final BusinessUser user = (BusinessUser) generalService.searchUnique(new Search(User.class).addFilterEqual("id",
                userId));

        SettingDetail settingsDetail = new SettingDetail();
        settingsDetail.setUserId(userId);

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
                supplierDetail.setLocalities(localityConverter.convertToTargetList(supplier.getLocalities()));
                supplierDetail.setCategories(categoryConverter.convertToTargetList(supplier.getCategories()));
                settingsDetail.setSupplier(supplierDetail);
            }

        }
        settingsDetail.setFirstName(user.getBusinessUserData().getPersonFirstName());
        settingsDetail.setLastName(user.getBusinessUserData().getPersonLastName());
        settingsDetail.setPhone(user.getBusinessUserData().getPhone());
        settingsDetail.setIdentificationNumber(user.getBusinessUserData().getIdentificationNumber());
        settingsDetail.setCompanyName(user.getBusinessUserData().getCompanyName());
        settingsDetail.setDescription(user.getBusinessUserData().getDescription());
        settingsDetail.setTaxId(user.getBusinessUserData().getTaxId());
        List<AddressDetail> addresses = new ArrayList<AddressDetail>();
        for (Address address : user.getAddresses()) {
            addresses.add(addressConverter.convertToTarget(address));
        }
        settingsDetail.setAddresses(addresses);

        /** NOTIFICATIONS. **/
        List<NotificationDetail> notifications = new ArrayList<NotificationDetail>();
        for (NotificationItem item : user.getSettings().getNotificationItems()) {
            NotificationDetail targetItem = new NotificationDetail();
            /**/ targetItem.setNotificationIdemId(item.getId());
            /**/ targetItem.setEnabled(item.isEnabled());
            /**/ targetItem.setName(item.getNotification().getName());
            /**/ targetItem.setPeriod(item.getPeriod());
            notifications.add(targetItem);
        }
        settingsDetail.setNotifications(notifications);

        GWT.log("User settings get:" + settingsDetail.getFirstName());
        return settingsDetail;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    /**
     * This method will update number of unread messages of logged user.
     * Since this RPC class requires access of authenticated user (see security-web.xml) this method will be called
     * only when PoptavkaUserAuthentication object exist in SecurityContextHolder and we can retrieve userId.
     *
     * TODO Vojto - call DB servise to retrieve the number of unread messages for given userId
     *
     * @return UnreadMessagesDetail with number of unread messages and other info to be displayed after users logs in
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException {
        Long userId = ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        // TODO Vojto - get number of unread messages. UserId is provided from Authentication obejct see above
        UnreadMessagesDetail unreadMessagesDetail = new UnreadMessagesDetail();
        unreadMessagesDetail.setUnreadMessagesCount(99);
        return unreadMessagesDetail;
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public Boolean updateSettings(SettingDetail settingsDetail) throws RPCException, ApplicationSecurityException {
        final BusinessUser user = (BusinessUser) generalService.searchUnique(
                new Search(User.class).addFilterEqual("id", settingsDetail.getUserId()));

        List<BusinessUserRole> roles = user.getBusinessUserRoles();
        for (BusinessUserRole role : roles) {
            role.getBusinessUser().setEmail(settingsDetail.getEmail());
            if (role instanceof Client) {
                Client client = (Client) role;
                client.setOveralRating(settingsDetail.getClientRating());

            }
            if (role instanceof Supplier) {
                Supplier supplier = (Supplier) role;
                if (settingsDetail.getSupplier().getOverallRating() != null) {
                    supplier.setOveralRating(settingsDetail.getSupplier().getOverallRating());
                }
                supplier.setLocalities(localityConverter.convertToSourceList(
                        settingsDetail.getSupplier().getLocalities()));
                supplier.setCategories(categoryConverter.convertToSourceList(
                        settingsDetail.getSupplier().getCategories()));
            }

        }
        user.getBusinessUserData().setPersonFirstName(settingsDetail.getFirstName());
        user.getBusinessUserData().setPersonLastName(settingsDetail.getLastName());
        user.getBusinessUserData().setPhone(settingsDetail.getPhone());
        user.getBusinessUserData().setIdentificationNumber(settingsDetail.getIdentificationNumber());
        user.getBusinessUserData().setCompanyName(settingsDetail.getCompanyName());
        user.getBusinessUserData().setWebsite(settingsDetail.getWebsite());
        user.getBusinessUserData().setDescription(settingsDetail.getDescription());
        user.getBusinessUserData().setTaxId(settingsDetail.getTaxId());
        List<Address> addresses = new ArrayList<Address>();
        for (AddressDetail addressDetail : settingsDetail.getAddresses()) {
            addresses.add(addressConverter.convertToSource(addressDetail));
        }
        user.setAddresses(addresses);

        /** NOTIFICATIONS. **/
        List<NotificationItem> notificationsItems = new ArrayList<NotificationItem>();
        for (int i = 0; i < user.getSettings().getNotificationItems().size(); i++) {
            NotificationItem domainItem = user.getSettings().getNotificationItems().get(i);
            NotificationDetail item = settingsDetail.getNotifications().get(i);

            domainItem.setPeriod(item.getPeriod());
            domainItem.setEnabled(item.isEnabled());
            notificationsItems.add(domainItem);
        }
        user.getSettings().setNotificationItems(notificationsItems);

        generalService.merge(user);
        return true;
    }
}
