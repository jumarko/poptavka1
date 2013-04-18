package com.eprovement.poptavka.client.common.userRegistration;

import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = UserRegistrationFormView.class, multiple = true)
public class UserRegistrationFormPresenter
        extends LazyPresenter<UserRegistrationFormPresenter.AccountFormInterface, RootEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface AccountFormInterface extends LazyView {
        SimplePanel getAddressHolder();

        Widget getWidgetView();

        boolean isValid();

        ValidationMonitor getEmailBox();

        void initVisualFreeEmailCheck(Boolean isAvailable);

        BusinessUserDetail createBusinessUserDetail();
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Init widget and call init address selector widget.
     * @param embedToWidget
     */
    public void initUserRegistrationForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
        eventBus.initAddressWidget(view.getAddressHolder());
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        ((TextBox) view.getEmailBox().getWidget()).addValueChangeHandler(
                new ValueChangeHandler<String>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<String> value) {
                        if (view.getEmailBox().isValid()) {
                            eventBus.checkFreeEmail(value.getValue().trim());
                        }
                    }
                });
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    public void onCheckFreeEmailResponse(Boolean isAvailable) {
        view.initVisualFreeEmailCheck(isAvailable);
    }

    /**
     * Tells to holder(parent) widget if can continue for register user.
     *
     * @return false - There is some invalid input.
     *         true - All inputs are valid.
     */
    public boolean canContinue() {
        return view.isValid();
    }
}
