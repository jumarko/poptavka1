package com.eprovement.poptavka.server.service.user.activation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.eprovement.poptavka.client.service.demand.UserActivationRPCService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.user.BusinessUserVerificationService;

@Component(UserActivationRPCService.URL)
public class UserActivationRPCServiceImpl extends AutoinjectingRemoteService implements UserActivationRPCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserActivationRPCServiceImpl.class);

    private BusinessUserVerificationService businessUserVerificationService;

    public UserActivationRPCServiceImpl(BusinessUserVerificationService businessUserVerificationService) {
        this.businessUserVerificationService = businessUserVerificationService;
    }

    public UserActivationRPCServiceImpl() {
        super();
    }

    @Override
    public BusinessUser activateUser(@RequestParam("link") String activationString) {
        LOGGER.debug("activationString " + activationString);
        final BusinessUser businessUser = businessUserVerificationService.verifyUser(activationString);

        return businessUser;
    }

}
