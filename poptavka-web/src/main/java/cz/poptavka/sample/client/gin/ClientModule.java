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

package cz.poptavka.sample.client.gin;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.DefaultEventBus;
import com.gwtplatform.mvp.client.DefaultProxyFailureHandler;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import cz.poptavka.sample.client.NameTokens;
import cz.poptavka.sample.client.PoptavkaPlaceManager;
import cz.poptavka.sample.client.demand.DemandsPresenter;
import cz.poptavka.sample.client.demand.DemandsView;
import cz.poptavka.sample.client.main.MainPagePresenter;
import cz.poptavka.sample.client.main.MainPageView;

public class ClientModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
        bind(PlaceManager.class).to(PoptavkaPlaceManager.class).in(Singleton.class);
        bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(
                Singleton.class);
        bind(RootPresenter.class).asEagerSingleton();
        bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class).in(
                Singleton.class);

        // Constants
        /////////////////////////////////
        // HERE IS SET DEFAULT VIEW
        /////////////////////////////////

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.MAIN_PAGE);
        // Main Pages
        bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
                MainPageView.class, MainPagePresenter.MyProxy.class);

        // Main Pages
        bindPresenter(DemandsPresenter.class, DemandsPresenter.MyView.class,
                DemandsView.class, DemandsPresenter.MyProxy.class);

    }
}
