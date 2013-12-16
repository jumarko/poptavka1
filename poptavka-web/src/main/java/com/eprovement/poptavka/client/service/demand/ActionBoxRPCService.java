package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(ActionBoxRPCService.URL)
public interface ActionBoxRPCService extends RemoteService {

    String URL = "service/actionBox";

    /**************************************************************************/
    /* Message methods                                                        */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws RPCException;

}