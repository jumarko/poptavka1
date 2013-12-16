package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

public interface HomeWelcomeRPCServiceAsync {

    void getRootCategories(AsyncCallback<ArrayList<ICatLocDetail>> callback);
}
