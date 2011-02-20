package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;

public interface LocalityRPCServiceAsync {

    void getLocalities(LocalityType type,
            AsyncCallback<ArrayList<Locality>> callback);

}
