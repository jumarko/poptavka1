/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.addressSelector;

import com.eprovement.poptavka.client.service.demand.AddressSelectorRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.selectors.addressSelector.AddressSuggestionDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.selectors.SuggestionResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This RPC handles all requests for AddressSelector module.
 * @author Martin Slavkovsky
 */
@Configurable
public class AddressSelectorRPCServiceImpl extends AutoinjectingRemoteService implements AddressSelectorRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private LocalityService localityService;
    private Converter<Locality, AddressSuggestionDetail> addressSuggestionConverter;

    /**************************************************************************/
    /* Autowired services                                                     */
    /**************************************************************************/
    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    /**************************************************************************/
    /* Autowired converters                                                   */
    /**************************************************************************/
    @Autowired
    public void setAddressSuggestionConverter(@Qualifier("addressSuggestionConverter")
            Converter<Locality, AddressSuggestionDetail> addressSuggestionConverter) {
        this.addressSuggestionConverter = addressSuggestionConverter;
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Request for city with their state suggestions.
     * Returns cities which length is greater or equal than given wordLength restriction.
     * @param requestId
     * @param cityLike string
     * @param wordLength - city length restriction
     * @return suggestions
     * @throws RPCException
     */
    @Override
    public SuggestionResponse<AddressSuggestionDetail> getCityWithStateSuggestions(
            int requestId, String cityLike, int wordLength) throws RPCException {
        final List<Locality> localities =
                localityService.getLocalitiesByMinLength(wordLength, cityLike, LocalityType.CITY);
        final ArrayList<AddressSuggestionDetail> suggestions =
                addressSuggestionConverter.convertToTargetList(localities);
        return new SuggestionResponse(requestId, suggestions);
    }

    /**
     * Request for short city with their state suggestions.
     * Short cities are those which length is smaller than given wordLength restriction.
     * @param requestId
     * @param cityLike
     * @param wordLength - city length restriction
     * @return suggestions
     * @throws RPCException
     */
    @Override
    public SuggestionResponse<AddressSuggestionDetail> getShortCityWithStateSuggestions(
            int requestId, String cityLike, int wordLength) throws RPCException {
        final List<Locality> localities =
                localityService.getLocalitiesByMaxLengthExcl(wordLength, cityLike, LocalityType.CITY);
        final ArrayList<AddressSuggestionDetail> suggestions =
                addressSuggestionConverter.convertToTargetList(localities);
        return new SuggestionResponse(requestId, suggestions);
    }
}
