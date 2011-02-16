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

package cz.poptavka.sample.client;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import cz.poptavka.sample.client.gin.DefaultPlace;


public class PoptavkaPlaceManager extends PlaceManagerImpl {

    private final PlaceRequest defaultPlaceRequest;

    @Inject
    public PoptavkaPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter,
                                @DefaultPlace String defaultNameToken) {
        super(eventBus, tokenFormatter);

        this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
    }

    @Override
    public void revealDefaultPlace() {
        revealPlace(defaultPlaceRequest);
    }

    @Override
    public void revealErrorPlace(String invalidHistoryToken) {
        PlaceRequest myRequest = new PlaceRequest(NameTokens.ERROR_PAGE);
        revealPlace(myRequest);
    }
}
