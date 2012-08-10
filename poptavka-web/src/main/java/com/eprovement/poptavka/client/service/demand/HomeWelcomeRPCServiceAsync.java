package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

public interface HomeWelcomeRPCServiceAsync {

    void getRootCategories(AsyncCallback<ArrayList<CategoryDetail>> callback);
}
