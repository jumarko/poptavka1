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
package cz.poptavka.sample.client.demand;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import cz.poptavka.sample.client.NameTokens;
import cz.poptavka.sample.client.main.MainPagePresenter;

import java.util.logging.Logger;


public class DemandsPresenter extends
        Presenter<DemandsPresenter.MyView, DemandsPresenter.MyProxy> {

    private static final Logger LOGGER = Logger.getLogger(DemandsPresenter.class.getName());


    @ProxyCodeSplit
    @NameToken(NameTokens.DEMANDS_PAGE)
    public interface MyProxy extends ProxyPlace<DemandsPresenter> {

    }

    public interface MyView extends View {

    }

    @Inject
    public DemandsPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy);
    }

    @Override
    protected void onBind() {
        super.onBind();

    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SET_CONTEXT_AREA_CONTENT,
                this);
    }
}
