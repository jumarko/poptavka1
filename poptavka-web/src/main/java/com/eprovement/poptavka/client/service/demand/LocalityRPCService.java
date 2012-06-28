package com.eprovement.poptavka.client.service.demand;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.domain.address.LocalityType;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@RemoteServiceRelativePath(LocalityRPCService.URL)
public interface LocalityRPCService extends RemoteService {

    String URL = "service/locality";

    /**
     * Returns locality list.
     *
     * @param type
     * @return list locality list according to type
     */
    ArrayList<LocalityDetail> getLocalities(LocalityType type) throws RPCException;

    /**
     * Returns locality list.
     *
     * @param locCode
     * @return list locality children list
     */
    ArrayList<LocalityDetail> getLocalities(String locCode) throws RPCException;

    LocalityDetail getLocality(long id) throws RPCException;

    ArrayList<LocalityDetail> getAllRootLocalities() throws RPCException;

    ArrayList<LocalityDetail> getSubLocalities(String locCode) throws RPCException;
}
