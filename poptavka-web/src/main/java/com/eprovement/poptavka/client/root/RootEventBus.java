/*
 * RootEventBus servers all events for foot module. This is the starting
 * EventBus that handled the very first event in the app.
 *
 * Root Module countains all child modules, and all initial presenters like
 * Header, HomeMenu, UserMenu, SearchPanel, HomeBody, UserBody, Footer.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/
 */
package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.common.search.SearchModule;
import com.eprovement.poptavka.client.common.services.ServicesSelectorPresenter;
import com.eprovement.poptavka.client.common.userRegistration.UserRegistrationFormPresenter;
import com.eprovement.poptavka.client.error.ErrorModule;
import com.eprovement.poptavka.client.home.createDemand.DemandCreationModule;

import com.eprovement.poptavka.client.home.createSupplier.SupplierCreationModule;
import com.eprovement.poptavka.client.homeWelcome.HomeWelcomeModule;
import com.eprovement.poptavka.client.homedemands.HomeDemandsModule;
import com.eprovement.poptavka.client.homesuppliers.HomeSuppliersModule;
import com.eprovement.poptavka.client.root.activation.ActivationCodePopupPresenter;
import com.eprovement.poptavka.client.root.email.EmailDialogPopupPresenter;
import com.eprovement.poptavka.client.root.footer.FooterPresenter;
import com.eprovement.poptavka.client.root.header.HeaderPresenter;
import com.eprovement.poptavka.client.root.header.UserHeaderPresenter;
import com.eprovement.poptavka.client.root.menu.MenuPresenter;
import com.eprovement.poptavka.client.root.menu.UserMenuPresenter;
import com.eprovement.poptavka.client.user.admin.AdminModule;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModule;
import com.eprovement.poptavka.client.user.messages.MessagesModule;
import com.eprovement.poptavka.client.user.settings.SettingsModule;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModule;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.NotFoundHistory;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.module.AfterLoadChildModule;
import com.mvp4g.client.annotation.module.BeforeLoadChildModule;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.annotation.module.DisplayChildModuleView;
import com.mvp4g.client.annotation.module.LoadChildModuleError;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.ArrayList;
import java.util.List;

@Events(startPresenter = RootPresenter.class, historyOnStart = true)
@Debug(logLevel = Debug.LogLevel.DETAILED)
@ChildModules({
    @ChildModule(moduleClass = HomeWelcomeModule.class, async = false, autoDisplay = true),
    @ChildModule(moduleClass = SearchModule.class, async = false, autoDisplay = true),
    @ChildModule(moduleClass = DemandCreationModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = SupplierCreationModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = HomeDemandsModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = HomeSuppliersModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = ClientDemandsModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = SupplierDemandsModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = MessagesModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = SettingsModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = ErrorModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = AdminModule.class, async = true, autoDisplay = false) })
public interface RootEventBus extends EventBusWithLookup {

    /**
     * When your application starts, you may want to automatically fire an event
     * so that actions needed at first can occur.
     */
    @Start
    @InitHistory
    @Event(handlers = {RootPresenter.class, FooterPresenter.class })
    void start();

    /**************************************************************************/
    /* Layout events.                                                         */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void setHeader(IsWidget header);

    @Event(handlers = RootPresenter.class)
    void setMenu(IsWidget menu);

    @DisplayChildModuleView(SearchModule.class)
    @Event(handlers = RootPresenter.class)
    void setSearchBar(IsWidget searchBar);

    @Event(handlers = RootPresenter.class)
    void setUpSearchBar(IsWidget advanceSearchWidget);

    /**
     * Pouzitie autodisplay funkcie v RootModule ma za nasledok, ze kazdy modul sa
     * automaticky nastavi do RootPresentera cez metodu setBody(), ktora reprezentuje
     * hlavne telo webstranky. Je nutne anotovat tuto metody aby RootModul vedel,
     * ktora metoda ma nahrat pohlad ChildModulu a zobrazit na webstranke
     */
    @DisplayChildModuleView({
        HomeWelcomeModule.class,
        ErrorModule.class })
    @Event(handlers = RootPresenter.class)
    void setBody(IsWidget body);

    @Event(handlers = RootPresenter.class)
    void setFooter(IsWidget footer);

