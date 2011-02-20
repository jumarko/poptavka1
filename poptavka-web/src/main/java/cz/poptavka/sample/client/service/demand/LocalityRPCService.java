package cz.poptavka.sample.client.service.demand;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import java.util.List;

@RemoteServiceRelativePath("service/locality")
public interface LocalityRPCService extends RemoteService {

    List<Locality> getLocalities(LocalityType type);

}
