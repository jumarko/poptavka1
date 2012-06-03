/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.user;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.service.user.BusinessUserVerificationService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(UserActivationResource.USER_ACTIVATION_RESOURCE_URI)
public class UserActivationResource {

    public static final String USER_ACTIVATION_RESOURCE_URI = "/user/activation";

    private final BusinessUserVerificationService verificationService;

    @Autowired
    public UserActivationResource(BusinessUserVerificationService userVerificationService) {
        Validate.notNull(userVerificationService);
        this.verificationService = userVerificationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String getSupplierById(@RequestParam("link") String activationLink) {
        Validate.notEmpty(activationLink);

        final BusinessUser businessUser = verificationService.verifyUser(activationLink);
        
        if(businessUser != null) {
            return "redirect:/Activation.html";
        } else {
            return null;
        }
        
        //return "User [email=" + businessUser.getEmail() + " has been activated successfully.";
    }



}
