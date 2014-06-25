package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.selectors.SuggestionResponse;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocSuggestionDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.LinkedList;

public interface CatLocSelectorRPCServiceAsync {

    void getItem(int selectorType, long id, AsyncCallback<ICatLocDetail> callback);

    void getRootItems(int selectorType, AsyncCallback<List<ICatLocDetail>> callback);

    void getItemChildren(int selectorType, long id, AsyncCallback<List<ICatLocDetail>> callback);

    void requestHierarchy(int selectorType, ICatLocDetail detail, AsyncCallback<LinkedList<ICatLocDetail>> callback);

    void getSuggestions(int requestId, int selectorType, String itemLike, int wordLength,
            AsyncCallback<SuggestionResponse<CatLocSuggestionDetail>> callback);

    void getShortSuggestions(int requestId, int selectorType, String itemLike, int wordLength,
            AsyncCallback<SuggestionResponse<CatLocSuggestionDetail>> callback);
}
