package cz.poptavka.sample.client.service.demand;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;

import java.util.List;

@RemoteServiceRelativePath("service/locality")
public interface LocalityRPCService extends RemoteService {

    /**
     * Returns locality list.
     *
     * @param type
     * @return list locality list according to type
     */
    List<LocalityDetail> getLocalities(LocalityType type);

    /**
     * Returns locality list.
     *
     * @param locCode
     * @return list locality children list
     */
    List<LocalityDetail> getLocalities(String locCode);

}
