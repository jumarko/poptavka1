package cz.poptavka.sample.client.home.supplier;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.home.HomeEventBus;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter.SupplierInfoInterface;
import cz.poptavka.sample.client.home.supplier.widget.SupplierServiceWidget;
import cz.poptavka.sample.client.main.common.StatusIconLabel;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import cz.poptavka.sample.shared.domain.SupplierDetail;

@Presenter(view = SupplierCreationView.class)
public class SupplierCreationPresenter
    extends LazyPresenter<SupplierCreationPresenter.CreationViewInterface, HomeEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    SupplierCreationPresenter");

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public interface CreationViewInterface extends LazyView {

        StackLayoutPanel getMainPanel();

        SimplePanel getSupplierInfoHolder();

        SimplePanel getLocalityHolder();

        SimplePanel getCategoryHolder();

        SimplePanel getServiceHolder();

        HasClickHandlers getRegisterButton();

        Anchor getConditionLink();

        Widget getWidgetView();

        StatusIconLabel getStatusLabel(int order);

        void showConditions();
    }

    public void bindView() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (view.getMainPanel().getVisibleIndex() < eventItem) {
                    boolean result = canContinue(eventItem);
                    view.getStatusLabel(eventItem).setPassedSmall(result);
                    if (!canContinue(eventItem)) {
                        event.cancel();
                    }
                }
            }
        });
        view.getRegisterButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(SERVICE)) {
                    LOGGER.fine("register him!");
                    registerSupplier();
                } else {
                    LOGGER.fine("cannot continue");
                }
            }
        });
        view.getConditionLink().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.showConditions();
            }
        });
    }

    /**
     * Init method call.
     * TODO decide WHEN other parts should be built.
     *
     * @param homeSection
     */
    public void onAtRegisterSupplier() {
        LOGGER.info("Initializing Supplier Registration Widget ... ");
        eventBus.setHomeWidget(AnchorEnum.THIRD, view.getWidgetView(), true);
//        //init parts
        LOGGER.info(" -> Supplier Info Form");
        eventBus.initSupplierForm(view.getSupplierInfoHolder());
        LOGGER.info(" -> Category Widget");
        eventBus.initCategoryWidget(view.getCategoryHolder());
        LOGGER.info(" -> Locality Widget");
        eventBus.initLocalityWidget(view.getLocalityHolder());
        LOGGER.info(" -> Service Form");
        view.getServiceHolder().add(new SupplierServiceWidget());
    }

    private void registerSupplier() {
        SupplierInfoInterface info = (SupplierInfoInterface) view.getSupplierInfoHolder().getWidget();
        LocalitySelectorInterface locs = (LocalitySelectorInterface) view.getLocalityHolder().getWidget();
        CategorySelectorInterface cats = (CategorySelectorInterface) view.getCategoryHolder().getWidget();
        SupplierServiceWidget service = (SupplierServiceWidget) view.getServiceHolder().getWidget();

        SupplierDetail newSupplier = info.createSupplier();
        newSupplier.setLocalities(locs.getSelectedLocalityCodes());
        newSupplier.setCategories(cats.getSelectedCategoryCodes());

        eventBus.registerSupplier(newSupplier);
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterClient());
    }

    private static final int INFO = 1;
    private static final int CATEGORY = 2;
    private static final int LOCALITY = 3;
    private static final int SERVICE = 4;

    private boolean canContinue(int step) {
//        if (step == INFO) {
//            FormDemandBasicInterface widget =
//                (FormDemandBasicInterface) view.getSupplierInfoHolder().getWidget();
//            return widget.isValid();
//        }
//        if (step == CATEGORY) {
//            CategorySelectorInterface widget =
//                (CategorySelectorInterface) view.getCategoryHolder().getWidget();
//            return widget.isValid();
//        }
//        if (step == LOCALITY) {
//            LocalitySelectorInterface widget =
//                (LocalitySelectorInterface) view.getLocalityHolder().getWidget();
//            return widget.isValid();
//        }
//        if (step == SERVICE) {
//            FormDemandAdvViewInterface widget =
//                (FormDemandAdvViewInterface) view.getServiceHolder().getWidget();
//            return widget.isValid();
//        }
        //can't reach
//        return false;

        //DEVEL
        return true;

    }
}
