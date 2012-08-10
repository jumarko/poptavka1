package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

@RemoteServiceRelativePath(HomeWelcomeRPCService.URL)
public interface HomeWelcomeRPCService extends RemoteService {

    String URL = "service/homewelcome";

    ArrayList<CategoryDetail> getRootCategories() throws RPCException;
}
