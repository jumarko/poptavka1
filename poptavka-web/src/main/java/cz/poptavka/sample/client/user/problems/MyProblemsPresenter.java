package cz.poptavka.sample.client.user.problems;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.domain.mail.Message;
import cz.poptavka.sample.domain.user.User;

/**
 *
 * @author Martin Slavkovsky
 *
 */
@Presenter(view = MyProblemsView.class)
public class MyProblemsPresenter
        extends
        BasePresenter<MyProblemsPresenter.MyProblemsViewInterface, MyProblemsEventBus> {

    private static final Logger LOGGER = Logger
            .getLogger(MyProblemsPresenter.class.getName());

    public interface MyProblemsViewInterface {
        Widget getWidgetView();

        void createView();

        void displayMessages(List<Message> messages);
    }

    public void bind() {

    }

    public void onDisplayMyProblems() {
        LOGGER.info("TUTUTUTUTU");
        //Initialize Problems
        eventBus.setHomeWidget(AnchorEnum.FIRST, view.getWidgetView(), true);
       //Initialize Messages
        LOGGER.info("AJAJAJAJAJA");
        eventBus.displayMessages();
    }


    public void onDisplayMessages() {
        LOGGER.info("onDisplayMessages in MessagesPresenter");
        view.displayMessages(this.getMessages());
    }

    public List<Message> getMessages() {
        List<Message> list = new LinkedList<Message>();

        Message m1 = new Message();
        m1.setSubject("Odpoved1");
        User u = new User();
        u.setLogin("userLogin");
        u.setEmail("userEmail");
        m1.setSender(u);
        m1.setSent(new Date());
        m1.setId(1L);
        m1.setBody("This is message1. It is an answer to some question. "
                + "Who knows what kind of question it was. I don't like "
                + "making up fake text. So this is the last sentence. Bye.");

        Message m2 = new Message();
        m2.setSubject("Odpoved2");
        User u2 = new User();
        u.setLogin("secondUserLogin");
        u.setEmail("secondUserEmail");
        m2.setSender(u2);
        m2.setSent(new Date());
        m2.setId(2L);
        m2.setBody("No text. Sorry.");

        Message m3 = new Message();
        m3.setSubject("Odpoved3");
        User u3 = new User();
        u.setLogin("secondUserLogin");
        u.setEmail("secondUserEmail");
        m3.setSender(u3);
        m3.setSent(new Date());
        m3.setId(3L);
        m3.setBody("What about some chinese text? :)"
                + "I hate windows. I wanted place some japanese text here "
                + "and i couldn't because of shiti encoding cp1250. "
                + "How to avoid this situasion? My source codes are in UTF-8."
                + "So wtf???!!!");

        Message m4 = new Message();
        m4.setSubject("Odpoved4");
        User u4 = new User();
        u.setLogin("secondUserLogin");
        u.setEmail("secondUserEmail");
        m4.setSender(u4);
        m4.setSent(new Date());
        m4.setId(4L);
        m4.setBody("It is Japanese you moron. "
                + "Don't write here bulshits."
                + "By the way. Those are lyrics to song of may alarm clock.");

        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);

        return list;
    }

}
