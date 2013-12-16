package com.eprovement.poptavka.rest.client;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientSerializer implements Converter<Client, ClientDto> {

    private final Converter<BusinessUser, BusinessUserDto> businessUserSerializer;


    @Autowired
    public ClientSerializer(Converter<BusinessUser, BusinessUserDto> businessUserSerializer) {
        Validate.notNull(businessUserSerializer);
        this.businessUserSerializer = businessUserSerializer;
    }

    @Override
    // need to be transactional because it calls client.getBusinessUser
    @Transactional(readOnly = true)
    public ClientDto convert(Client client) {
        Validate.notNull(client);

        final ClientDto clientDto = new ClientDto();

        clientDto.setId(client.getId());
        final BusinessUserDto businessUserDto = businessUserSerializer.convert(client.getBusinessUser());
        clientDto.setEmail(businessUserDto.getEmail());
        clientDto.setPersonFirstName(businessUserDto.getPersonFirstName());
        clientDto.setPersonLastName(businessUserDto.getPersonLastName());
        clientDto.setCompanyName(businessUserDto.getCompanyName());
        clientDto.setAddresses(businessUserDto.getAddresses());
        clientDto.setOverallRating(client.getOveralRating());

        return clientDto;
    }

}
