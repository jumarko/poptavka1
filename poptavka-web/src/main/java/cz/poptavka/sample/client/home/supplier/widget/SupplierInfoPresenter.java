package cz.poptavka.sample.client.home.supplier.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.home.HomeEventBus;
import cz.poptavka.sample.client.main.common.StatusIconLabel;
import cz.poptavka.sample.client.main.common.StatusIconLabel.State;
import cz.poptavka.sample.shared.domain.SupplierDetail;

@Presenter(view = SupplierInfoView.class)
public class SupplierInfoPresenter extends
    LazyPresenter<SupplierInfoPresenter.SupplierInfoInterface, HomeEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface SupplierInfoInterface extends LazyView {

        Widget getWidgetView();

        HasValueChangeHandlers<String> getEmailBox();

        PasswordTextBox getPwdBox();

        PasswordTextBox getPwdConfirmBox();

        StatusIconLabel getMailStatus();

        StatusIconLabel getPwdStatus();

        StatusIconLabel getPwdConfirmStatus();

        SupplierDetail getBaseSupplier();

        SupplierDetail createSupplier();

    }

    @Override
    public void bindView() {
        view.getEmailBox().addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> value) {
                /** UI method **/
                initVisualMailCheck(value.getValue());
            }
        });
        view.getPwdBox().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPwdCheck(view.getPwdBox().getText());
            }
        });
        view.getPwdConfirmBox().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent arg0) {
                initVisualPwdConfirmCheck();
            }
        });
    }

    public void onInitSupplierForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

    public void onCheckFreeEmailResponse(Boolean isAvailable) {
        if (isAvailable) {
            view.getMailStatus().setState(State.ACCEPT_16);
            view.getMailStatus().setDescription(MSGS.mailAvailable());
        } else {
            view.getMailStatus().setState(State.ERROR_16);
        }
    }

    /** Visualization methods **/
    private void initVisualMailCheck(String value) {
        view.getMailStatus().setState(State.LOAD_24);
        view.getMailStatus().setVisible(true);

        if (value.contains("@") && value.contains(".")) {
            eventBus.checkFreeEmail(value);
        } else {
            view.getMailStatus().setStateWithDescription(State.ERROR_16, MSGS.malformedEmail());
        }
    }

    private void initVisualPwdCheck(String value) {
        view.getPwdStatus().setVisible(true);
        if (value.length() < 6) {
            view.getPwdStatus().setStateWithDescription(State.ERROR_16, MSGS.shortPassword());
        }
        if ((value.length() <= 8) && (value.length() > 5)) {
            view.getPwdStatus().setStateWithDescription(State.INFO_16, MSGS.semiStrongPassword());
        }
        if (value.length() > 8) {
            view.getPwdStatus().setStateWithDescription(State.ACCEPT_16, MSGS.strongPassword());
        }
    }

    private void initVisualPwdConfirmCheck() {
        view.getPwdConfirmStatus().setVisible(true);
        if (!view.getPwdBox().getText().equals(view.getPwdConfirmBox().getText())) {
            view.getPwdConfirmStatus().setStateWithDescription(State.ERROR_16, MSGS.passwordsUnmatch());
        } else {
            view.getPwdConfirmStatus().setStateWithDescription(State.ACCEPT_16, MSGS.passwordsMatch());
        }
    }
}
