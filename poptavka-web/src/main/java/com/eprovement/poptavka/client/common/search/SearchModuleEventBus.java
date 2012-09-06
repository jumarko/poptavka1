package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = SearchModulePresenter.class, module = SearchModule.class)
public interface SearchModuleEventBus extends EventBus, BaseChildEventBus {

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    @Event(handlers = SearchModulePresenter.class)
    void goToSearchModule();

    /**************************************************************************/
    /* Parent events - navigation events                                      */
    /**************************************************************************/
    // TODO praso - preco tu mame tieto metody. Vsetko su to navigacne eventy mali
    // by predsa byt v HomeMenu. Naco ich mat dva krat v dvoch presenteroch?
    // IV: tak pohopil som, ze su nutne kvoli vysledkom, ktore sa moju zobrazit po
    // zadani vyhladavacich kriterii do search panelu. Po kliknuti search uzivatelia
    // presmerujeme na jeden z pohladov suppliers/demands/messages
    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToClientDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToSupplierDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToAdminModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToMessagesModule(SearchModuleDataHolder filter, int loadWidget);

    /**************************************************************************/
    /* Additional widgets - init events                                       */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holder, int checkboxes);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel embedWidget);

    @Event(handlers = AdvanceSearchContentPresenter.class)
    void initAdvanceSearchContent(SimplePanel embedWidget, IsWidget attributeSelector);

    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = SearchModulePresenter.class)
    void clearSearchContent();
}
