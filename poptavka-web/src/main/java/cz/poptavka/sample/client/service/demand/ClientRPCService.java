package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.domain.user.Client;

import java.util.List;


@RemoteServiceRelativePath("service/cs")
public interface ClientRPCService extends RemoteService {
    List<Client> getAllClients();

}
