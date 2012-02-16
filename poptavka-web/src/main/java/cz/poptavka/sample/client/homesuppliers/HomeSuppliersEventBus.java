/*
 * HomeSuppliersEventBus servers all events for module HomeSuppliersModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis dodavatelov
 */
package cz.poptavka.sample.client.homesuppliers;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ivan.vlcek
 */
@Events(startView = HomeSuppliersView.class, module = HomeSuppliersModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeSuppliersEventBus extends EventBus {

    @Start
    @Event(handlers = HomeSuppliersPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is
     * nothing to carry out in this method we should remove forward event to
     * save the number of method invocations.
     */
    @Forward
    @Event(handlers = HomeSuppliersPresenter.class)
    void forward();

    //
    //                      Navigation events.
    //
    // PARENT EVENTS
    @Event(forwardToParent = true)
    void setHomeBodyHolderWidget(IsWidget body);

    @Event(forwardToParent = true)
    void setUserBodyHolderWidget(Widget body);

    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    /**
     * The only entry point to this module due to code-splitting and exclusive
     * fragment.
     */
    // INIT
    @Event(handlers = HomeSuppliersPresenter.class, historyConverter = HomeSuppliersHistoryConverter.class)
    String initHomeSuppliersModule(SearchModuleDataHolder searchDataHolder, String location);

    //
    //                  **** DISPLAY ****
    //
    // ROOT CATEGORIES
    @Event(handlers = HomeSuppliersPresenter.class)
    void displayRootcategories(ArrayList<CategoryDetail> list);

    // SUB CATEGORIES
    @Event(handlers = HomeSuppliersPresenter.class)
    void displaySubCategories(ArrayList<CategoryDetail> list,
            Long parentCategory);

    // SUPPLIERS
    @Event(handlers = HomeSuppliersPresenter.class)
    void displaySuppliers(List<FullSupplierDetail> list);

    /**
     * Display sub-categories, suppliers of selected category and detail of
     * selected supplier.
     */
    // CHILD WIDGET
    @Event(handlers = HomeSuppliersPresenter.class)
    void displayChildWidget(CategoryDetail categoryDetail);

    //
    //                  **** PATH ****
    //
    // UPDATE
    @Event(handlers = HomeSuppliersPresenter.class, historyConverter = HomeSuppliersHistoryConverter.class)
    String updatePath(CategoryDetail category);

    /**
     * Retrieve category for displaying ({@link addToPath}) its name in path.
     * @param categoryId
     */
    @Event(handlers = HomeSuppliersHandler.class)
    void getCategoryName(Long categoryId);

    /**
     * After retrieving category, add its name to path
     * @param categoryDetail
     */
    @Event(handlers = HomeSuppliersPresenter.class)
    void addToPath(CategoryDetail categoryDetail);

    @Event(handlers = HomeSuppliersPresenter.class)
    void createAsyncDataProvider(final int totalFound);

    //
    //              Business events handled by Handlers.
    //
    // CATEGORIES
    @Event(handlers = HomeSuppliersHandler.class)
    void getCategories();

    @Event(handlers = HomeSuppliersHandler.class)
    void getSubCategories(Long category);

    // SUPPLIERS
    @Event(handlers = HomeSuppliersHandler.class)
    void getSuppliersCount(SearchModuleDataHolder detail);

    @Event(handlers = HomeSuppliersHandler.class)
    void getSuppliers(int start, int count, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns);
}
