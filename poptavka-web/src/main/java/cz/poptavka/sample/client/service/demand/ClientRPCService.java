package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.adminModule.ClientDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

import java.util.Map;

@RemoteServiceRelativePath("service/cs")
public interface ClientRPCService extends RemoteService {

    ArrayList<UserDetail> getAllClients() throws CommonException;

    ArrayList<ClientDetail> getClients(int start, int count) throws CommonException;

    UserDetail createNewClient(UserDetail clientDetail) throws CommonException;

    Integer getClientsCount() throws CommonException;

    ClientDetail updateClient(ClientDetail supplierDetail) throws CommonException;

    ArrayList<ClientDetail> getSortedClients(int start, int count, Map<String, OrderType> orderColumns)
        throws CommonException;
}
