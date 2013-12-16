package com.eprovement.poptavka.rest.common.serializer;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.register.RegisterService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
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


    private final LocalityService localityService;
    private final RegisterService registerService;

    public BusinessUserDeserializer(LocalityService localityService, RegisterService registerService) {
        Validate.notNull(localityService, "localityService cannot be null");
        Validate.notNull(registerService, "registerService cannot be null");
        this.localityService = localityService;
        this.registerService = registerService;
    }

    @Override
    public BusinessUser convert(BusinessUserDto businessUserDto) {
        Validate.notNull(businessUserDto);
        Validate.notNull(businessUserDto);

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
            Validate.notNull(origin, String.format("No record found for origin code '%s'. "
                    + "Make sure that external system has geen properly registrated",
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
                final Address address = new Address();
                final com.eprovement.poptavka.domain.address.Locality city =
                        localityService.findCityByName(addressDto.getRegion(), addressDto.getCity());
                if (city == null) {
                    throw new IllegalArgumentException(
                            String.format("No city locality found for state name '%s' and city name '%s'",
                                    addressDto.getRegion(), addressDto.getCity()));
                }
                address.setCity(city);
                address.setStreet(addressDto.getStreet());
                address.setHouseNum(addressDto.getHouseNum());
                address.setZipCode(addressDto.getZipCode());
                addresses.add(address);
            }
        }
        businessUser.setAddresses(addresses);
    }

}
