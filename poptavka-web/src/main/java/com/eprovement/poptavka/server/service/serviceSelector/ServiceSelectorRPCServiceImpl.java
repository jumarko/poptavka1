/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.serviceSelector;

import com.eprovement.poptavka.client.service.demand.ServiceSelectorRPCService;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Martin Slavkovsky
 */
@Configurable
public class ServiceSelectorRPCServiceImpl extends AutoinjectingRemoteService
        implements ServiceSelectorRPCService {

    private GeneralService generalService;

    //Converters
    private Converter<Service, ServiceDetail> serviceConverter;

    /**************************************************************************/
    /* Autowired methods                                                      */
    /**************************************************************************/
    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    //Converters
    @Autowired
    public void setServiceConverter(
            @Qualifier("serviceConverter") Converter<Service, ServiceDetail> serviceConverter) {
        this.serviceConverter = serviceConverter;
    }


    /**************************************************************************/
    /* Supplier Service methods                                               */
    /**************************************************************************/
    @Override
    public ArrayList<ServiceDetail> getSupplierServices(ServiceType serviceType) throws RPCException {
        Search supplierServicesSearch = new Search(Service.class);
        supplierServicesSearch.addFilterEqual("valid", true);
        supplierServicesSearch.addFilterEqual("serviceType", serviceType);
        List<Service> services = this.generalService.search(supplierServicesSearch);
        return serviceConverter.convertToTargetList(services);
    }
}