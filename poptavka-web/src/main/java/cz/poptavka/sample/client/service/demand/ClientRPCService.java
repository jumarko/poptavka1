package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.adminModule.ClientDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import java.util.Map;

@RemoteServiceRelativePath("service/cs")
public interface ClientRPCService extends RemoteService {

    ArrayList<UserDetail> getAllClients();

    ArrayList<ClientDetail> getClients(int start, int count);

    UserDetail createNewClient(UserDetail clientDetail);

    boolean checkFreeEmail(String email);

    Integer getClientsCount();

    ClientDetail updateClient(ClientDetail supplierDetail);

    ArrayList<ClientDetail> getSortedClients(int start, int count, Map<String, OrderType> orderColumns);
}
