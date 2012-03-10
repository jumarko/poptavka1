package cz.poptavka.sample.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.root.RootEventBus;
import cz.poptavka.sample.client.root.interfaces.IMenuView;
import cz.poptavka.sample.client.root.interfaces.IMenuView.IMenuPresenter;

@Presenter(view = MenuView.class)
public class MenuPresenter extends BasePresenter<IMenuView, RootEventBus>
        implements IMenuPresenter {

    public void onStart() {
        // TODO praso - preco volame bindView a neprepisujeme defaultnu bind() metodu
        // ktora sa vola defaultne pri prvom spusteni presentera?
        bindView();
        GWT.log("Menu module loaded");
        eventBus.setMenu(view);
    }

    public void onSetHomeMenu() {
        // TODO praso - preco tu volame znovu bind metod. Nestaci prvy krat v onStart()?
        // vid docs
        // if presenter is activated and if it's the first event handled by the presenter,
        // it calls the bind method.
        bindView();
        GWT.log("set menu after log out");
        eventBus.setMenu(view);
    }

    public void bindView() {
        GWT.log("Binding menu view");
        view.getHomeButton().addClickHandler(new ClickHandler() {

            // TODO praso na co je tato metoda? Ma odhlasovat alebo co?
            @Override
            public void onClick(ClickEvent event) {
                eventBus.atHome();
            }
        });
        view.getDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initHomeDemandsModule(null, "home");
            }
        });
        view.getSuppliersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initHomeSuppliersModule(null, "home");
            }
        });
        view.getCreateSupplierButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCreateSupplierModule("home");
            }
        });
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCreateDemandModule("home");
            }
        });

    }
}
