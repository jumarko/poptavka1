package cz.poptavka.sample.client.main;


import java.util.ArrayList;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.module.AfterLoadChildModule;
import com.mvp4g.client.annotation.module.BeforeLoadChildModule;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.HomeModule;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter;
import cz.poptavka.sample.client.main.common.creation.FormDemandAdvPresenter;
import cz.poptavka.sample.client.main.common.creation.FormDemandBasicPresenter;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.client.user.UserModule;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Events(startView = MainView.class, historyOnStart = true)
@ChildModules({
    @ChildModule(moduleClass = HomeModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = UserModule.class, async = true, autoDisplay = false)
    })
public interface MainEventBus extends EventBus {

    /**
     * Init method for layout and history initialization. Layout initialization
     * contains menu injection.
     * To inject more widgets, update onStart() method of MainPresenter
     */
    @InitHistory
    @Event(handlers = MainPresenter.class)
    void start();

    /** Init home module (unlogged user). */
    @Event(modulesToLoad = HomeModule.class)
    void atHome();

    /** Init user module (logged user). */
    @Event(modulesToLoad = UserModule.class)
    void atAccount();
//
//    NEEDED ?

//    @Event(modulesToLoad = UserModule.class)
//    void setTabWidget(Widget widget);

    @Event(modulesToLoad = HomeModule.class)
    void atCreateDemand();

    @Event(modulesToLoad = HomeModule.class)
    void atRegisterSupplier();

    /**
     * Sets widget to View's body section. Body section can hold one widget only.
     *
     * @param body
     */
    @Event(handlers = MainPresenter.class)
    void setBodyHolderWidget(Widget body);

    @BeforeLoadChildModule
    @Event(handlers = MainPresenter.class)
    void beforeLoad();

    @AfterLoadChildModule
    @Event(handlers = MainPresenter.class)
    void afterLoad();

    /** Demand Creation common method calls */
    @Event(handlers = FormDemandBasicPresenter.class)
    void initDemandBasicForm(SimplePanel holderWidget);

    @Event(handlers = FormDemandAdvPresenter.class)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(handlers = MainHandler.class)
    void createDemand(DemandDetail detail, Long clientId);

    /** CategorySelection section **/
    @Event(handlers = CategorySelectorPresenter.class)
    void initCategoryWidget(SimplePanel embedToWidget);

    @Event(handlers = CategorySelectorPresenter.class)
    void setCategoryListData(int newListPosition, ArrayList<CategoryDetail> list);

    @Event(handlers = MainHandler.class)
    void getRootCategories();

    @Event(handlers = MainHandler.class)
    void getChildListCategories(int newListPosition, String categoryId);

    /** LocalitySelector section **/
    @Event(handlers = LocalitySelectorPresenter.class)
    void initLocalityWidget(SimplePanel embedToWidget);

    @Event(handlers = LocalitySelectorPresenter.class)
    void setLocalityData(int localityType, ArrayList<LocalityDetail> localityList);

    @Event(handlers = MainHandler.class)
    void getRootLocalities();

    @Event(handlers = MainHandler.class)
    void getChildLocalities(int localityType, String locCode);

    @Event(handlers = MainPresenter.class)
    void compactModeCheck();

    @Event(broadcastTo = CompactModeBroadcast.class, passive = true)
    void compactModeResponse(Boolean compactDisplay);

    /** NO EDITING AFTER THIS LINE
     * Every Child Module HAVE TO implement this method calls.
     * Popup methods for shoving, changing text and hiding, for letting user know, that application is still working.
     */
    @Event(handlers = MainPresenter.class)
    void loadingShow(String loadingMessage);

    @Event(handlers = MainPresenter.class)
    void loadingHide();

    /** Layout change calls. */
    @Event(handlers = MainPresenter.class)
    void setPublicLayout();

    @Event(handlers = MainPresenter.class)
    void setUserLayout();

}
