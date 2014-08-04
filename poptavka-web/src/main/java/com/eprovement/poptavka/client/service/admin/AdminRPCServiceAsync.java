package com.eprovement.poptavka.client.service.admin;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.LogType;
import com.eprovement.poptavka.shared.domain.PropertiesDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AdminClientDetail;
import com.eprovement.poptavka.shared.domain.adminModule.LogDetail;
import com.eprovement.poptavka.shared.domain.demand.OriginDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.Set;

public interface AdminRPCServiceAsync {

    //---------------------- NEW DEMANDS --------------------------------------------
    void getAdminNewDemandsCount(AsyncCallback<Integer> callback);

    void getAdminNewDemands(SearchDefinition searchDefinition, AsyncCallback<List<AdminDemandDetail>> callback);

    //---------------------- ASSIGNED DEMANDS --------------------------------------------
    void getAdminAssignedDemandsCount(
        long userId, SearchDefinition searchDefinition, DemandStatus demandStatus, AsyncCallback<Integer> callback);

    void getAdminAssignedDemands(long userId, DemandStatus demandStatus, SearchDefinition searchDefinition,
        AsyncCallback<List<AdminDemandDetail>> callback);

    //---------------------- ACTIVE DEMANDS ----------------------------------------------
    void getAdminActiveDemandsCount(SearchDefinition searchDefinition, AsyncCallback<Integer> callback);

    void getAdminActiveDemands(SearchDefinition searchDefinition, AsyncCallback<List<AdminDemandDetail>> callback);

    void getConversation(long threadRootId, long loggedUserId, long counterPartyUserId,
        AsyncCallback<List<MessageDetail>> callback);

    void createConversation(long demandId, long userAdminId, AsyncCallback<Long> callback);

    void approveDemands(Set<AdminDemandDetail> demandsToApprove, AsyncCallback<Void> callback);

    /**************************************************************************/
    /*                        Admin Clients                                   */
    /**************************************************************************/
    /**
     * @see com.eprovement.poptavka.client.service.admin.AdminRPCService#getClientsCount
     */
    void getClientsCount(SearchDefinition searchDefinition, AsyncCallback<Integer> callback);

    /**
     * @see com.eprovement.poptavka.client.service.admin.AdminRPCService#getClients
     */
    void getClients(SearchDefinition searchDefinition, AsyncCallback<List<AdminClientDetail>> callback);

    /**
     * @see com.eprovement.poptavka.client.service.admin.AdminRPCService#getOrigins()
     */
    void getOrigins(AsyncCallback<List<OriginDetail>> callback);

    /**
     * @see com.eprovement.poptavka.client.service.admin.AdminRPCService#changeEmail(long, java.lang.String)
     */
    void changeEmail(long userId, String newEmail, AsyncCallback<Void> callback);

    /**
     * @see com.eprovement.poptavka.client.service.admin.AdminRPCService#setUserOrigin(long, long)
     */
    void setUserOrigin(long clietnId, long originId, AsyncCallback<Void> callback);

    void getSystemProperties(AsyncCallback<List<PropertiesDetail>> callback);

    void updateSystemProperties(PropertiesDetail properties, AsyncCallback<Boolean> callback);

    void calculateDemandCounts(AsyncCallback<Void> callback);
    void calculateSupplierCounts(AsyncCallback<Void> callback);

    /**
     * @see com.eprovement.poptavka.client.service.admin.AdminRPCService#getJobProgress
     */
    void getJobProgress(LogType type, AsyncCallback<LogDetail> callback);
}
