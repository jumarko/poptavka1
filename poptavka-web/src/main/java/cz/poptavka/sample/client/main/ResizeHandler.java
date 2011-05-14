package cz.poptavka.sample.client.main;

import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

@EventHandler
public class ResizeHandler extends BaseEventHandler<MainEventBus> {

    public void callResize() {
        eventBus.compactModeCheck();
    }

}
