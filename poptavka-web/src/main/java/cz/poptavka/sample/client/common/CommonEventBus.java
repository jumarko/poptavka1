package cz.poptavka.sample.client.common;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.common.category.CategorySelectorPresenter;
import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategoryType;
import cz.poptavka.sample.client.common.creation.DemandCreationPresenter;
import cz.poptavka.sample.client.common.creation.DemandCreationView;
import cz.poptavka.sample.client.common.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Events(startView = DemandCreationView.class, module = CommonModule.class)
public interface CommonEventBus extends EventBus {

    @Event(handlers = {DemandCreationPresenter.class })
    void initDemandCreation(boolean homeSection);

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

    @Event(handlers = CommonHandler.class)
    void getRootCategories();

    @Event(handlers = CommonHandler.class)
    void getChildCategories(CategoryType type, String categoryId);


}
