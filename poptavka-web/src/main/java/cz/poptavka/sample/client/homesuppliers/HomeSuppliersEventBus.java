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
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;

/**
 *
 * @author ivan.vlcek
 */
@Events(startView = SuppliersView.class, module = HomeSuppliersModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeSuppliersEventBus extends EventBus {

    @Start
    @Event(handlers = SuppliersPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is
     * nothing to carry out in this method we should remove forward event to
     * save the number of method invocations.
     */
    @Forward
    @Event(handlers = SuppliersPresenter.class)
    void forward();

    /* Navigation events. */
    /**
     * The only entry point to this module due to code-splitting and exclusive
     * fragment.
     */
    @Event(handlers = SuppliersPresenter.class)
    void goToHomeSuppliers();

    /**
     * Display root categories.
     */
    @Event(handlers = SuppliersPresenter.class)
    void atSuppliers();

    /**
     * Display sub-categories, suppliers of selected category and detail of
     * selected supplier.
     */
    @Event(handlers = SuppliersPresenter.class)
    void atDisplaySuppliers(CategoryDetail categoryDetail);

    /* Parent events. */
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    /* Business events. */
    /* Business events handled by Presenters. */
    @Event(handlers = SuppliersPresenter.class)
    void displayRootcategories(ArrayList<CategoryDetail> list);

    @Event(handlers = SuppliersPresenter.class)
    void displaySubCategories(ArrayList<CategoryDetail> list,
            Long parentCategory);

    @Event(handlers = SuppliersPresenter.class)
    void displaySuppliers(ArrayList<FullSupplierDetail> list);

    @Event(handlers = SuppliersPresenter.class)
    void setLocalityData(ArrayList<LocalityDetail> list);

    @Event(handlers = SuppliersPresenter.class)
    void addToPath(CategoryDetail category);

    @Event(handlers = SuppliersPresenter.class)
    void removeFromPath(Long code);

    @Event(handlers = SuppliersPresenter.class)
    void setCategoryID(Long categoryCode);

    @Event(handlers = SuppliersPresenter.class)
    void resetDisplaySuppliersPager(int totalFoundNew);

    /* Business events handled by Handlers. */
    @Event(handlers = SuppliersHandler.class)
    void getSubCategories(Long category);

    @Event(handlers = SuppliersHandler.class)
    void getCategories();

    @Event(handlers = SuppliersHandler.class)
    void getLocalities();

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersByCategoryLocality(int start, int count, Long category,
            String locality);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersByCategory(int start, int count, Long category);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersCount(Long category, String locality);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersCountByCategory(Long category);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersCountByCategoryLocality(Long category, String locality);
}
