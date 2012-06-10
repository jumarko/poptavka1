/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminDemandInfoView.class)
public class AdminDemandInfoPresenter
        extends LazyPresenter<AdminDemandInfoPresenter.AdminDemandInfoInterface, AdminEventBus> {

    public interface AdminDemandInfoInterface extends LazyView {

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

        ListBox getEditCatList();

        ListBox getEditLocList();

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
    public void bindView() {
        addUpdateButtonHandler();
        addEditCatButtonHandler();
        addEditLocationButtonHandler();
        addFinishCategoryButtonHandler();
        addFinishLocalityButtonHandler();
        addBackCategoryButtonHandler();
        addBackLocalityButtonHandler();
        addEditCategoryButtonHandler();
        addEditLocalityListHandler();
        addCategoryListHandler();
        addLocalityListHandler();
        addRootCategoryButtonHandler();
        addRootLocalityButtonHandler();
    }

    private void addRootLocalityButtonHandler() {
        view.getRootLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditLocList().clear();
                view.getLocPath().setText("");
                eventBus.getAdminDemandRootLocalities();
            }
        });
    }

    private void addRootCategoryButtonHandler() {
        view.getRootCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditCatList().clear();
                view.getCatPath().setText("");
                eventBus.getAdminDemandRootCategories();
            }
        });
    }

    private void addLocalityListHandler() {
        view.getLocalityList().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getLocalityList().removeItem(view.getLocalityList().getSelectedIndex());
            }
        });
    }

    private void addCategoryListHandler() {
        view.getCategoryList().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getCategoryList().removeItem(view.getCategoryList().getSelectedIndex());
            }
        });
    }

    private void addEditLocalityListHandler() {
        view.getEditLocList().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int idx = view.getEditLocList().getSelectedIndex();
                localityHistory.add(new String[]{
                            view.getEditLocList().getValue(idx),
                            view.getEditLocList().getItemText(idx)});
                eventBus.getAdminDemandSubLocalities(view.getEditLocList().getValue(idx));
            }
        });
    }

    private void addEditCategoryButtonHandler() {
        view.getEditCatList().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int idx = view.getEditCatList().getSelectedIndex();
                categoryHistory.add(new String[]{
                            view.getEditCatList().getValue(idx),
                            view.getEditCatList().getItemText(idx)});
                eventBus.getAdminDemandSubCategories(Long.parseLong(view.getEditCatList().getValue(idx)));
            }
        });
    }

    private void addBackLocalityButtonHandler() {
        view.getBackLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int size = localityHistory.size();
                if (size < 2) {
                    return;
                }
                localityHistory.remove(size - 1);
                eventBus.getAdminDemandParentLocalities(localityHistory.get(size - 2)[0]);
            }
        });
    }

    private void addBackCategoryButtonHandler() {
        view.getBackCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int size = categoryHistory.size();
                if (size < 2) {
                    return;
                }
                categoryHistory.remove(size - 1);
                eventBus.getAdminDemandParentCategories(Long.parseLong(categoryHistory.get(size - 2)[0]));
            }
        });
    }

    private void addFinishLocalityButtonHandler() {
        view.getFinnishLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditLocList().setEnabled(false);
                view.getEditLocPanel().setVisible(false);
            }
        });
    }

    private void addFinishCategoryButtonHandler() {
        view.getFinnishCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditCatList().setEnabled(false);
                view.getEditCatPanel().setVisible(false);
            }
        });
    }

    private void addEditLocationButtonHandler() {
        view.getEditLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                localityHistory.clear();
                view.getEditLocList().clear();
                view.getEditLocList().setEnabled(true);
                view.getBackLocBtn().setEnabled(false);
                localityHistory.add(new String[]{"root", "root"});
                view.getEditLocPanel().setVisible(true);
                eventBus.getAdminDemandRootLocalities();
            }
        });
    }

    private void addEditCatButtonHandler() {
        view.getEditCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                categoryHistory.clear();
                view.getEditCatList().clear();
                view.getEditCatList().setEnabled(true);
                view.getBackCatBtn().setEnabled(false);
                categoryHistory.add(new String[]{"root", "root"});
                view.getEditCatPanel().setVisible(true);
                eventBus.getAdminDemandRootCategories();
            }
        });
    }

    private void addUpdateButtonHandler() {
        view.getUpdateBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.addDemandToCommit(view.getUpdatedDemandDetail());
                Window.alert("Demand updated");
            }
        });
    }

    public void onDisplayAdminTabDemandsLoop(List<FullDemandDetail> list) {
        eventBus.displayAdminTabDemands(list);
    }
    private List<String[]> categoryHistory = new ArrayList<String[]>();
    private List<String[]> localityHistory = new ArrayList<String[]>();
    private Boolean alreadyAdded = false;

    public void onDisplayAdminDemandCategories(List<CategoryDetail> list) {
        if (list.isEmpty()) {
            String[] data = categoryHistory.get(categoryHistory.size() - 1);
            view.getCategoryList().addItem(data[1], data[0]);
            if (alreadyAdded) {
                categoryHistory.remove(categoryHistory.size() - 2);
            }
            alreadyAdded = true;
        } else {
            view.getEditCatList().clear();
            for (CategoryDetail cat : list) {
                view.getEditCatList().addItem(cat.getName(), Long.toString(cat.getId()));
            }
        }
        if (categoryHistory.size() > 1) {
            view.getBackCatBtn().setEnabled(true);
        }
        this.updateCategoryPath();
    }

    public void onDisplayAdminDemandLocalities(List<LocalityDetail> list) {
        if (list.isEmpty()) {
            String[] data = localityHistory.get(localityHistory.size() - 1);
            view.getLocalityList().addItem(data[1], data[0]);
            if (alreadyAdded) {
                localityHistory.remove(localityHistory.size() - 2);
            }
            alreadyAdded = true;
        } else {
            view.getEditLocList().clear();
            for (LocalityDetail loc : list) {
                view.getEditLocList().addItem(loc.getName(), loc.getCode());
            }
        }
        if (categoryHistory.size() > 1) {
            view.getBackCatBtn().setEnabled(true);
        }
        this.updateLocalityPath();
    }

    public void onDoBackDemandCategories(List<CategoryDetail> list) {
        view.getEditCatList().clear();
        for (CategoryDetail cat : list) {
            view.getEditCatList().addItem(cat.getName(), Long.toString(cat.getId()));
        }
        this.updateCategoryPath();
    }

    public void onDoBackDemandLocalities(List<LocalityDetail> list) {
        view.getEditLocList().clear();
        for (LocalityDetail loc : list) {
            view.getEditLocList().addItem(loc.getName(), Long.toString(loc.getId()));
        }
        this.updateLocalityPath();
    }

    private void updateCategoryPath() {
        StringBuilder strBld = new StringBuilder();
        for (String[] str : categoryHistory) {
            strBld.append(str[1]);
            strBld.append(" -> ");
        }
        strBld.delete(strBld.length() - 4, strBld.length());
        view.getCatPath().setText(strBld.toString());
    }

    private void updateLocalityPath() {
        StringBuilder strBld = new StringBuilder();
        for (String[] str : localityHistory) {
            strBld.append(str[1]);
            strBld.append(" -> ");
        }
        strBld.delete(strBld.length() - 4, strBld.length());
        view.getLocPath().setText(strBld.toString());
    }
}