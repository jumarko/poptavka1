package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.UserDetail;


@RemoteServiceRelativePath("service/cs")
public interface ClientRPCService extends RemoteService {
    ArrayList<UserDetail> getAllClients();

    UserDetail createNewClient(UserDetail clientDetail);

    UserDetail verifyClient(UserDetail client);

    boolean checkFreeEmail(String email);

}
