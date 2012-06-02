package com.eprovement.poptavka.client.home;


import com.mvp4g.client.annotation.Presenter;


import com.mvp4g.client.presenter.LazyPresenter;

import com.mvp4g.client.view.LazyView;

@Presenter(view = HomeView.class)
public class HomePresenter extends LazyPresenter<HomePresenter.HomeInterface, HomeEventBus> {

//    private static final Logger LOGGER = Logger.getLogger("HomePresenter");
//    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface HomeInterface extends LazyView {

//        SearchModuleView getSearchView();
//        AdvancedSearchView getAdvancedSearchView();
//        Widget getWidgetView();
//
//        void setHomeToken(String token);
//
//        void setBody(Widget content);
//
//        SimplePanel getSearchPanel();
//
//        HasClickHandlers getDemandsButton();
//
//        HasClickHandlers getSuppliersButton();
//
//        HasClickHandlers getCreateSupplierButton();
//
//        HasClickHandlers getCreateDemandButton();
    }
//    private DemandCreationPresenter demandCreation;

    @Override
    public void bindView() {
//        view.setHomeToken(getTokenGenerator().atHome());
//
//        view.getDemandsButton().addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                eventBus.initHomeDemandsModule(null, "home");
//            }
//        });
//        view.getSuppliersButton().addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                eventBus.initHomeSuppliersModule(null, "home");
//            }
//        });
//        view.getCreateSupplierButton().addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                eventBus.goToCreateSupplier("home");
//            }
//        });
//        view.getCreateDemandButton().addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                eventBus.goToCreateDemand("home");
//            }
//        });
    }

//    public void onStart() {
//        // for now do nothing...
//    }
//
//    public void onForward() {
//        // for now do nothing...
//    }
//
//    public void onAtHome() {
//        LOGGER.info("INIT Home Widget");
//
//        // DISPLAY SEARCH PANEL
//        eventBus.initSearchModule(view.getSearchPanel());
//
////        onDisplayMenu();
//        // DISPLAY MENU
//        eventBus.setPublicLayout();
//        eventBus.setHomeBodyHolderWidget(view.getWidgetView());
//
//        // WELCOME MODULE
//        eventBus.initHomeWelcomeModule(null);
//        // TODO initial homepage widget compilation
//    }
//
//    public void onSetBodyWidget(Widget content) {
//        view.setBody(content);
//    }
////
////    public void onDisplayMenu() {
////        eventBus.setPublicLayout();
////        eventBus.setBodyHolderWidget(view.getWidgetView());
////    }
//
//    /* Business events for child modules loading */
//    public void onErrorOnLoad(Throwable reason) {
//        // TODO praso - display error message if child modules doesn't load successfully
////view.displayErrorMessage( reason.getMessage() );
//    }
//
//    public void onBeforeLoad() {
//        // TODO praso - display wait loop
////view.setWaitVisible( true );
//    }
//
//    public void onAfterLoad() {
//        // TODO praso -  hide wait loop
////view.setWaitVisible( false );
//    }
//
//    /******** SEARCH PANEL ***********/
//    public void onShowHideAdvancedSearchPanel(String content, int whereIdx, int catIdx, int locIdx) {
////        if (view.getAdvancedSearchView().isVisible()) {
////            view.getAdvancedSearchView().setBaseInfo(MSGS.searchContent(), 0); //, 0, 0);
////            view.getAdvancedSearchView().setVisible(false);
////        } else {
////            view.getAdvancedSearchView().setBaseInfo(content, whereIdx); //, catIdx, locIdx);
////            view.getAdvancedSearchView().setVisible(true);
////        }
//    }

    /**
     * Fills category listBox with given list of localities.
     * @param list - data (categories)
     */
//    public void onSetCategoryData(final ArrayList<CategoryDetail> list) {
//        final ListBox box1 = view.getSearchView().getCategory();
//        final ListBox box2 = view.getAdvancedSearchView().getCategory();
//        box1.clear();
//        box2.clear();
//        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//
//            @Override
//            public void execute() {
//                box1.addItem(MSGS.allCategories());
////                box2.addItem(MSGS.allCategories());
//                for (int i = 0; i < list.size(); i++) {
//                    box1.addItem(list.get(i).getName(), String.valueOf(list.get(i).getId()));
////                    box2.addItem(list.get(i).getName(), String.valueOf(list.get(i).getId()));
//                }
//                box1.setSelectedIndex(0);
////                box2.setSelectedIndex(0);
//                LOGGER.info("Category Lists filled");
//            }
//        });
//    }

    /**
     * Fills locality listBox with given list of localities.
     * @param list - data (localities)
     */
//    public void onSetLocalityData(final ArrayList<LocalityDetail> list) {
//        final ListBox box1 = view.getSearchView().getLocality();
////        final ListBox box2 = view.getAdvancedSearchView().getLocality();
//        box1.clear();
////        box2.clear();
//        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//
//            @Override
//            public void execute() {
//                box1.addItem(MSGS.allLocalities());
//                for (int i = 0; i < list.size(); i++) {
//                    box1.addItem(list.get(i).getName(), String.valueOf(list.get(i).getCode()));
////                    box2.addItem(list.get(i).getName(), String.valueOf(list.get(i).getCode()));
//                }
//                box1.setSelectedIndex(0);
////                box2.setSelectedIndex(0);
//                LOGGER.info("Locality Lists filled");
//            }
//        });
//    }
}
