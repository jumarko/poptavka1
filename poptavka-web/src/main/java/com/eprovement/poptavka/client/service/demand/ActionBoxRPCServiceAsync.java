package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public interface ActionBoxRPCServiceAsync {

    /**************************************************************************/
    /* Message methods                                                        */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void setMessageStarStatus(List<Long> list, boolean newStatus, AsyncCallback<Void> callback);
}
