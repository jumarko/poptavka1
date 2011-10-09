package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.InvoiceDetail;
import cz.poptavka.sample.shared.domain.PaymentDetail;
import cz.poptavka.sample.shared.domain.PermissionDetail;
import cz.poptavka.sample.shared.domain.PreferenceDetail;
import java.util.Map;

public interface GeneralRPCServiceAsync {

    //---------------------- ACCESS ROLE ------------------------------------------------
    void getAccessRolesCount(AsyncCallback<Integer> callback);

    void updateAccessRole(AccessRoleDetail accessRoleDetail,
            AsyncCallback<AccessRoleDetail> callback);

    void getSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<AccessRoleDetail>> callback);

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    void getEmailsActivationCount(AsyncCallback<Integer> callback);

    void updateEmailActivation(EmailActivationDetail supplierDetail,
            AsyncCallback<EmailActivationDetail> callback);

    void getSortedEmailsActivation(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<EmailActivationDetail>> callback);

    //---------------------- INVOICE --------------------------------------------------
    void getInvoicesCount(AsyncCallback<Integer> callback);

    void updateInvoice(InvoiceDetail supplierDetail,
            AsyncCallback<InvoiceDetail> callback);

    void getSortedInvoices(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<InvoiceDetail>> callback);

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    void getOurPaymentDetailsCount(AsyncCallback<Integer> callback);

    void updateOurPaymentDetail(PaymentDetail supplierDetail,
            AsyncCallback<PaymentDetail> callback);

    void getSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<PaymentDetail>> callback);

    //---------------------- PERMISSION ------------------------------------------------
    void getPermissionsCount(AsyncCallback<Integer> callback);

    void updatePermission(PermissionDetail supplierDetail,
            AsyncCallback<PermissionDetail> callback);

    void getSortedPermissions(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<PermissionDetail>> callback);

    //---------------------- PREFERENCES ------------------------------------------------
    void getPreferencesCount(AsyncCallback<Integer> callback);

    void updatePreference(PreferenceDetail supplierDetail,
            AsyncCallback<PreferenceDetail> callback);

    void getSortedPreferences(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<PreferenceDetail>> callback);
}
