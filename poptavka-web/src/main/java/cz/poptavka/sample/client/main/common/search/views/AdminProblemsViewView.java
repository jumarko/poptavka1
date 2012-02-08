package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

public class AdminProblemsViewView extends Composite {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminProblemsViewView> {
    }
    @UiField
    TextBox idFrom, idTo, text;
    @UiField
    Button clearBtn;

    public AdminProblemsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminProblems();
        if (!idFrom.getText().equals("")) {
            data.getAdminProblems().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminProblems().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!text.getText().equals("")) {
            data.getAdminProblems().setText(text.getText());
        }
        return data;
    }

    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminProblems().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminProblems().getIdFrom());
        }
        if (data.getAdminProblems().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminProblems().getIdTo());
        }
        if (data.getAdminProblems().getText() != null) {
            infoText.append("text:");
            infoText.append(data.getAdminProblems().getText());
        }
        infoHolder.setText(infoText.toString());
    }

    @UiHandler("idFrom")
    void validateIdFrom(ChangeEvent event) {
        if (!idFrom.getText().matches("[0-9]+")) {
            idFrom.setText("");
        }
    }

    @UiHandler("idTo")
    void validateIdTo(ChangeEvent event) {
        if (!idTo.getText().matches("[0-9]+")) {
            idTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        idFrom.setText("");
        idTo.setText("");
        text.setText("");
    }
}