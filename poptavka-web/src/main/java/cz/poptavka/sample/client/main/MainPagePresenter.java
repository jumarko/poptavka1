/**
 * Copyright 2010 upTick Pty Ltd
 *
 * Licensed under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation. You may obtain a copy of the
 * License at: http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package cz.poptavka.sample.client.main;


import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import cz.poptavka.sample.client.NameTokens;
import cz.poptavka.sample.client.service.demand.ClientRPCServiceAsync;
import cz.poptavka.sample.domain.user.Client;

import java.util.List;
import java.util.logging.Logger;


public class MainPagePresenter extends
        Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> {

    private final PlaceManager placeManager;

    @Inject
    private ClientRPCServiceAsync clientRPCService;


    private static final Logger LOGGER = Logger.getLogger(MainPagePresenter.class.getName());

    @ProxyStandard
    @NameToken(NameTokens.MAIN_PAGE)
    public interface MyProxy extends Proxy<MainPagePresenter>, Place {
    }

    public interface MyView extends View {
        //ApplicationMenu getApplicationMenu();
        FlexTable getTable();
    }

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SET_CONTEXT_AREA_CONTENT =
            new Type<RevealContentHandler<?>>();

    @Inject
    public MainPagePresenter(EventBus eventBus, MyView view, MyProxy proxy,
                             PlaceManager placeManager) {
        super(eventBus, view, proxy);

        this.placeManager = placeManager;

    }

    @Override
    protected void onBind() {
        super.onBind();

        LOGGER.fine("onBind()");

    }

    @Override
    protected void onReveal() {
        super.onReveal();
        clientRPCService.getAllClients(new AsyncCallback<List<Client>>() {

            @Override
            public void onSuccess(List<Client> result) {
                for (int i = 0; i < result.size(); i++) {
                    getView().getTable().setText(i, 0, result.get(i).getPerson().getFirstName());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

            }
        });
        LOGGER.fine("onReveal()");
    }

    @Override
    protected void onReset() {
        super.onReset();

    }

    @Override
    protected void revealInParent() {
        RevealRootLayoutContentEvent.fire(this, this);
    }

}
