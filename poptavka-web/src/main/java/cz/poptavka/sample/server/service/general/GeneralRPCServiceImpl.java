/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.general;

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
    public Long getAccessRolesCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<AccessRoleDetail> getAccessRoles(int start, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AccessRoleDetail updateAccessRole(AccessRoleDetail accessRoleDetail) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<AccessRoleDetail> getSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Long getEmailsActivationCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<EmailActivationDetail> getEmailsActivation(int start, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EmailActivationDetail updateEmailActivation(EmailActivationDetail supplierDetail) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<EmailActivationDetail> getSortedEmailsActivation(
            int start, int count, Map<String, OrderType> orderColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Long getInvoicesCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<InvoiceDetail> getInvoices(int start, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InvoiceDetail updateInvoice(InvoiceDetail supplierDetail) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<InvoiceDetail> getSortedInvoices(int start, int count, Map<String, OrderType> orderColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Long getOurPaymentDetailsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<PaymentDetail> getOurPaymentDetails(int start, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PaymentDetail updateOurPaymentDetail(PaymentDetail supplierDetail) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<PaymentDetail> getSortedOurPaymentDetails(
            int start, int count, Map<String, OrderType> orderColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Long getPermissionsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<PermissionDetail> getPermissions(int start, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionDetail updatePermission(PermissionDetail supplierDetail) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<PermissionDetail> getSortedPermissions(int start, int count, Map<String, OrderType> orderColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Long getPreferencesCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<PreferenceDetail> getPreferences(int start, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreferenceDetail updatePreference(PreferenceDetail supplierDetail) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<PreferenceDetail> getSortedPreferences(int start, int count, Map<String, OrderType> orderColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private ArrayList<PreferenceDetail> createPreferenceDetailList(Collection<Preference> preference) {
        ArrayList<PreferenceDetail> preferences = new ArrayList<PreferenceDetail>();
        for (Preference role : preference) {
            preferences.add(PreferenceDetail.createPreferenceDetail(role));
        }
        return preferences;
    }
}
