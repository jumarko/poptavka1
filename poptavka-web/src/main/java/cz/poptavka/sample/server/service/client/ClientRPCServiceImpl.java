package cz.poptavka.sample.server.service.client;

import cz.poptavka.sample.client.service.demand.ClientRPCService;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.user.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ClientRPCServiceImpl extends AutoinjectingRemoteService implements ClientRPCService {

    private ClientService clientService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRPCServiceImpl.class);

    @Override
    public List<Client> getAllClients() {
        // TODO Auto-generated method stub
        LOGGER.info("Getting fake clients");
        return clientService.getAll();
    }


    public ClientService getClientService() {
        return clientService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

}
