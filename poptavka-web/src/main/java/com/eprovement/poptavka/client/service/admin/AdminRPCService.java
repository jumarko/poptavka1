package com.eprovement.poptavka.client.service.admin;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import java.util.Set;

@RemoteServiceRelativePath(AdminRPCService.URL)
public interface AdminRPCService extends RemoteService {

    String URL = "service/admin";

    //---------------------- NEW DEMANDS --------------------------------------------

    Integer getAdminNewDemandsCount() throws RPCException, ApplicationSecurityException;

    List<AdminDemandDetail> getAdminNewDemands(SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    //---------------------- ASSIGNED DEMANDS --------------------------------------------
    Integer getAdminAssignedDemandsCount(
        long userId, SearchDefinition searchDefinition, DemandStatus demandStatus) throws
        RPCException, ApplicationSecurityException;

    List<AdminDemandDetail> getAdminAssignedDemands(
        long userId, DemandStatus demandStatus, SearchDefinition searchDefinition) throws
        RPCException, ApplicationSecurityException;

    //---------------------- ACTIVE DEMANDS ---------------------------------------------
    Integer getAdminActiveDemandsCount(SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    List<AdminDemandDetail> getAdminActiveDemands(SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    List<MessageDetail> getConversation(long threadRootId, long loggedUserId, long counterPartyUserId)
        throws RPCException;

    Long createConversation(long demandId, long userAdminId) throws RPCException, ApplicationSecurityException;

    void approveDemands(Set<AdminDemandDetail> demandsToApprove) throws RPCException, ApplicationSecurityException;

    //---------------------- OTHERS -------------------------------------------------------
    UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException;
}
