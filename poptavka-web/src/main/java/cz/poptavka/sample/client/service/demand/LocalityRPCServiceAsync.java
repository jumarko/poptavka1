package cz.poptavka.sample.client.service.demand;


import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;

import java.util.List;

public interface LocalityRPCServiceAsync {

    void getLocalities(LocalityType type, AsyncCallback<List<LocalityDetail>> callback);

    void getLocalities(String locCode, AsyncCallback<List<LocalityDetail>> callback);
}
