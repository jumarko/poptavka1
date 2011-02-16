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

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import cz.poptavka.sample.client.demand.DemandsPresenter;
import cz.poptavka.sample.client.main.MainPagePresenter;


@GinModules({ DispatchAsyncModule.class, ClientModule.class })
public interface PoptavkaGinjector extends Ginjector {

    EventBus getEventBus();

    PlaceManager getPlaceManager();

    AsyncProvider<DemandsPresenter> getStorePresenter();

    // Main Pages
    Provider<MainPagePresenter> getMainPagePresenter();

    ProxyFailureHandler getProxyFailureHandler();
}
