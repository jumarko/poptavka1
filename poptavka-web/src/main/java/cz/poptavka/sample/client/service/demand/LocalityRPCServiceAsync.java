package cz.poptavka.sample.client.service.demand;


import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import java.util.List;

public interface LocalityRPCServiceAsync {

    void getLocalities(LocalityType type,
            AsyncCallback<List<Locality>> callback);

}
