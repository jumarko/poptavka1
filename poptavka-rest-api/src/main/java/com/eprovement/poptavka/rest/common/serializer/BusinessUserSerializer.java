package com.eprovement.poptavka.rest.common.serializer;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class BusinessUserSerializer implements Converter<BusinessUser, BusinessUserDto> {

    @Override
    public BusinessUserDto convert(BusinessUser businessUser) {
        Validate.notNull(businessUser);
        Validate.notNull(businessUser.getBusinessUserData());

        final BusinessUserDto businessUserDto = new BusinessUserDto();
        businessUserDto.setOrigin(businessUser.getOrigin() != null ? businessUser.getOrigin().getCode() : null);
        businessUserDto.setEmail(businessUser.getEmail());
        businessUserDto.setCompanyName(businessUser.getBusinessUserData().getCompanyName());
        businessUserDto.setPersonFirstName(businessUser.getBusinessUserData().getPersonFirstName());
        businessUserDto.setPersonLastName(businessUser.getBusinessUserData().getPersonLastName());
        businessUserDto.setPhone(businessUser.getBusinessUserData().getPhone());
        businessUserDto.setWebsite(businessUser.getBusinessUserData().getWebsite());
        setAddresses(businessUser, businessUserDto);

        return businessUserDto;
    }

    /**
     * Converts addresses from {@code businessUser} and sets them to the {@code businessUserDto}.
     *
     * @param businessUser where addresses are retrieved from
     * @param businessUserDto where addresses are set to
     */
    private void setAddresses(BusinessUser businessUser, BusinessUserDto businessUserDto) {
        if (CollectionUtils.isNotEmpty(businessUser.getAddresses())) {
            for (Address address : businessUser.getAddresses()) {
                final LocalityDto addressDto = new LocalityDto();
                addressDto.setRegion(address.getCity() != null
                        ? address.getCity().getParent().getParent().getName() : null);
                addressDto.setDistrict(address.getCity() != null ? address.getCity().getParent().getName() : null);
                addressDto.setCity(address.getCity() != null ? address.getCity().getName() : null);
                addressDto.setStreet(address.getStreet());
                addressDto.setHouseNum(address.getHouseNum());
                addressDto.setZipCode(address.getZipCode());
                businessUserDto.addAddress(addressDto);
            }
        }
    }
}
