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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 */
public class DemandsView extends ViewImpl implements DemandsPresenter.MyView {

    private Label label = new Label();

    public DemandsView() {
    }

    @Override
    public Widget asWidget() {
        return label;
    }


}
