package cz.poptavka.sample.client.user.problems;

import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;
/**
 *
 * @author Martin Slavkovsky
 *
 */
@Events(startView = MyProblemsView.class, module = MyProblemsModule.class)
public interface MyProblemsEventBus extends EventBus {

}
