/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.root;

import com.eprovement.poptavka.client.service.root.RootRPCService;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author Martin Slavkovsky
 * @since 9.4.2014
 */
@Configurable
public class RootRPCServiceImpl extends AutoinjectingRemoteService implements RootRPCService {

    @Autowired
    private GeneralService generalService;
    @Autowired
    private SupplierService supplierService;

    @Override
    public Boolean unsubscribe(String password) throws RPCException {
        Search search = new Search(BusinessUser.class);
        search.addFilterEqual("password", password);
        BusinessUser businessUser = (BusinessUser) generalService.searchUnique(search);
        for (NotificationItem notificationItem : businessUser.getSettings().getNotificationItems()) {
            notificationItem.setEnabled(Boolean.FALSE);
        }
        generalService.save(businessUser);
        //If given user is supplier, decrement supplier counts
        for (BusinessUserRole businessUserRole : businessUser.getBusinessUserRoles()) {
            if (businessUserRole instanceof Supplier) {
                supplierService.decrementSupplierCount(((Supplier) businessUserRole));
            }
        }
        return true;
    }

}
