package cz.poptavka.sample.client.home;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.creation.DemandCreationPresenter;
import cz.poptavka.sample.client.home.creation.FormLoginPresenter;
import cz.poptavka.sample.client.home.creation.FormUserRegistrationPresenter;
import cz.poptavka.sample.client.home.demands.DemandsModule;
import cz.poptavka.sample.client.home.supplier.SupplierCreationPresenter;
import cz.poptavka.sample.client.home.suppliers.DisplaySuppliersModule;
import cz.poptavka.sample.client.home.widget.category.CategoryDisplayPresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;


@Events(startView = HomeView.class, module = HomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
@ChildModules({
        @ChildModule(moduleClass = DemandsModule.class, autoDisplay = false, async = true),
        @ChildModule(moduleClass = DisplaySuppliersModule.class, autoDisplay = false, async = true)
})
public interface HomeEventBus extends EventBus {

    /** init method  **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    String atHome();

    /** init method. **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    void displayMenu();

    /** Display HomeView - parent Widget for public section. **/
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /**
     * Assign widget to selected part and automatically removes previous widget. Optionally can remove widgets from
     * others anchors
     *
     * @param content
     */
    @Event(handlers = HomePresenter.class)
    void setBodyWidget(Widget content);

    /** navigation events  */
    @Event(handlers = DemandCreationPresenter.class, historyConverter = HomeHistoryConverter.class)
    String atCreateDemand();

    @Event(modulesToLoad = DemandsModule.class, historyConverter = HomeHistoryConverter.class)
    String atDemands();

    @Event(modulesToLoad = DisplaySuppliersModule.class, historyConverter = HomeHistoryConverter.class)
    String atSuppliers();

    @Event(handlers = SupplierCreationPresenter.class, historyConverter = HomeHistoryConverter.class)
    String atRegisterSupplier();

    /** main module calls - common widgets */
    @Event(forwardToParent = true)
    void initDemandBasicForm(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initServiceForm(SimplePanel serviceHolder);

    @Event(forwardToParent = true)
    void initSupplierForm(SimplePanel supplierInfoHolder);

    /** main module calls - Handler calls */
    @Event(forwardToParent = true)
    void createDemand(FullDemandDetail newDemand, Long clientId);

    @Event(forwardToParent = true)
    void getRootCategories();

    @Event(forwardToParent = true)
    void checkFreeEmail(String value);

    /** demand creation related events **/
    @Event(handlers = FormLoginPresenter.class)
    void initLoginForm(SimplePanel holderWidget);

    @Event(handlers = DemandCreationPresenter.class)
    void toggleLoginRegistration();

    @Event(handlers = FormUserRegistrationPresenter.class, passive = true)
    void checkFreeEmailResponse(Boolean result);
//    void checkFreeEmailResponse();

    @Event(handlers = FormUserRegistrationPresenter.class)
    void initRegistrationForm(SimplePanel holderWidget);
    //logic flow order representing registering client and then creating his demand
    @Event(handlers = HomeHandler.class)
    void registerNewClient(UserDetail newClient);

    @Event(handlers = DemandCreationPresenter.class)
    void prepareNewDemandForNewClient(UserDetail client);

    //alternative way of loging - verifying
    @Event(handlers = HomeHandler.class)
    void verifyExistingClient(UserDetail client);
    //error output
    @Event(handlers = DemandCreationPresenter.class)
    void loginError();


    /** Home category display widget and related call */
    @Event(handlers = CategoryDisplayPresenter.class)
    void initCategoryDisplay(SimplePanel holderWidget);

    @Event(handlers = CategoryDisplayPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(ArrayList<CategoryDetail> list);

    /** Supplier registration **/
    @Event(handlers = HomeHandler.class)
    void registerSupplier(UserDetail newSupplier);

    /** Common method calls **/

    /** DO NOT EDIT **/

    /** Method for setting public UI layout. */
    @Event(forwardToParent = true)
    void setPublicLayout();

    /** Popup methods for shoving, changing text and hiding,
     * for letting user know, that application is still working.
     * Every Child Module HAVE TO implement this method calls.
     * Popup methods for shoving, changing text and hiding, for letting user know, that application is still working.
     */
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();
    /** NO METHODS AFTER THIS **/

}
