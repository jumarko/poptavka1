package com.eprovement.poptavka.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.LocalitySuggestionDetail;
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
    List<LocalityDetail> getLocalities(LocalityType type) throws RPCException;

    /**
     * Returns locality list.
     *
     * @param locCode
     * @return list locality children list
     */
    List<LocalityDetail> getSubLocalities(Long id) throws RPCException;

    LocalityDetail getLocality(long id) throws RPCException;

    List<LocalityDetail> getAllRootLocalities() throws RPCException;

    List<LocalitySuggestionDetail> getCityWithStateSuggestions(String cityLike) throws RPCException;

    List<LocalitySuggestionDetail> getShortCityWithStateSuggestions(String cityLike) throws RPCException;
}
