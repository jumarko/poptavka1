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
    int getAccessRolesCount();

    AccessRoleDetail updateAccessRole(AccessRoleDetail supplierDetail);

    ArrayList<AccessRoleDetail> getSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    int getEmailsActivationCount();

    EmailActivationDetail updateEmailActivation(EmailActivationDetail supplierDetail);

    ArrayList<EmailActivationDetail> getSortedEmailsActivation(
            int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- INVOICE --------------------------------------------------
    int getInvoicesCount();

    InvoiceDetail updateInvoice(InvoiceDetail supplierDetail);

    ArrayList<InvoiceDetail> getSortedInvoices(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    int getOurPaymentDetailsCount();

    PaymentDetail updateOurPaymentDetail(PaymentDetail supplierDetail);

    ArrayList<PaymentDetail> getSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- PERMISSION ------------------------------------------------
    int getPermissionsCount();

    PermissionDetail updatePermission(PermissionDetail supplierDetail);

    ArrayList<PermissionDetail> getSortedPermissions(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- PREFERENCES ------------------------------------------------
    int getPreferencesCount();

    PreferenceDetail updatePreference(PreferenceDetail supplierDetail);

    ArrayList<PreferenceDetail> getSortedPreferences(int start, int count, Map<String, OrderType> orderColumns);
}