    @Event(handlers = FooterPresenter.class)
    void setDefaultFooterStyle();

    @Event(handlers = FooterPresenter.class)
    void setExtendedFooterStyle();
    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**************************************************************************/
    /* Navigation events - Home menu control section                          */
    /**************************************************************************/
    @Event(forwardToModules = HomeWelcomeModule.class)
    void goToHomeWelcomeModule();

    @Event(forwardToModules = HomeDemandsModule.class)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToModules = HomeDemandsModule.class)
    void goToHomeDemandsModuleFromWelcome(int categoryIdx, CategoryDetail category);

    @Event(forwardToModules = HomeSuppliersModule.class)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    // TODO martin - Preco v tychto metodach nepouzivas filter? Search Bar predsa bude aj v tychto
    // pohladoch. Alebo je tam nejaky default filter?
    @Event(forwardToModules = SupplierCreationModule.class)
    void goToCreateSupplierModule();

    // TODO martin - Preco v tychto metodach nepouzivas filter? Search Bar predsa bude aj v tychto
    // pohladoch. Alebo je tam nejaky default filter?
    @Event(forwardToModules = DemandCreationModule.class)
    void goToCreateDemandModule();

    /**
     * Forward error handling to Error Module that will display proper messages to user.
     *
     * @param errorResponseCode - HTTP error response code if available, otherwise use value 0
     * @param errorId - errorId if available, otherwise use null
     */
    @Event(forwardToModules = ErrorModule.class)
    void displayError(int errorResponseCode, String errorId);

    /**************************************************************************/
    /* Navigation events - User menu control section                          */
    /**************************************************************************/
    @Event(forwardToModules = ClientDemandsModule.class)
    void goToClientDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToModules = SupplierDemandsModule.class)
    void goToSupplierDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    /**
     * @param filter - provided by search module
     */
    @Event(forwardToModules = MessagesModule.class)
    void goToMessagesModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToModules = SettingsModule.class)
    void goToSettingsModule();

    @Event(forwardToModules = AdminModule.class)
    void goToAdminModule(SearchModuleDataHolder filter, int loadWidget);

    /* Both Home and User menut control section */
    @Event(forwardToModules = SearchModule.class)
    void goToSearchModule();

    /**
     * Contact us popup will appear. Parameters will be forwared to method fillContactUsValues().
     *
     * @param subject - predefined subject i.e. report issue that was invoked by user from Error Module
     * @param errorId - the error ID what was genereated for reported issue
     */
    @Event(handlers = FooterPresenter.class)
    void sendUsEmail(int subject, String errorId);

    /**
     * Contact us popup will will be prefilled with values as subject and errorId.
     *
     * @param subject - predefined subject i.e. report issue that was invoked by user from Error Module
     * @param errorId - the error ID what was genereated for reported issue
     */
    @Event(handlers = EmailDialogPopupPresenter.class)
    void fillContactUsValues(int subject, String errorId);

    /**************************************************************************/
    /* Navigation events - Other control sections                             */
    /**************************************************************************/
    /**
     * Logout usera prechadza vzdy cez tuto metodu. Nastavuje sa menu, hlavicka
     * a defaultny modul po prihlaseni.
     */
    @Event(handlers = {HeaderPresenter.class, RootPresenter.class, MenuPresenter.class })
    void atHome();

    /**
     * Login usera prechadza vzdy cez tuto metodu. Nastavuje sa menu, hlavicka,
     * cookies a defaultny modul po odhlaseni
     */
    @Event(handlers = {UserHeaderPresenter.class, RootPresenter.class, UserMenuPresenter.class })
    void atAccount();

    @Event(handlers = UserHeaderPresenter.class)
    void setUpdatedUnreadMessagesCount(int numberOfMessages);

    /**
     * This method populates Storage i.e. our custom GWT session object with UserDetail.
     * A secured RPC service is invoked so this method can be called only if user is logged in and he opened our
     * website in new browser tab, which obviously starts the whole app from the begining. If user is not logged in
     * the RPC service will cause the initiation of loginPopupView via SecuredAsyncCallback.
     */
    @Event(handlers = RootHandler.class)
    void loginFromSession(int widgetToLoad);

    /**
     * This event will be called in case an error occurs while loading the
     * ChildModule code.
     *
     * @param reason - An object may be fired for the event used in case of
     * error but the type of this object must be compatible with
     * java.lang.Throwable. In this case, the error returned by the
     * RunAsync object is passed to the event.
     */
    @LoadChildModuleError
    @Event(handlers = RootPresenter.class)
    void errorOnLoad(Throwable reason);

    /**
     * This event will be called before starting to load the ChildModule code.
     * You can for example decide to display a wait popup.
     *
     * Zatial zakomentovane. Mozno to nebudeme potrebovat kvoli zrychleniu aplikacie
     */
    @BeforeLoadChildModule
    @Event(handlers = RootPresenter.class)
    void beforeLoad();

    /**
     * This event will be called after the code is done loading.
     * You can for example decide to hide a wait popup.
     *
     * Zatial zakomentovane. Mozno to nebudeme potrebovat kvoli zrychleniu aplikacie
     */
    @AfterLoadChildModule
    @Event(handlers = RootPresenter.class)
    void afterLoad();

    /**************************************************************************/
    /* Parent events - no events for RootModule                               */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    // TODO Praso - mozeme odstranit? No usage
    @NotFoundHistory
    @Event(handlers = RootPresenter.class)
    void notFound();

    /**************************************************************************/
    /* LOGIN - LOGOUT.                                                        */
    /**************************************************************************/
    /**
     * Login is handled by HeaderPresenter so that event handler can be set up before calling LoginPopupPresenter.
     */
    @Event(handlers = RootPresenter.class)
    void login(int widgetToLoad);

    @Event(handlers = RootPresenter.class)
    void autoLogin(String email, String password, int widgetToLoad);

    @Event(handlers = UserHeaderPresenter.class)
    void logout(int widgetToLoad);

    /**************************************************************************/
    /* LOADING.                                                               */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void loadingShow(String loadingMessage);

    // TODO praso - zakomentoval som tieto dve metody na loadovanie cakacieho popupu
    // Chcem pouzit standardnu funkciu cez onBefore, onAfter
    @Event(handlers = RootPresenter.class)
    void loadingHide();

    /**************************************************************************/
    /* CATEGORY SELECTOR WIDGET.                                              */
    /**************************************************************************/
    // TODO Praso - tuto metodu vola sibling module DemandCreationModule, SupplierCreationModule a ClientDemandsModule
    /** CategorySelection section. **/
    @Event(handlers = RootPresenter.class)
    void initCategoryWidget(SimplePanel embedToWidget, int checkboxes, int displayCountsOfWhat,
        List<CategoryDetail> categoriesToSet);

    /**************************************************************************/
    /* LOCALITY SELECTOR WIDGET.                                              */
    /**************************************************************************/
    /** LocalitySelector section. **/
    @Event(handlers = RootPresenter.class)
    void initLocalityWidget(SimplePanel embedToWidget, int checkboxes, int displayCountsOfWhat,
        List<LocalityDetail> localitiesToSet);

    /**************************************************************************/
    /* ADDRESS SELECTOR WIDGET.                                               */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void initAddressWidget(SimplePanel embedToWidget);

    @Event(forwardToModules = {SupplierCreationModule.class, SettingsModule.class }, passive = true)
    void notifyAddressWidgetListeners();

    /**************************************************************************/
    /* SERVICE SELECTOR WIDGET.                                               */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void initServicesWidget(SimplePanel embedToWidget);

    @Event(forwardToModules = SettingsModule.class, passive = true)
    void nofityServicesWidgetListeners();

    @Event(handlers = RootHandler.class)
    void getServices();

    @Event(handlers = ServicesSelectorPresenter.class)
    void setServices(ArrayList<ServiceDetail> services);

    /**************************************************************************/
    /* EMAIL DIALOG POPUP.                                                    */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void initEmailDialogPopup();

    /**************************************************************************/
    /* ACTION BOX.                                                            */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void initActionBox(SimplePanel holderWidget, UniversalTableGrid grid);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by MenuPresenter --- HOME MENU                 */
    /**************************************************************************/
    @Event(handlers = MenuPresenter.class)
    void menuStyleChange(int loadedModule);

    /**************************************************************************/
    /* Business events handled by UserMenuPresenter --- USER MENU             */
    /**************************************************************************/
    @Event(handlers = UserMenuPresenter.class)
    void userMenuStyleChange(int loadedModule);

    /**************************************************************************/
    /* Vytvorenie DevelDetailWrapperPresentera.                               */
    /**************************************************************************/
    //Musi byt, lebo na vytvorenie DeatilWraper widgetu musim pouzit rootEventBus
    @Event(handlers = RootPresenter.class)
    void requestDetailWrapperPresenter();

    //passive = mal by zavoalt metodu len v aktivom prezenteri., funguje aj s modulmi???
    //Ak funguje, tak tu to staci, pretoze aktivny bude vzdy len jeden modul, podla prihlaseneho uzivatela
    //alebo moze byt rola, kde budeme chciet mat zobrazene oboje? .. to bude skor zriedkavo, takze
    //by to nemuselo az tak vadit
    @Event(forwardToModules = {ClientDemandsModule.class, SupplierDemandsModule.class }, passive = true)
    void responseDetailWrapperPresenter(DetailsWrapperPresenter detailSection);

    /**************************************************************************/
    /* Business events handled by DevelDetailWrapperPresenter.                */
    /**************************************************************************/
    /*
     * Request/Response Method pair
     * DemandDetail for detail section
     * @param demandId
     * @param type
     */
    @Event(handlers = RootHandler.class)
    void requestDemandDetail(Long demandId);

    @Event(handlers = DetailsWrapperPresenter.class, passive = true)
    void responseDemandDetail(FullDemandDetail demandDetail);

    @Event(handlers = RootHandler.class)
    void requestClientDetail(Long clientId);

    @Event(handlers = DetailsWrapperPresenter.class, passive = true)
    void responseClientDetail(FullClientDetail clientDetail);

    @Event(handlers = RootHandler.class)
    void requestSupplierDetail(Long supplierId);

    @Event(handlers = DetailsWrapperPresenter.class, passive = true)
    void responseSupplierDetail(FullSupplierDetail supplierDetail);

    /*
     * Request/Response method pair
     * Fetch and display chat(conversation) for supplier new demands list
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    @Event(handlers = RootHandler.class)
    void requestConversation(Long threadId, Long userId);

    @Event(handlers = DetailsWrapperPresenter.class)
    void responseConversation(List<MessageDetail> chatMessages);

    /**************************************************************************/
    /* Messages                                                               */
    /**************************************************************************/
    @Event(handlers = RootHandler.class)
    void requestReadStatusUpdate(List<Long> userMessageIds, boolean isRead);

    @Event(handlers = RootHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    /**
     * Send/Response method pair
     * Sends message and receive the answer in a form of the same message to be displayed on UI.
     * @param messageToSend
     */
    @Event(handlers = RootHandler.class)
    void sendQuestionMessage(MessageDetail messageToSend);

    @Event(handlers = RootHandler.class)
    void sendOfferMessage(OfferMessageDetail offerMessageToSend);

    @Event(handlers = DetailsWrapperPresenter.class, passive = true)
    void addConversationMessage(MessageDetail sentMessage);

    /**
     * Send status message i.e. when user clicks on action buttons like Accept Offer, Finish Offer, Close Demand etc.
     * @param statusMessageBody representing status message text
     */
    @Event(handlers = DetailsWrapperPresenter.class, passive = true)
    void sendStatusMessage(String statusMessageBody);

    /**************************************************************************/
    /* User Activatoin                                                        */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void initActivationCodePopup(BusinessUserDetail user, int widgetToLoad);

    @Event(handlers = RootHandler.class)
    void activateUser(BusinessUserDetail user, String activationCode);

    @Event(handlers = RootHandler.class)
    void sendActivationCodeAgain(BusinessUserDetail client);

    @Event(handlers = ActivationCodePopupPresenter.class)
    void responseActivateUser(UserActivationResult activationResult);

    @Event(handlers = ActivationCodePopupPresenter.class)
    void responseSendActivationCodeAgain(boolean sent);

    /**************************************************************************/
    /* Business events handled by AccountAccountInfoPresenters.               */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void initUserRegistrationForm(SimplePanel holderWidget);

    @Event(handlers = RootHandler.class)
    void checkFreeEmail(String value);

    @Event(handlers = UserRegistrationFormPresenter.class)
    void checkFreeEmailResponse(Boolean result);
}
