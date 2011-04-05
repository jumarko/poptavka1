package cz.poptavka.sample.client.common.creation.widget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;

@Presenter(view = FormUserWrapperView.class)
public class FormUserWrapperPresenter extends
    LazyPresenter<FormUserWrapperPresenter.FormWrapperInterface, CommonEventBus> {

    public interface FormWrapperInterface extends LazyView {

        Widget getWidgetView();

        RadioButton getPersonButton();

        RadioButton getCompanyButton();

        SimplePanel getFormHolder();
    }

    @Override
    public void bindView() {
        view.getPersonButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> value) {
                if (view.getPersonButton().getValue()) {
                    eventBus.initPersonForm(view.getFormHolder());
                }
            }
        });
        view.getCompanyButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> value) {
                if (view.getCompanyButton().getValue()) {
                    eventBus.initCompanyForm(view.getFormHolder());
                }
            }
        });
    }

    public void onInitNewUserForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }
}
