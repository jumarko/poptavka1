package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import cz.poptavka.sample.domain.user.Client;

import java.util.List;

public interface ClientRPCServiceAsync {

    void getAllClients(AsyncCallback<List<Client>> callback);

}
