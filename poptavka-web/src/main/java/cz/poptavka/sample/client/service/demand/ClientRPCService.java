package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.ClientDetail;


@RemoteServiceRelativePath("service/cs")
public interface ClientRPCService extends RemoteService {
    ArrayList<ClientDetail> getAllClients();

    long createNewClient(ClientDetail clientDetail);

    void sendClientId(long id);

    long verifyClient(ClientDetail client);
}
