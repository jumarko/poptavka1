package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ILesserCatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

@RemoteServiceRelativePath(HomeWelcomeRPCService.URL)
public interface HomeWelcomeRPCService extends RemoteService {

    String URL = "service/homewelcome";

    ArrayList<ILesserCatLocDetail> getRootCategories() throws RPCException;

    ICatLocDetail getICatLocDetail(long categoryId) throws RPCException;
}
