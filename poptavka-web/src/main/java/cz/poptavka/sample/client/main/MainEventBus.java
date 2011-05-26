package cz.poptavka.sample.client.main;


import java.util.ArrayList;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.module.AfterLoadChildModule;
import com.mvp4g.client.annotation.module.BeforeLoadChildModule;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.HomeModule;
import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.client.main.login.LoginPopupPresenter;
import cz.poptavka.sample.client.user.UserModule;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Events(startView = MainView.class, historyOnStart = true)
@Debug(logLevel = Debug.LogLevel.DETAILED)
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

    /** Login popup display **/
    @Event(handlers = LoginPopupPresenter.class)
    void login();

    /** Init home module (unlogged user). */
    @Event(modulesToLoad = HomeModule.class)
    void atHome();

    /** init method for Authentificated User. */
    //should be only for UserPresenter
    // TODO MainPage login button text, should be handled by children modules
    @Event(modulesToLoad = UserModule.class, handlers = MainPresenter.class)
    void atAccount();

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
    @Event(handlers = MainPresenter.class)
    void initDemandBasicForm(SimplePanel holderWidget);

    @Event(handlers = MainPresenter.class)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(handlers = MainHandler.class)
    void createDemand(DemandDetail detail, Long clientId);

    /** CategorySelection section **/
    @Event(handlers = MainPresenter.class)
    void initCategoryWidget(SimplePanel embedToWidget);

    @Event(handlers = CategorySelectorPresenter.class)
    void setCategoryListData(int newListPosition, ArrayList<CategoryDetail> list);

    @Event(handlers = MainHandler.class)
    void getRootCategories();

    @Event(handlers = MainHandler.class)
    void getChildListCategories(int newListPosition, String categoryId);

    /** LocalitySelector section **/
    @Event(handlers = MainPresenter.class)
    void initLocalityWidget(SimplePanel embedToWidget);

    @Event(handlers = LocalitySelectorPresenter.class)
    void setLocalityData(int localityType, ArrayList<LocalityDetail> localityList);

    @Event(handlers = MainHandler.class)
    void getRootLocalities();

    @Event(handlers = MainHandler.class)
    void getChildLocalities(int localityType, String locCode);

    /** Supplier registration **/
    @Event(handlers = MainPresenter.class)
    void initServiceForm(SimplePanel serviceHolder);

    @Event(handlers = MainPresenter.class)
    void initSupplierForm(SimplePanel supplierInfoHolder);

    @Event(handlers = MainHandler.class)
    void checkFreeEmail(String value);

    @Event(handlers = SupplierInfoPresenter.class,
            modulesToLoad = HomeModule.class)
    void checkFreeEmailResponse(Boolean result);

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
