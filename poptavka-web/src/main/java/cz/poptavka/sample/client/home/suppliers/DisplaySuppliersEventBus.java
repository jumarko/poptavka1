package cz.poptavka.sample.client.home.suppliers;


import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import java.util.ArrayList;

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
    @Event(handlers = SuppliersCataloguePresenter.class)
    void atSuppliers();

    @Event(forwardToParent = true)
    void setBodyWidget(Widget content);

    //Category
    @Event(handlers = DisplaySuppliersHandler.class)
    void getCategory(String category);

    @Event(handlers = DisplaySuppliersHandler.class)
    void getCategories();

    //Display
    @Event(handlers = SuppliersCataloguePresenter.class)
    void setCategoryData(ArrayList<CategoryDetail> list);

    @Event(handlers = DisplaySuppliersPresenter.class)
    void displaySubcategories(ArrayList<CategoryDetail> list);
}
