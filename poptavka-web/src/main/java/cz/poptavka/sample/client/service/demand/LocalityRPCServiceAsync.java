package cz.poptavka.sample.client.service.demand;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public interface LocalityRPCServiceAsync {

    void getLocalities(LocalityType type, AsyncCallback<ArrayList<LocalityDetail>> callback);

    void getLocalities(String locCode, AsyncCallback<ArrayList<LocalityDetail>> callback);
}
