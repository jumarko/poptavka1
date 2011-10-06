package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.InvoiceDetail;
import cz.poptavka.sample.shared.domain.PaymentDetail;
import cz.poptavka.sample.shared.domain.PermissionDetail;
import cz.poptavka.sample.shared.domain.PreferenceDetail;
import java.util.Map;

@RemoteServiceRelativePath("service/cs")
public interface GeneralRPCService extends RemoteService {

    //---------------------- ACCESS ROLE ------------------------------------------------
    Long getAccessRolesCount();

    ArrayList<AccessRoleDetail> getAccessRoles(int start, int count);

    AccessRoleDetail updateAccessRole(AccessRoleDetail supplierDetail);

    ArrayList<AccessRoleDetail> getSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    Long getEmailsActivationCount();

    ArrayList<EmailActivationDetail> getEmailsActivation(int start, int count);

    EmailActivationDetail updateEmailActivation(EmailActivationDetail supplierDetail);

    ArrayList<EmailActivationDetail> getSortedEmailsActivation(
            int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- INVOICE --------------------------------------------------
    Long getInvoicesCount();

    ArrayList<InvoiceDetail> getInvoices(int start, int count);

    InvoiceDetail updateInvoice(InvoiceDetail supplierDetail);

    ArrayList<InvoiceDetail> getSortedInvoices(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    Long getOurPaymentDetailsCount();

    ArrayList<PaymentDetail> getOurPaymentDetails(int start, int count);

    PaymentDetail updateOurPaymentDetail(PaymentDetail supplierDetail);

    ArrayList<PaymentDetail> getSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- PERMISSION ------------------------------------------------
    Long getPermissionsCount();

    ArrayList<PermissionDetail> getPermissions(int start, int count);

    PermissionDetail updatePermission(PermissionDetail supplierDetail);

    ArrayList<PermissionDetail> getSortedPermissions(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- PREFERENCES ------------------------------------------------
    Long getPreferencesCount();

    ArrayList<PreferenceDetail> getPreferences(int start, int count);

    PreferenceDetail updatePreference(PreferenceDetail supplierDetail);

    ArrayList<PreferenceDetail> getSortedPreferences(int start, int count, Map<String, OrderType> orderColumns);
}
