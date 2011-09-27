/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminDemandInfoView.class)
public class AdminDemandInfoPresenter
        extends BasePresenter<AdminDemandInfoPresenter.AdminDemandInfoInterface, UserEventBus> {

    public interface AdminDemandInfoInterface {

        Widget getWidgetView();

        void setDemandDetail(FullDemandDetail contact);

        FullDemandDetail getUpdatedDemandDetail();

        Label getCatPath();

        Label getLocPath();

        Button getUpdateBtn();

        Button getEditCatBtn();

        Button getEditLocBtn();

        Button getFinnishCatBtn();

        Button getFinnishLocBtn();

        Button getBackCatBtn();

        Button getBackLocBtn();

        Button getRootCatBtn();

        Button getRootLocBtn();

        ListBox getEditCatList1();

        ListBox getEditLocList1();

        ListBox getEditLocList2();

        ListBox getCategoryList();

        ListBox getLocalityList();

        VerticalPanel getEditCatPanel();

        VerticalPanel getEditLocPanel();
    }

    public void onShowAdminDemandDetail(FullDemandDetail selectedObject) {
//        eventBus.displayContent(view.getWidgetView());
        view.getEditCatPanel().setVisible(false);
        view.setDemandDetail(selectedObject);
        eventBus.responseAdminDemandDetail(view.getWidgetView());

    }

    @Override
    public void bind() {
        view.getUpdateBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.addDemandToCommit(view.getUpdatedDemandDetail(), "all", "detail");
                Window.alert("Demand updated");
            }
        });
        view.getEditCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                categoryHistory.clear();
                view.getEditCatList1().clear();
                view.getEditCatList1().setEnabled(true);
                view.getBackCatBtn().setEnabled(false);
                categoryHistory.add(new String[]{"root", "root"});
                view.getEditCatPanel().setVisible(true);
                eventBus.getAdminRootCategories();
            }
        });
        view.getEditLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                localityHistory.add(new String[]{"root", "root"});
                view.getEditLocPanel().setVisible(true);
                eventBus.getAdminRootLocalities();
            }
        });
        view.getFinnishCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditCatList1().setEnabled(false);
                view.getEditCatPanel().setVisible(false);
            }
        });
        view.getFinnishLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditLocPanel().setVisible(false);
            }
        });
        view.getBackCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int size = categoryHistory.size();
                if (size < 2) {
                    return;
                }
                categoryHistory.remove(size - 1);
                eventBus.getAdminParentCategories(Long.parseLong(categoryHistory.get(size - 2)[0]));
            }
        });
        view.getBackLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.getAdminParentLocalities(view.getEditLocList1().getValue(0));
            }
        });
        view.getEditCatList1().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int idx = view.getEditCatList1().getSelectedIndex();
                categoryHistory.add(new String[]{
                    view.getEditCatList1().getValue(idx),
                    view.getEditCatList1().getItemText(idx)});
                eventBus.getAdminSubCategories(1, Long.parseLong(view.getEditCatList1().getValue(idx)));
            }
        });
        view.getEditLocList1().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int idx = view.getEditLocList1().getSelectedIndex();
                localityHistory.add(new String[]{
                    view.getEditLocList1().getValue(idx),
                    view.getEditLocList1().getItemText(idx)});
                eventBus.getAdminSubCategories(1, Long.parseLong(view.getEditLocList1().getValue(idx)));
            }
        });
        view.getEditLocList2().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int idx = view.getEditLocList2().getSelectedIndex();
                localityHistory.add(new String[]{
                    view.getEditLocList2().getValue(idx),
                    view.getEditLocList2().getItemText(idx)});
                eventBus.getAdminSubCategories(2, Long.parseLong(view.getEditLocList2().getValue(idx)));
            }
        });
        view.getCategoryList().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getCategoryList().removeItem(view.getCategoryList().getSelectedIndex());
            }
        });
        view.getLocalityList().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getLocalityList().removeItem(view.getLocalityList().getSelectedIndex());
            }
        });
        view.getRootCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditCatList1().clear();
                view.getCatPath().setText("");
                eventBus.getAdminRootCategories();
            }
        });
        view.getRootLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditLocList1().clear();
                view.getEditLocList2().clear();
                view.getLocPath().setText("");
                eventBus.getAdminRootLocalities();
            }
        });
    }

    public void onDisplayAdminTabDemandsLoop(List<FullDemandDetail> list) {
        eventBus.displayAdminTabDemands(list);
    }
    private List<String[]> categoryHistory = new ArrayList<String[]>();
    private List<String[]> localityHistory = new ArrayList<String[]>();
    private Boolean alreadyAdded = false;

    public void onDisplayAdminCategories(int listNumber, List<CategoryDetail> list) {
        if (list.isEmpty()) {
            String[] data = categoryHistory.get(categoryHistory.size() - 1);
            view.getCategoryList().addItem(data[1], data[0]);
            if (alreadyAdded) {
                categoryHistory.remove(categoryHistory.size() - 2);
            }
            alreadyAdded = true;
        } else {
            view.getEditCatList1().clear();
            for (CategoryDetail cat : list) {
                view.getEditCatList1().addItem(cat.getName(), Long.toString(cat.getId()));
            }
        }
        if (categoryHistory.size() > 1) {
            view.getBackCatBtn().setEnabled(true);
        }
        this.updatePath();
    }

    public void onDisplayAdminLocalities(int listNumber, List<LocalityDetail> list) {
//        if (list.isEmpty()) {
//            view.getLocalityList().addItem(lastLocClicked[1], lastLocClicked[0]);
//            return;
//        }
//
//        if (listNumber == 1) {
//            for (CategoryDetail cat : list) {
//                view.getEditCatList1().addItem(cat.getName(), Long.toString(cat.getId()));
//            }
//        } else {
//            if (view.getEditCatList2().getItemCount() == 0) {
//                view.getEditCatList2().clear();
//                for (CategoryDetail cat : list) {
//                    view.getEditCatList2().addItem(cat.getName(), Long.toString(cat.getId()));
//                }
//            } else {
//                view.getEditCatList1().clear();
//                for (int i = 0; i < view.getEditCatList2().getItemCount(); i++) {
//                    view.getEditCatList1().addItem(
//                            view.getEditCatList2().getItemText(i), view.getEditCatList2().getValue(i));
//                }
//                view.getEditCatList2().clear();
//                for (CategoryDetail cat : list) {
//                    view.getEditCatList2().addItem(cat.getName(), Long.toString(cat.getId()));
//                }
//            }
//        }
    }

    public void onDoBackCategories(List<CategoryDetail> list) {
        view.getEditCatList1().clear();
        for (CategoryDetail cat : list) {
            view.getEditCatList1().addItem(cat.getName(), Long.toString(cat.getId()));
        }
        this.updatePath();
    }

    public void onDoBackLocalities(List<LocalityDetail> list) {
        view.getEditLocList2().clear();

        for (int i = 0; i < view.getEditLocList1().getItemCount(); i++) {
            view.getEditLocList2().addItem(view.getEditLocList1().getItemText(i), view.getEditLocList1().getValue(i));
        }
        for (LocalityDetail loc : list) {
            view.getEditLocList1().addItem(loc.getName(), loc.getCode());
        }
    }

    private void updatePath() {
        StringBuilder strBld = new StringBuilder();
        for (String[] str : categoryHistory) {
            strBld.append(str[1]);
            strBld.append(" -> ");
        }
        strBld.delete(strBld.length() - 4, strBld.length());
        view.getCatPath().setText(strBld.toString());
    }
}
