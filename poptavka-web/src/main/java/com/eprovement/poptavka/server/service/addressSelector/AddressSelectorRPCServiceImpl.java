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
    @Override
    public SuggestionResponse<AddressSuggestionDetail> getCityWithStateSuggestions(
            int requestId, String cityLike, int wordLength) throws RPCException {
        final List<Locality> localities =
                localityService.getLocalitiesByMinLength(wordLength, cityLike, LocalityType.CITY);
        final ArrayList<AddressSuggestionDetail> suggestions =
                addressSuggestionConverter.convertToTargetList(localities);
        return new SuggestionResponse(requestId, suggestions);
    }

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
