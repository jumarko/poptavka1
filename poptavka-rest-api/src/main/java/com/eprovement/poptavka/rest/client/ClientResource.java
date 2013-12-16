
package com.eprovement.poptavka.rest.client;

import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.rest.ResourceNotFoundException;
import com.eprovement.poptavka.rest.common.ResourceUtils;
import com.eprovement.poptavka.rest.common.resource.AbstractPageableResource;
import com.eprovement.poptavka.service.user.ClientService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(ClientResource.CLIENT_RESOURCE_URI)
public class ClientResource extends AbstractPageableResource<Client, ClientDto> {

    static final String CLIENT_RESOURCE_URI = "/clients";

    private final ClientService clientService;
    private final ClientSerializer clientSerializer;
    private final ClientDeserializer clientDeserializer;

    @Autowired
    public ClientResource(ClientService clientService, ClientSerializer clientSerializer,
                          ClientDeserializer clientDeserializer) {
        super(Client.class, CLIENT_RESOURCE_URI);

        Validate.notNull(clientService);
        Validate.notNull(clientSerializer);
        Validate.notNull(clientDeserializer);

        this.clientService = clientService;
        this.clientSerializer = clientSerializer;
        this.clientDeserializer = clientDeserializer;
    }


    @Override
    public Collection<ClientDto> convertToDtos(Collection<Client> domainObjects) {
        final List<ClientDto> clientDtos = new ArrayList<>();
        for (Client client : domainObjects) {
            final ClientDto clientDto = this.clientSerializer.convert(client);
            setLinks(clientDto, client);
            clientDtos.add(clientDto);
        }
        return clientDtos;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClientDto createClient(@RequestBody ClientDto clientDto, HttpServletResponse response) {
        Validate.notEmpty(clientDto.getEmail(), "Client's email cannot be empty!");
        Validate.isTrue(clientService.checkFreeEmail(clientDto.getEmail()),
                "Client with email=" + clientDto.getEmail() + " already exists!");
        final Client createdClient = this.clientService.create(clientDeserializer.convert(clientDto));
        response.setHeader("Location", CLIENT_RESOURCE_URI + "/" + createdClient.getBusinessUser().getEmail());
        final ClientDto createdClientDto = this.clientSerializer.convert(createdClient);
        setLinks(createdClientDto, createdClient);
        return createdClientDto;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ClientDto getClientById(@PathVariable Long id) {
        Validate.notNull(id, "client id has to be specified");
        final Client client = clientService.getById(id);
        final ClientDto clientDto = this.clientSerializer.convert(client);
        setLinks(clientDto, client);
        return clientDto;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ClientDto getClientByEmail(@RequestParam String email) {
        Validate.notEmpty(email, "email has to be specified");
        final Client client = getByEmailAndCheck(email);
        final ClientDto clientDto = this.clientSerializer.convert(client);
        setLinks(clientDto, client);
        return clientDto;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateClient(@PathVariable String id, @RequestBody ClientDto clientDto) {
//        getByEmailAndCheck(email);
        this.clientService.update(clientDeserializer.convert(clientDto));
    }

    private Client getByEmailAndCheck(String email) {
        final Client client = this.clientService.getByEmail(email);
        if (client == null) {
            throw new ResourceNotFoundException("No client with email=" + email + " has been found!");
        }
        return client;
    }

    //--------------------------------------------------- PRIVATE STUFF ------------------------------------------------
    // TODO juro: refactor out this stuff to common place ??
    private void setLinks(ClientDto clientDto, Client client) {
        clientDto.setLinks(ResourceUtils.generateSelfLinks(CLIENT_RESOURCE_URI, client));
    }

}
