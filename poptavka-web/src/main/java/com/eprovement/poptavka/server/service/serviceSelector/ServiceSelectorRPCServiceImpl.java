/*
 * Copyright (C) 2013, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.serviceSelector;

import com.eprovement.poptavka.client.service.demand.ServiceSelectorRPCService;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.userservice.UserServiceService;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This RPC handles all requests from ServiceSelector module.
 * @author Martin Slavkovsky
 */
@Configurable
public class ServiceSelectorRPCServiceImpl extends AutoinjectingRemoteService
    implements ServiceSelectorRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Services. **/
    private GeneralService generalService;
    private UserServiceService userServiceService;
    /** Converters. **/
    private Converter<Service, ServiceDetail> serviceConverter;
    private Converter<UserService, UserServiceDetail> userServiceConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
    /**************************************************************************/
    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setUserServiceService(UserServiceService userServiceService) {
        this.userServiceService = userServiceService;
    }

    //Converters
    @Autowired
    public void setServiceConverter(
        @Qualifier("serviceConverter") Converter<Service, ServiceDetail> serviceConverter) {
        this.serviceConverter = serviceConverter;
    }

    @Autowired
    public void setUserServiceConverter(
        @Qualifier("userServiceConverter") Converter<UserService, UserServiceDetail> userServiceConverter) {
        this.userServiceConverter = userServiceConverter;
    }

    /**************************************************************************/
    /* Supplier Service methods                                               */
    /**************************************************************************/
    /**
     * Request supplier services
     * @param serviceTypes
     * @return list of service details
     * @throws RPCException
     */
    @Override
    public ArrayList<ServiceDetail> getSupplierServices(ServiceType... serviceTypes) throws RPCException {
        Search supplierServicesSearch = new Search(Service.class);
        supplierServicesSearch.addFilterEqual("valid", true);
        supplierServicesSearch.addFilterIn("serviceType", serviceTypes);
        // TODO LATER ivlcek - for now we work with Supplier services only. CLASSIC service will be added later
        supplierServicesSearch.addFilterNotEqual("code", Registers.Service.CLASSIC);
        List<Service> services = this.generalService.search(supplierServicesSearch);
        return serviceConverter.convertToTargetList(services);
    }

    /**
     * Creates UserService object to bind service to user.
     * @param userId
     * @param serviceDetail
     */
    @Override
    public UserServiceDetail createUserService(long userId, ServiceDetail serviceDetail) throws RPCException {
        //find given user and service
        final BusinessUser user = generalService.find(BusinessUser.class, userId);
        final Service service = generalService.find(Service.class, serviceDetail.getId());
        //create user service to bind service to user
        UserService userService = new UserService();
        userService.setService(service);
        userService.setStatus(Status.INACTIVE);
        userService.setBusinessUser(user);

        return userServiceConverter.convertToTarget(userServiceService.create(userService));
    }
}
