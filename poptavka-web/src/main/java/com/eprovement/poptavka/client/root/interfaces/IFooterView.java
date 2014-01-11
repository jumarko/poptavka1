package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface IFooterView extends IsWidget {

    public interface IFooterPresenter {
    }

    Button getCompany();
    Button getContactUs();
    Button getAboutUs();
    Button getFAQ();
    Button getPrivacyPolicy();
    Button getTermsAndConditions();

    Widget getWidgetView();
}
