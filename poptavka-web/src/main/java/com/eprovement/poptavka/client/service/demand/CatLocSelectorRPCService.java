package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocTreeItem;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.selectors.SuggestionResponse;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocSuggestionDetail;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import java.util.LinkedList;

@RemoteServiceRelativePath(CatLocSelectorRPCService.URL)
public interface CatLocSelectorRPCService extends RemoteService {

    String URL = "service/catLocSelector";

    ICatLocDetail getItem(int selectorType, long id) throws RPCException;

    List<ICatLocDetail> getItemChildren(int selectorType, long id) throws RPCException;

    List<ICatLocDetail> getRootItems(int selectorType) throws RPCException;

    LinkedList<CatLocTreeItem> requestHierarchy(int selectorType, ICatLocDetail detail) throws RPCException;

    SuggestionResponse<CatLocSuggestionDetail> getSuggestions(
            int requestId, int selectorType, String itemLike, int wordLength) throws RPCException;

    SuggestionResponse<CatLocSuggestionDetail> getShortSuggestions(
            int requestId, int selectorType, String itemLike, int wordLength) throws RPCException;
}
