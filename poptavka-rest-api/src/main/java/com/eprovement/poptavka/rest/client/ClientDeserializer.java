package com.eprovement.poptavka.rest.client;

import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.rest.common.serializer.BusinessUserDeserializer;
import com.eprovement.poptavka.service.user.ClientService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ClientDeserializer implements Converter<ClientDto, Client> {

    private final BusinessUserDeserializer businessUserDeserializer;
    private final ClientService clientService;


    public ClientDeserializer(BusinessUserDeserializer businessUserDeserializer,
                              ClientService clientService) {
        notNull(businessUserDeserializer, "businessUserDeserializer cannot be null");
        notNull(clientService, "clientService cannot be null");
        this.businessUserDeserializer = businessUserDeserializer;
        this.clientService = clientService;
    }


    @Override
    public Client convert(ClientDto clientDto) {
        notNull(clientDto);
        if (clientDto.getId() != null) {
            // existing client, load him from DB
            final Client clientById = clientService.getById(clientDto.getId());
            notNull(clientById, "Invalid client id - no such client");
            return clientById;
        }

        final Client client = new Client();
        final BusinessUser businessUser = businessUserDeserializer.convert(clientDto);
        client.setBusinessUser(businessUser);
        if (clientDto.getOverallRating() != null) {
            client.setOveralRating(clientDto.getOverallRating());
        }
        return client;
    }

}
