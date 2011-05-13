package cz.poptavka.sample.client.common;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.common.category.CategorySelectorPresenter;
import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategoryType;
import cz.poptavka.sample.client.common.creation.DemandCreationPresenter;
import cz.poptavka.sample.client.common.creation.DemandCreationView;
import cz.poptavka.sample.client.common.creation.widget.FormDemandAdvPresenter;
import cz.poptavka.sample.client.common.creation.widget.FormDemandBasicPresenter;
import cz.poptavka.sample.client.common.creation.widget.FormLoginPresenter;
import cz.poptavka.sample.client.common.creation.widget.FormUserRegistrationPresenter;
import cz.poptavka.sample.client.common.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.home.supplier.SupplierCreationPresenter;
import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter;
import cz.poptavka.sample.client.home.supplier.widget.SupplierServicePresenter;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;

@Events(startView = DemandCreationView.class, module = CommonModule.class)
public interface CommonEventBus extends EventBus {

    @Event(handlers = {DemandCreationPresenter.class })
    void atCreateDemand(boolean homeSection);

    @Event(handlers = SupplierCreationPresenter.class)
    void atRegisterSupplier();

    @Event(forwardToParent = true)
    void setAnchorWidget(boolean homeSection, AnchorEnum anchor, Widget content, boolean clearOthers);

    /** LocalitySelector section. **/
    @Event(handlers = LocalitySelectorPresenter.class)
    void initLocalityWidget(HasOneWidget embedToWidget);

    @Event(handlers = LocalitySelectorPresenter.class)
    void setLocalityData(LocalityType type, ArrayList<LocalityDetail> localityList);

    //handler methods
    @Event(handlers = CommonHandler.class)
    void getLocalities();

    @Event(handlers = CommonHandler.class)
    void getChildLocalities(LocalityType type, String locCode);

    /** CategorySelection section. **/
    @Event(handlers = CategorySelectorPresenter.class)
    void initCategoryWidget(HasOneWidget embedToWidget);

    @Event(handlers = CategorySelectorPresenter.class)
    void setCategoryListData(int newListPosition, ArrayList<CategoryDetail> list);

    @Event(forwardToParent = true)
    void setCategoryDisplayData(CategoryType type, ArrayList<CategoryDetail> list);

    @Event(handlers = CommonHandler.class)
    void getRootCategories();

//    //TODO del
//    @Event(handlers = CommonHandler.class)
//    void getChildCategories(CategoryType type, String categoryId);

    @Event(handlers = CommonHandler.class)
    void getChildListCategories(int newListPosition, String categoryId);

    @Event(handlers = CommonHandler.class)
    void createDemand(DemandDetail newDemand, Long clientId);

    /** Forms for Demand Creation **/
    @Event(handlers = FormDemandBasicPresenter.class)
    void initDemandBasicForm(SimplePanel embedToWidget);

    @Event(handlers = FormDemandAdvPresenter.class)
    void initDemandAdvForm(SimplePanel embedToWidget);

    @Event(handlers = FormLoginPresenter.class)
    void initLoginForm(SimplePanel embedToWidget);

    @Event(handlers = FormUserRegistrationPresenter.class)
    void initRegistrationForm(SimplePanel embedToWidget);

    @Event(handlers = CommonHandler.class)
    void verifyExistingClient(ClientDetail client);

    @Event(handlers = CommonHandler.class)
    void registerNewClient(ClientDetail client);

    @Event(handlers = DemandCreationPresenter.class)
    void toggleCreateAndRegButton();

    @Event(handlers = CommonHandler.class)
    void checkFreeEmail(String value);

    @Event(handlers = {FormUserRegistrationPresenter.class, SupplierInfoPresenter.class })
    void checkFreeEmailResponse(Boolean result);

    @Event(handlers = DemandCreationPresenter.class)
    void prepareNewDemandForNewClient(Long clientId);

    @Event(handlers = DemandCreationPresenter.class)
    void showLoginError();

    /**
     * Popup methods for shoving, changing text and hiding, for letting user know, that application is still working
     */
    @Event(forwardToParent = true)
    void displayLoadingPopup(String loadingMessage);

    @Event(forwardToParent = true)
    void changeLoadingMessage(String loadingMessage);

    @Event(forwardToParent = true)
    void hideLoadingPopup();

    /** Supplier registration **/
    @Event(handlers = SupplierServicePresenter.class)
    void initServiceForm(SimplePanel serviceHolder);

    @Event(handlers = SupplierInfoPresenter.class)
    void initSupplierForm(SimplePanel supplierInfoHolder);

    @Event(handlers = CommonHandler.class)
    void registerSupplier(SupplierDetail newSupplier);

}
