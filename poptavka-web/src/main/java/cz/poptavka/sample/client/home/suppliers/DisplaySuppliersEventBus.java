package cz.poptavka.sample.client.home.suppliers;

import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

/**
 *
 * @author Martin Slavkovsky
 *
 */
@Events(startView = DisplaySuppliersView.class, module = DisplaySuppliersModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface DisplaySuppliersEventBus extends EventBus {

    /**
     * Initialize demands presenter.
     */
    //@Event(handlers = SuppliersCataloguePresenter.class)
//    @Event(handlers = DisplaySuppliersPresenter.class,
//    historyConverter = DisplaySuppliersHistoryConverter.class)//, historyOnStart = true)
//    void atSuppliers();
//
//    @Event(forwardToParent = true)
//    void setBodyWidget(Widget content);
//
//    //Category
//    @Event(handlers = DisplaySuppliersHandler.class)
//    void getSubCategories(String category);
//
//    @Event(handlers = DisplaySuppliersHandler.class)
//    void getCategories();
//
//    //Display
//    @Event(handlers = DisplaySuppliersPresenter.class, historyConverter = DisplaySuppliersHistoryConverter.class)
//    void displaySubcategories(ArrayList<CategoryDetail> list);
//
//    @Event(handlers = DisplaySuppliersPresenter.class)
//    void addToPath(String category);
//
//    @Event(forwardToParent = true)
//    void loadingShow(String loadingMessage);
//
//    @Event(forwardToParent = true)
//    void loadingHide();
}
