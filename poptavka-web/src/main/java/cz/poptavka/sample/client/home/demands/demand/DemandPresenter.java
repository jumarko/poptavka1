package cz.poptavka.sample.client.home.demands.demand;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.demands.DemandsEventBus;

@Presenter(view = DemandView.class)
public class DemandPresenter extends BasePresenter<DemandPresenter.DemandViewInterface, DemandsEventBus> {

    public interface DemandViewInterface {

        void setAttachmentToken(String token);

        void setLoginToken(String token);

        void setRegisterToken(String token);
    }

    /**
     * Bind objects and theirs action handlers.
     */
    @Override
    public void bind() {
        //TODO - dorobit
        //view.setAttachmentToken(getTokenGenerator().atAttachment());

        //TODO - dorobit, ked sa bude vediet ako to ma vyzerat
        //view.setLoginToken(getTokenGenerator().atLogin());

//        view.setRegisterToken(getTokenGenerator().atRegisterSupplier());
    }

//    public void onAtRegisterSupplier() {
//    }
}
