package cz.poptavka.sample.client.common;

import java.util.ArrayList;
import java.util.HashMap;

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
import cz.poptavka.sample.client.common.creation.widget.FormCompanyPresenter;
import cz.poptavka.sample.client.common.creation.widget.FormDemandAdvPresenter;
import cz.poptavka.sample.client.common.creation.widget.FormDemandBasicPresenter;
import cz.poptavka.sample.client.common.creation.widget.FormLoginPresenter;
import cz.poptavka.sample.client.common.creation.widget.FormPersonPresenter;
import cz.poptavka.sample.client.common.creation.widget.FormUserWrapperPresenter;
import cz.poptavka.sample.client.common.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Events(startView = DemandCreationView.class, module = CommonModule.class)
public interface CommonEventBus extends EventBus {

    @Event(handlers = {DemandCreationPresenter.class })
    void atCreateDemand(boolean homeSection);

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
    void setCategoryData(CategoryType type, ArrayList<CategoryDetail> list);

    @Event(forwardToParent = true)
    void setCategoryDisplayData(CategoryType type, ArrayList<CategoryDetail> list);

    @Event(handlers = CommonHandler.class)
    void getRootCategories();

    @Event(handlers = CommonHandler.class)
    void getChildCategories(CategoryType type, String categoryId);

    @Event(handlers = CommonHandler.class)
    void createDemand(DemandDetail newDemand, Long clientId);

    /** Forms for Demand Creation **/
    @Event(handlers = FormDemandBasicPresenter.class)
    void initDemandBasicForm(SimplePanel embedToWidget);

    @Event(handlers = FormDemandAdvPresenter.class)
    void initDemandAdvForm(SimplePanel embedToWidget);

    @Event(handlers = FormLoginPresenter.class)
    void initFormLogin(SimplePanel embedToWidget);

    @Event(handlers = FormUserWrapperPresenter.class)
    void initNewUserForm(SimplePanel embedToWidget);

    @Event(handlers = FormPersonPresenter.class,
            activate = FormPersonPresenter.class, deactivate = FormCompanyPresenter.class)
    void initPersonForm(HasOneWidget embedToWidget);

    @Event(handlers = FormCompanyPresenter.class,
            activate = FormCompanyPresenter.class, deactivate = FormPersonPresenter.class)
    void initCompanyForm(HasOneWidget embedToWidget);

    /** Form Validation methods. **/
    @Event(handlers = FormDemandBasicPresenter.class)
    void validateDemandBasicForm();

    @Event(handlers = FormDemandAdvPresenter.class)
    void validateDemandAdvForm();

    @Event(handlers = {FormCompanyPresenter.class, FormPersonPresenter.class })
    void submitUserForm();

    /** Form navigation. **/
    @Event(handlers = DemandCreationPresenter.class)
    void formNextStep();

    @Event(handlers = DemandCreationPresenter.class)
    void formBackStep();

    /** Form get data. **/
    @Event(handlers = FormDemandBasicPresenter.class)
    void getBasicInfoValues();

    @Event(handlers = CategorySelectorPresenter.class)
    void getSelectedCategoryCodes();

    @Event(handlers = LocalitySelectorPresenter.class)
    void getSelectedLocalityCodes();

    @Event(handlers = FormDemandAdvPresenter.class)
    void getAdvInfoValues();

    @Event(handlers = DemandCreationPresenter.class)
    void pushBasicInfoValues(HashMap<String, Object> basicValues);

    @Event(handlers = DemandCreationPresenter.class)
    void pushSelectedCategoryCodes(ArrayList<String> categories);

    @Event(handlers = DemandCreationPresenter.class)
    void pushSelectedLocalityCodes(ArrayList<String> localities);

    @Event(handlers = DemandCreationPresenter.class)
    void pushAdvInfoValues(HashMap<String, Object> advValues);

}
