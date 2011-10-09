/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.general;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import cz.poptavka.sample.client.service.demand.GeneralRPCService;
import cz.poptavka.sample.domain.activation.EmailActivation;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.invoice.Invoice;
import cz.poptavka.sample.domain.invoice.OurPaymentDetails;
import cz.poptavka.sample.domain.settings.Preference;
import cz.poptavka.sample.domain.user.rights.AccessRole;
import cz.poptavka.sample.domain.user.rights.Permission;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.InvoiceDetail;
import cz.poptavka.sample.shared.domain.PaymentDetail;
import cz.poptavka.sample.shared.domain.PermissionDetail;
import cz.poptavka.sample.shared.domain.PreferenceDetail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Excalibur
 */
public class GeneralRPCServiceImpl extends AutoinjectingRemoteService implements GeneralRPCService {

    private static final long serialVersionUID = 1132667081084321575L;
    private GeneralService generalService;

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    /**********************************************************************************************
     ***********************  ACCESS ROLE SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public int getAccessRolesCount() {
        final Search search = new Search(AccessRole.class);
        return generalService.count(search);
    }

//    @Override
//    public ArrayList<AccessRoleDetail> getAccessRoles(int start, int count) {
//        final Search localitySearch = new Search(AccessRole.class);
//        localitySearch.setFirstResult(start);
//        localitySearch.setMaxResults(count);
//        localitySearch.addSortAsc("name");
//        return this.createAccessRoleDetailList(generalService.search(localitySearch));
//    }
    @Override
    public AccessRoleDetail updateAccessRole(AccessRoleDetail accessRoleDetail) {
        return generalService.merge(accessRoleDetail);
    }

    @Override
    public ArrayList<AccessRoleDetail> getSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createAccessRoleDetailList(
                generalService.search(this.search(start, count, orderColumns, AccessRole.class)));
    }

    private ArrayList<AccessRoleDetail> createAccessRoleDetailList(Collection<AccessRole> demands) {
        ArrayList<AccessRoleDetail> accessRoles = new ArrayList<AccessRoleDetail>();
        for (AccessRole role : demands) {
            accessRoles.add(AccessRoleDetail.createAccessRoleDetail(role));
        }
        return accessRoles;
    }

    /**********************************************************************************************
     ***********************  EMAIL ACTIVATION SECTION. *******************************************
     **********************************************************************************************/
    @Override
    public int getEmailsActivationCount() {
        final Search search = new Search(EmailActivation.class);
        return generalService.count(search);
    }

//    @Override
//    public ArrayList<EmailActivationDetail> getEmailsActivation(int start, int count) {
//        final Search search = new Search(EmailActivation.class);
//        search.setFirstResult(start);
//        search.setMaxResults(count);
//        search.addSortAsc("timeout");
//        return this.createEmailActivationDetailList(generalService.search(search));
//    }
    @Override
    public EmailActivationDetail updateEmailActivation(EmailActivationDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public ArrayList<EmailActivationDetail> getSortedEmailsActivation(
            int start, int count, Map<String, OrderType> orderColumns) {
        return this.createEmailActivationDetailList(
                generalService.search(this.search(start, count, orderColumns, EmailActivation.class)));
    }

    private ArrayList<EmailActivationDetail> createEmailActivationDetailList(Collection<EmailActivation> emailsList) {
        ArrayList<EmailActivationDetail> emailsActivation = new ArrayList<EmailActivationDetail>();
        for (EmailActivation email : emailsList) {
            emailsActivation.add(EmailActivationDetail.createEmailActivationDetail(email));
        }
        return emailsActivation;
    }

    /**********************************************************************************************
     ***********************  INVOICE SECTION. ****************************************************
     **********************************************************************************************/
    @Override
    public int getInvoicesCount() {
        final Search search = new Search(Invoice.class);
        return generalService.count(search);
    }

//    @Override
//    public ArrayList<InvoiceDetail> getInvoices(int start, int count) {
//        final Search search = new Search(Invoice.class);
//        search.setFirstResult(start);
//        search.setMaxResults(count);
//        search.addSortAsc("issueDate");
//        return this.createInvoiceDetailList(generalService.search(search));
//    }
    @Override
    public InvoiceDetail updateInvoice(InvoiceDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public ArrayList<InvoiceDetail> getSortedInvoices(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createInvoiceDetailList(
                generalService.search(this.search(start, count, orderColumns, Invoice.class)));
    }

    private ArrayList<InvoiceDetail> createInvoiceDetailList(Collection<Invoice> invoicesList) {
        ArrayList<InvoiceDetail> invoices = new ArrayList<InvoiceDetail>();
        for (Invoice invoice : invoicesList) {
            invoices.add(InvoiceDetail.createInvoiceDetail(invoice));
        }
        return invoices;
    }

    /**********************************************************************************************
     ***********************  OUR PAYMENT DETAIL SECTION. *****************************************
     **********************************************************************************************/
    @Override
    public int getOurPaymentDetailsCount() {
        final Search search = new Search(OurPaymentDetails.class);
        return generalService.count(search);
    }

//    @Override
//    public ArrayList<PaymentDetail> getOurPaymentDetails(int start, int count) {
//        final Search search = new Search(OurPaymentDetails.class);
//        search.setFirstResult(start);
//        search.setMaxResults(count);
//        return this.createPaymentDetailList(generalService.search(search));
//    }
    @Override
    public PaymentDetail updateOurPaymentDetail(PaymentDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public ArrayList<PaymentDetail> getSortedOurPaymentDetails(
            int start, int count, Map<String, OrderType> orderColumns) {
        return this.createPaymentDetailList(
                generalService.search(this.search(start, count, orderColumns, OurPaymentDetails.class)));
    }

    private ArrayList<PaymentDetail> createPaymentDetailList(Collection<OurPaymentDetails> paymentsList) {
        ArrayList<PaymentDetail> payments = new ArrayList<PaymentDetail>();
        for (OurPaymentDetails payment : paymentsList) {
            payments.add(PaymentDetail.createOurPaymentDetailDetail(payment));
        }
        return payments;
    }

    /**********************************************************************************************
     ***********************  PERMISSION SECTION. *************************************************
     **********************************************************************************************/
    @Override
    public int getPermissionsCount() {
        final Search search = new Search(Permission.class);
        return generalService.count(search);
    }

//    @Override
//    public ArrayList<PermissionDetail> getPermissions(int start, int count) {
//        final Search search = new Search(Permission.class);
//        search.setFirstResult(start);
//        search.setMaxResults(count);
//        search.addSortAsc("code");
//        return this.createPermissionDetailList(generalService.search(search));
//    }
    @Override
    public PermissionDetail updatePermission(PermissionDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public ArrayList<PermissionDetail> getSortedPermissions(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createPermissionDetailList(
                generalService.search(this.search(start, count, orderColumns, Permission.class)));
    }

    private ArrayList<PermissionDetail> createPermissionDetailList(Collection<Permission> permissionList) {
        ArrayList<PermissionDetail> accessRoles = new ArrayList<PermissionDetail>();
        for (Permission permission : permissionList) {
            accessRoles.add(PermissionDetail.createPermissionsDetail(permission));
        }
        return accessRoles;
    }

    /**********************************************************************************************
     ***********************  PREFERENCE SECTION. *************************************************
     **********************************************************************************************/
    @Override
    public int getPreferencesCount() {
        final Search search = new Search(Preference.class);
        return generalService.count(search);
    }

//    @Override
//    public ArrayList<PreferenceDetail> getPreferences(int start, int count) {
//        final Search search = new Search(Preference.class);
//        search.setFirstResult(start);
//        search.setMaxResults(count);
//        search.addSortAsc("key");
//        return this.createPreferenceDetailList(generalService.search(search));
//    }
    @Override
    public PreferenceDetail updatePreference(PreferenceDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public ArrayList<PreferenceDetail> getSortedPreferences(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createPreferenceDetailList(
                generalService.search(this.search(start, count, orderColumns, Preference.class)));
    }

    private ArrayList<PreferenceDetail> createPreferenceDetailList(Collection<Preference> preference) {
        ArrayList<PreferenceDetail> preferences = new ArrayList<PreferenceDetail>();
        for (Preference role : preference) {
            preferences.add(PreferenceDetail.createPreferenceDetail(role));
        }
        return preferences;
    }

    /**********************************************************************************************
     ***********************  COMMON METHODS. *************************************************
     **********************************************************************************************/
    private Search search(int start, int count, Map<String, OrderType> orderColumns, Class<?> classs) {
        final Search search = new Search(classs);
        search.setFirstResult(start);
        search.setMaxResults(count);
        List<Sort> sorts = new ArrayList<Sort>();
        for (String str : orderColumns.keySet()) {
            if (orderColumns.get(str) == OrderType.ASC) {
                sorts.add(new Sort(str, false));
            } else {
                sorts.add(new Sort(str, true));
            }
        }
        return search.addSorts(sorts.toArray(new Sort[sorts.size()]));
    }
}
