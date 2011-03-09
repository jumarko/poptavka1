package cz.poptavka.sample.client.home;

import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

/**
 * Handler for RPC calls for localities & categories
 *
 * @author Beho
 *
 */
@EventHandler
public class HomeHandler extends BaseEventHandler<HomeEventBus> {

//    private LocalityRPCServiceAsync localityService = null;
//    private CategoryRPCServiceAsync categoryService = null;
//
//    @InjectService
//    public void setLocalityService(LocalityRPCServiceAsync service) {
//        localityService = service;
//    }
//
//    @InjectService
//    public void setCategoryService(CategoryRPCServiceAsync service) {
//        categoryService = service;
//    }
//
//    public void onGetLocalities(final LocalityType type) {
//        localityService.getLocalities(type, new AsyncCallback<List<LocalityDetail>>() {
//            @Override
//            public void onSuccess(List<LocalityDetail> list) {
//                eventBus.displayLocalityList(type, list);
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                //empty
//            }
//        });
//    }
//
//    public void onGetChildLocalities(final LocalityType type, String locCode) {
//        localityService.getLocalities(locCode, new AsyncCallback<List<LocalityDetail>>() {
//            @Override
//            public void onFailure(Throwable arg0) {
//                //empty
//            }
//
//            @Override
//            public void onSuccess(List<LocalityDetail> list) {
//                eventBus.displayLocalityList(type, list);
//            }
//        });
//    }
//
//    public void onGetRootCategories() {
//        categoryService.getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                //empty
//            }
//
//            @Override
//            public void onSuccess(ArrayList<CategoryDetail> list) {
//                eventBus.displayRootCategories(list);
//            }
//        });
//    }

}
