package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.Map;

@RemoteServiceRelativePath(ClientRPCService.URL)
public interface ClientRPCService extends RemoteService {

    String URL = "service/cs";

    ArrayList<BusinessUserDetail> getAllClients() throws RPCException;

    ArrayList<ClientDetail> getClients(int start, int count) throws RPCException;

    BusinessUserDetail createNewClient(BusinessUserDetail clientDetail) throws RPCException;

    Integer getClientsCount() throws RPCException;

    ClientDetail updateClient(ClientDetail supplierDetail) throws RPCException;

    ArrayList<ClientDetail> getSortedClients(int start, int count, Map<String, OrderType> orderColumns)
        throws RPCException;
}
