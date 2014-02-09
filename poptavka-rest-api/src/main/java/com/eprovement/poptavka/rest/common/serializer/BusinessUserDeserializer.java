package com.eprovement.poptavka.rest.common.serializer;

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;
import com.eprovement.poptavka.service.register.RegisterService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates deserialization logic common for all type of {@link BusinessUser}-s, e.g.
 * {@link com.eprovement.poptavka.domain.user.Client} and {@link com.eprovement.poptavka.domain.user.Supplier}.
 *
 */
@Service
public class BusinessUserDeserializer implements Converter<BusinessUserDto, BusinessUser> {


    private final LocalityDeserializer localityDeserializer;
    private final RegisterService registerService;

    public BusinessUserDeserializer(LocalityDeserializer localityDeserializer, RegisterService registerService) {
        notNull(localityDeserializer, "localityDeserializer cannot be null");
        notNull(registerService, "registerService cannot be null");
        this.localityDeserializer = localityDeserializer;
        this.registerService = registerService;
    }

    @Override
    public BusinessUser convert(BusinessUserDto businessUserDto) {
        notNull(businessUserDto);
        notNull(businessUserDto);

        final BusinessUser businessUser = new BusinessUser();
        businessUser.setEmail(businessUserDto.getEmail());
        businessUser.setPassword(businessUserDto.getPassword());
        final BusinessUserData businessUserData = new BusinessUserData();
        businessUserData.setCompanyName(businessUserDto.getCompanyName());
        businessUserData.setPersonFirstName(businessUserDto.getPersonFirstName());
        businessUserData.setPersonLastName(businessUserDto.getPersonLastName());
        businessUserData.setPhone(businessUserDto.getPhone());
        businessUserData.setWebsite(businessUserDto.getWebsite());
        businessUser.setBusinessUserData(businessUserData);
        setAddresses(businessUserDto, businessUser);
        setOrigin(businessUserDto, businessUser);

        return businessUser;
    }

    private void setOrigin(BusinessUserDto businessUserDto, BusinessUser businessUser) {
        if (businessUserDto.getOrigin() != null) {
            final Origin origin = registerService.getValue(businessUserDto.getOrigin(), Origin.class);
            notNull(origin, String.format("No record found for origin code '%s'. "
                    + "Make sure that external system has been properly registrated",
                    businessUserDto.getOrigin()));
            businessUser.setOrigin(origin);
        }
    }

    /**
     * Converts addresses from {@code businessUserDto} and sets them to the {@code businessUser}.
     *
     * @param businessUserDto where addresses are retrieved from
     * @param businessUser where addresses are set to
     */
    private void setAddresses(BusinessUserDto businessUserDto, BusinessUser businessUser) {
        final List<Address> addresses = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(businessUserDto.getAddresses())) {
            for (LocalityDto addressDto : businessUserDto.getAddresses()) {
                final Locality addressLoc = localityDeserializer.convert(addressDto);
                isTrue(LocalityType.CITY == addressLoc.getType(), "expected city but found: " + addressLoc.getType());
                final Address address = new Address();
                address.setCity(addressLoc);
                address.setStreet(addressDto.getStreet());
                address.setHouseNum(addressDto.getHouseNum());
                address.setZipCode(addressDto.getZipCode());
                addresses.add(address);
            }
        }
        businessUser.setAddresses(addresses);
    }

}
