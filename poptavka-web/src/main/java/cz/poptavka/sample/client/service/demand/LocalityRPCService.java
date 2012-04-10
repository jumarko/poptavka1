package cz.poptavka.sample.client.service.demand;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

@RemoteServiceRelativePath("service/locality")
public interface LocalityRPCService extends RemoteService {

    /**
     * Returns locality list.
     *
     * @param type
     * @return list locality list according to type
     */
    ArrayList<LocalityDetail> getLocalities(LocalityType type) throws CommonException;

    /**
     * Returns locality list.
     *
     * @param locCode
     * @return list locality children list
     */
    ArrayList<LocalityDetail> getLocalities(String locCode) throws CommonException;

    Locality getLocality(long id) throws CommonException;

    ArrayList<LocalityDetail> getAllRootLocalities() throws CommonException;

    ArrayList<LocalityDetail> getSubLocalities(String locCode) throws CommonException;
}
