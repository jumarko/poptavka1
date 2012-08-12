package com.eprovement.poptavka.client.homeWelcome.interfaces;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;

public interface IHomeWelcomeView extends IsWidget {

    public interface IHomeWelcomePresenter {
    }

    HorizontalPanel getCategorySection();

    SingleSelectionModel<CategoryDetail> getCategorySelectionModel();

    void displayCategories(int columns, ArrayList<CategoryDetail> categories);

    Widget getWidgetView();
}
