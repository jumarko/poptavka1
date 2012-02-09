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
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import cz.poptavka.sample.client.user.admin.AdminModuleEventBus;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminSupplierInfoView.class)
public class AdminSupplierInfoPresenter
        extends LazyPresenter<AdminSupplierInfoPresenter.AdminSupplierInfoInterface, AdminModuleEventBus> {

    public interface AdminSupplierInfoInterface extends LazyView {

        Widget getWidgetView();

        void setSupplierDetail(FullSupplierDetail contact);

        FullSupplierDetail getUpdatedSupplierDetail();

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

    public void onShowAdminSupplierDetail(FullSupplierDetail selectedObject) {
//        eventBus.displayContent(view.getWidgetView());
        view.getEditCatPanel().setVisible(false);
        view.getEditLocPanel().setVisible(false);
        view.setSupplierDetail(selectedObject);
        eventBus.responseAdminSupplierDetail(view.getWidgetView());

    }

    @Override
    public void bindView() {
        addUpdateButtonHandler();
        addEditCategoryButtonHandler();
        addEditLocalityButtonHandler();
        addFinishCategoryButtonHandler();
        addFinnishLocalityButtonHandler();
        addBackCategoryButtonHandler();
        addBackLocalityButtonHandler();
        addEditCategoryListHandler();
        addEitLocalityListHandler();
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
                eventBus.getAdminSupplierRootLocalities();
            }
        });
    }

    private void addRootCategoryButtonHandler() {
        view.getRootCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getEditCatList().clear();
                view.getCatPath().setText("");
                eventBus.getAdminSupplierRootCategories();
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

    private void addEitLocalityListHandler() {
        view.getEditLocList().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int idx = view.getEditLocList().getSelectedIndex();
                localityHistory.add(new String[]{
                            view.getEditLocList().getValue(idx),
                            view.getEditLocList().getItemText(idx)});
                eventBus.getAdminSupplierSubLocalities(view.getEditLocList().getValue(idx));
            }
        });
    }

    private void addEditCategoryListHandler() {
        view.getEditCatList().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int idx = view.getEditCatList().getSelectedIndex();
                categoryHistory.add(new String[]{
                            view.getEditCatList().getValue(idx),
                            view.getEditCatList().getItemText(idx)});
                eventBus.getAdminSupplierSubCategories(Long.parseLong(view.getEditCatList().getValue(idx)));
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
                eventBus.getAdminSupplierParentLocalities(localityHistory.get(size - 2)[0]);
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
                eventBus.getAdminSupplierParentCategories(Long.parseLong(categoryHistory.get(size - 2)[0]));
            }
        });
    }

    private void addFinnishLocalityButtonHandler() {
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

    private void addEditLocalityButtonHandler() {
        view.getEditLocBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                localityHistory.clear();
                view.getEditLocList().clear();
                view.getEditLocList().setEnabled(true);
                view.getBackLocBtn().setEnabled(false);
                localityHistory.add(new String[]{"root", "root"});
                view.getEditLocPanel().setVisible(true);
                eventBus.getAdminSupplierRootLocalities();
            }
        });
    }

    private void addEditCategoryButtonHandler() {
        view.getEditCatBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                categoryHistory.clear();
                view.getEditCatList().clear();
                view.getEditCatList().setEnabled(true);
                view.getBackCatBtn().setEnabled(false);
                categoryHistory.add(new String[]{"root", "root"});
                view.getEditCatPanel().setVisible(true);
                eventBus.getAdminSupplierRootCategories();
            }
        });
    }

    private void addUpdateButtonHandler() {
        view.getUpdateBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.addSupplierToCommit(view.getUpdatedSupplierDetail());
                Window.alert("Supplier updated");
            }
        });
    }

    private List<String[]> categoryHistory = new ArrayList<String[]>();
    private List<String[]> localityHistory = new ArrayList<String[]>();
    private Boolean alreadyAdded = false;

    public void onDisplayAdminSupplierCategories(List<CategoryDetail> list) {
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

    public void onDisplayAdminSupplierLocalities(List<LocalityDetail> list) {
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
                view.getEditLocList().addItem(loc.getName(), Long.toString(loc.getId()));
            }
        }
        if (categoryHistory.size() > 1) {
            view.getBackCatBtn().setEnabled(true);
        }
        this.updateLocalityPath();
    }

    public void onDoBackSupplierCategories(List<CategoryDetail> list) {
        view.getEditCatList().clear();
        for (CategoryDetail cat : list) {
            view.getEditCatList().addItem(cat.getName(), Long.toString(cat.getId()));
        }
        this.updateCategoryPath();
    }

    public void onDoBackSupplierLocalities(List<LocalityDetail> list) {
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
