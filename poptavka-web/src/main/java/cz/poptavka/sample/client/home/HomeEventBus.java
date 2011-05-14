package cz.poptavka.sample.client.home;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.home.creation.DemandCreationPresenter;
import cz.poptavka.sample.client.home.creation.FormLoginPresenter;
import cz.poptavka.sample.client.home.creation.FormUserRegistrationPresenter;
import cz.poptavka.sample.client.home.demands.DemandsModule;
import cz.poptavka.sample.client.home.supplier.SupplierCreationPresenter;
import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter;
import cz.poptavka.sample.client.home.supplier.widget.SupplierServicePresenter;
import cz.poptavka.sample.client.home.widget.category.CategoryDisplayPresenter;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter.CategoryType;
import cz.poptavka.sample.client.user.problems.MyProblemsModule;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;


@Events(startView = HomeView.class, module = HomeModule.class)
@ChildModules({
        @ChildModule(moduleClass = DemandsModule.class, autoDisplay = false, async = true),
        @ChildModule(moduleClass = MyProblemsModule.class, autoDisplay = false, async = true)
        })
public interface HomeEventBus extends EventBus {

    /** init method. **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    void displayMenu();

    /** init method. **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    String atHome();

    /** Method for setting public UI layout. */
    @Event(forwardToParent = true)
    void setPublicLayout();

    /**
     * Display HomeView - parent Widget for public section.
     *
     * @param body
     */
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /**
     * Assign widget to selected part and automatically removes previous widget. Optionally can remove widgets from
     * others anchors
     *
     * @param anchor to be connected to
     * @param content
     * @param clearOthers if true, removes widgets from other anchors
     */
    @Event(handlers = HomePresenter.class)
    void setHomeWidget(AnchorEnum anchor, Widget content, boolean clearOthers);

    /** Demand creation */
    @Event(handlers = DemandCreationPresenter.class, historyConverter = HomeHistoryConverter.class)
    String atCreateDemand();

    @Event(forwardToParent = true)
    void initDemandBasicForm(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(handlers = FormLoginPresenter.class)
    void initLoginForm(SimplePanel holderWidget);

    @Event(handlers = DemandCreationPresenter.class)
    void toggleLoginRegistration();

    @Event(handlers = FormUserRegistrationPresenter.class)
    void initRegistrationForm(SimplePanel holderWidget);

    //logic flow order representing registering client and then creating his demand
    @Event(handlers = HomeHandler.class)
    void registerNewClient(ClientDetail newClient);

    @Event(handlers = DemandCreationPresenter.class)
    void prepareNewDemandForNewClient(long clientId);

    @Event(forwardToParent = true)
    void createDemand(DemandDetail newDemand, Long clientId);

    //alternative way of loging - verifying
    @Event(handlers = HomeHandler.class)
    void verifyExistingClient(ClientDetail client);

    //error output
    @Event(handlers = DemandCreationPresenter.class)
    void loginError();


    /** Home category display widget and related call */
    @Event(handlers = CategoryDisplayPresenter.class)
    void initCategoryDisplay(AnchorEnum anchor);

    @Event(handlers = CategoryDisplayPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(CategoryType type, ArrayList<CategoryDetail> list);

    @Event(forwardToParent = true)
    void getRootCategories();

    /** TODO Martin Sl. - move to correct place */
    /** display MyProblems module */
    @Event(modulesToLoad = MyProblemsModule.class)
    void displayProblems();

    /** Supplier registration **/
    @Event(handlers = SupplierServicePresenter.class)
    void initServiceForm(SimplePanel serviceHolder);

    @Event(handlers = SupplierInfoPresenter.class)
    void initSupplierForm(SimplePanel supplierInfoHolder);

    @Event(handlers = HomeHandler.class)
    void registerSupplier(SupplierDetail newSupplier);

    /** Common method calls **/
    @Event(handlers = HomeHandler.class)
    void checkFreeEmail(String value);

    @Event(handlers = {FormUserRegistrationPresenter.class })
    void checkFreeEmailResponse(boolean result);


    @Event(modulesToLoad = DemandsModule.class, historyConverter = HomeHistoryConverter.class)
    String atDemands();

    @Event(handlers = SupplierCreationPresenter.class, historyConverter = HomeHistoryConverter.class)
    String atRegisterSupplier();

    /** DO NOT EDIT **/
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
