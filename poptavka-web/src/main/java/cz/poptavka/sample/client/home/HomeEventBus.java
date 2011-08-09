package cz.poptavka.sample.client.home;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.creation.DemandCreationPresenter;
import cz.poptavka.sample.client.home.creation.FormLoginPresenter;
import cz.poptavka.sample.client.home.creation.FormUserRegistrationPresenter;
import cz.poptavka.sample.client.home.demands.DemandsHandler;
import cz.poptavka.sample.client.home.demands.DemandsPresenter;
import cz.poptavka.sample.client.home.supplier.SupplierCreationPresenter;
import cz.poptavka.sample.client.home.suppliers.SuppliersHandler;
import cz.poptavka.sample.client.home.suppliers.RootPresenter;
import cz.poptavka.sample.client.home.suppliers.SuppliersPresenter;
import cz.poptavka.sample.client.home.widget.category.CategoryDisplayPresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.DemandDetailForDisplayDemands;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.Collection;


@Events(startView = HomeView.class, module = HomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
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

//    @Event(modulesToLoad = DemandsModule.class, historyConverter = HomeHistoryConverter.class)
    @Event(handlers = DemandsPresenter.class, historyConverter = HomeHistoryConverter.class)
    String atDemands();

//    @Event(handlers = ????.class, historyConverter = HomeHistoryConverter.class)
//    String atAttachement();

//    @Event(handlers = ????, historyConverter = HomeHistoryConverter.class)
//    String atLogin();

    //Display root categories
    @Event(handlers = RootPresenter.class, historyConverter = HomeHistoryConverter.class)
    String atSuppliers();

    //Display subcategories, suppliers of selected category and detail of selected supplier
    @Event(handlers = SuppliersPresenter.class)
    void atDisplaySuppliers(Long categoryID);

//    @Event(handlers = RootPresenter.class, historyConverter = HomeHistoryConverter.class)
//    String createToken(String token);

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

    /** DISPLAT SUPPLIERS */
    //Category
    @Event(handlers = SuppliersHandler.class)
    void getSubCategories(Long category);

    @Event(handlers = {DemandsHandler.class, SuppliersHandler.class })
    void getCategories();

    //Locality
    @Event(handlers = {DemandsHandler.class, SuppliersHandler.class })
    void getLocalities();

    //Suppliers
//    @Event(handlers = RootPresenter.class)
//    void getSuppliers(Long category, Long locality);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersByCategoryLocality(int start, int count, Long category, String locality);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersByCategory(int start, int count, Long category);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersCount(Long category, String locality);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersCountByCategory(Long category);

    //Display
    @Event(handlers = RootPresenter.class)
    void displayRootcategories(ArrayList<CategoryDetail> list);

    @Event(handlers = SuppliersPresenter.class)
    void displaySubCategories(ArrayList<CategoryDetail> list, Long parentCategory);

    @Event(handlers = SuppliersPresenter.class)
    void displaySuppliers(ArrayList<UserDetail> list);

    @Event(handlers = {SuppliersPresenter.class, DemandsPresenter.class })
    void setLocalityData(ArrayList<LocalityDetail> list);

    @Event(handlers = SuppliersPresenter.class, historyConverter = HomeHistoryConverter.class)
    void addToPath(CategoryDetail category);

    @Event(handlers = SuppliersPresenter.class)
    void removeFromPath(Long code);

    @Event(handlers = SuppliersPresenter.class)
    void setCategoryID(Long categoryCode);

    @Event(handlers = SuppliersPresenter.class)
    void createAsyncDataProviderSupplier(final long totalFound);

    //DISPLAY DEMANDS
    //Demand
    @Event(handlers = DemandsHandler.class)
    void getDemands(int fromResult, int toResult);

    @Event(handlers = DemandsHandler.class)
    void getAllDemandsCount();

    @Event(handlers = DemandsHandler.class)
    void getDemandsCountCategory(long id);

    @Event(handlers = DemandsHandler.class)
    void getDemandsCountLocality(String code);

    @Event(handlers = DemandsHandler.class)
    void getDemandsByCategories(int fromResult, int toResult, long id);

    @Event(handlers = DemandsHandler.class)
    void getDemandsByLocalities(int fromResult, int toResult, String id);

    //Display
    @Event(handlers = DemandsPresenter.class)
    void setCategoryData(ArrayList<CategoryDetail> list);

    @Event(handlers = DemandsPresenter.class)
    void displayDemands(Collection<DemandDetailForDisplayDemands> result);

    @Event(handlers = DemandsPresenter.class)
    void setDemand(DemandDetailForDisplayDemands demand);

    @Event(handlers = DemandsPresenter.class)
    void createAsyncDataProvider();

    @Event(handlers = DemandsPresenter.class)
    void setResultSource(String resultSource);

    @Event(handlers = DemandsPresenter.class)
    void setResultCount(long resultCount);
}
