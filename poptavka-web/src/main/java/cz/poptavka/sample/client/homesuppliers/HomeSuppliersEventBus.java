/*
 * HomeSuppliersEventBus servers all events for module HomeSuppliersModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis dodavatelov
 */
package cz.poptavka.sample.client.homesuppliers;

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

    /* Navigation events. */
    /**
     * The only entry point to this module due to code-splitting and exclusive
     * fragment.
     */
    @Event(handlers = HomeSuppliersPresenter.class)
    void initHomeSupplierModule(SearchModuleDataHolder searchDataHolder);

    /**
     * Display root categories.
     */
    @Event(handlers = HomeSuppliersPresenter.class)
    void atSuppliers();

    /**
     * Display sub-categories, suppliers of selected category and detail of
     * selected supplier.
     */
    @Event(handlers = HomeSuppliersPresenter.class)
    void atDisplaySuppliers(CategoryDetail categoryDetail);

    /* Parent events. */
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    /* Business events. */
    /* Business events handled by Presenters. */
    @Event(handlers = HomeSuppliersPresenter.class)
    void displayRootcategories(ArrayList<CategoryDetail> list);

    @Event(handlers = HomeSuppliersPresenter.class)
    void displaySubCategories(ArrayList<CategoryDetail> list,
            Long parentCategory);

    @Event(handlers = HomeSuppliersPresenter.class)
    void displaySuppliers(List<FullSupplierDetail> list);

//    @Event(handlers = HomeSuppliersPresenter.class)
//    void setLocalityData(ArrayList<LocalityDetail> list);
    @Event(handlers = HomeSuppliersPresenter.class)
    void addToPath(CategoryDetail category);

    @Event(handlers = HomeSuppliersPresenter.class)
    void removeFromPath(Long code);

    @Event(handlers = HomeSuppliersPresenter.class)
    void setCategoryID(Long categoryCode);

//    @Event(handlers = HomeSuppliersPresenter.class)
//    void resetDisplaySuppliersPager(int totalFoundNew);
    @Event(handlers = HomeSuppliersPresenter.class)
    void createAsyncDataProvider(final int totalFound);

    /* Business events handled by Handlers. */
    @Event(handlers = HomeSuppliersHandler.class)
    void getSubCategories(Long category);

    @Event(handlers = HomeSuppliersHandler.class)
    void getCategories();

    @Event(handlers = HomeSuppliersPresenter.class)
    void rootWithSearchDataHolder();
//    @Event(handlers = HomeSuppliersHandler.class)
//    void getLocalities();
//
//    @Event(handlers = HomeSuppliersHandler.class)
//    void getSuppliersByCategoryLocality(int start, int count, Long category,
//            String locality);
//
//    @Event(handlers = HomeSuppliersHandler.class)
//    void getSuppliersByCategory(int start, int count, Long category);
//
//    @Event(handlers = HomeSuppliersHandler.class)
//    void getSuppliersCount(Long category, String locality);
//
//    @Event(handlers = HomeSuppliersHandler.class)
//    void getSuppliersCountByCategory(Long category);
//
//    @Event(handlers = HomeSuppliersHandler.class)
//    void getSuppliersCountByCategoryLocality(Long category, String locality);

    @Event(handlers = HomeSuppliersHandler.class)
    void getSuppliersCount(SearchModuleDataHolder detail);

    @Event(handlers = HomeSuppliersHandler.class)
    void getSuppliers(int start, int count, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns);
}
