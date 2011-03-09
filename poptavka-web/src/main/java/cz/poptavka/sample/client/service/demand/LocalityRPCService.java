package cz.poptavka.sample.client.service.demand;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@RemoteServiceRelativePath("service/locality")
public interface LocalityRPCService extends RemoteService {

    /**
     * Returns locality list.
     *
     * @param type
     * @return list locality list according to type
     */
    ArrayList<LocalityDetail> getLocalities(LocalityType type);

    /**
     * Returns locality list.
     *
     * @param locCode
     * @return list locality children list
     */
    ArrayList<LocalityDetail> getLocalities(String locCode);

}
