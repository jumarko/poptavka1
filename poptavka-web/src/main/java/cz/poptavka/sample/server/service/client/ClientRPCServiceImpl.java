package cz.poptavka.sample.server.service.client;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.ClientRPCService;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.ClientDetail;

public class ClientRPCServiceImpl extends AutoinjectingRemoteService implements ClientRPCService {

    private ClientService clientService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRPCServiceImpl.class);

    @Override
    public ArrayList<ClientDetail> getAllClients() {
        // TODO Auto-generated method stub
        LOGGER.info("Getting fake clients");
//        return clientService.getAll();
        return null;
    }


    public ClientService getClientService() {
        return clientService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }


    @Override
    public void sendClientId(long id) {
        // TODO Auto-generated method stub

    }

}
