package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;

@RemoteServiceRelativePath("service/locality")
public interface LocalityRPCService extends RemoteService {

    ArrayList<Locality> getLocalities(LocalityType type);

}
