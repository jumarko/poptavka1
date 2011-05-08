package cz.poptavka.sample.client.user.problems;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.domain.mail.Message;
import cz.poptavka.sample.domain.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

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

        void displayProblems(List<Problem> problems);

        void displayMessages(Problem problem);

        CellTable<Problem> getCellTable();
    }

    public void bind() {
        LOGGER.info("BIND my problems");
        final SingleSelectionModel<Problem> selectionModel = new SingleSelectionModel<Problem>();
        view.getCellTable().setSelectionModel(selectionModel);
        selectionModel
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {
                        Problem selected = selectionModel.getSelectedObject();
                        if (selected != null) {
                            eventBus.displayMessages(selected);
                        }
                    }
                });
    }

    public void onDisplayProblems() {
        // Initialize Problems
        eventBus.setHomeWidget(AnchorEnum.FIRST, view.getWidgetView(), true);
        // Initialize Messages
        view.displayProblems(this.getProblems());
    }

    public void onDisplayMessages(Problem problem) {
        LOGGER.info("onDisplayMessages in MessagesPresenter");
        view.displayMessages(problem);
    }

    public List<Message> getMessages(String sufix) {
        List<Message> list = new LinkedList<Message>();

        Message m1 = new Message();
        m1.setSubject("Odpoved" + sufix);
        User u = new User();
        u.setEmail("userEmail" + sufix);
        m1.setSender(u);
        m1.setSent(new Date());
        m1.setId(1L);
        m1.setBody("This is message1. It is an answer to some question. "
                + "Who knows what kind of question it was. I don't like "
                + "making up fake text. So this is the last sentence. Bye.");

        Message m2 = new Message();
        m2.setSubject("Odpoved" + sufix);
        User u2 = new User();
        u2.setEmail("UserEmail" + sufix);
        m2.setSender(u2);
        m2.setSent(new Date());
        m2.setId(2L);
        m2.setBody("No text. Sorry.");

        Message m3 = new Message();
        m3.setSubject("Odpoved" + sufix);
        User u3 = new User();
        u3.setEmail("UserEmail" + sufix);
        m3.setSender(u3);
        m3.setSent(new Date());
        m3.setId(3L);
        m3.setBody("I hate windows. I wanted place some japanese text here "
                + "and i couldn't because of shiti encoding cp1250. "
                + "How to avoid this situasion? My source codes are in UTF-8."
                + "So wtf???!!!");

        Message m4 = new Message();
        m4.setSubject("Odpoved" + sufix);
        User u4 = new User();
        u4.setEmail("UserEmail" + sufix);
        m4.setSender(u4);
        m4.setSent(new Date());
        m4.setId(4L);
        m4.setBody("It is Japanese you moron. " + "Don't write here bulshits."
                + "By the way. Those are lyrics to song of may alarm clock.");

        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);

        return list;
    }

    public List<Problem> getProblems() {
        List<Problem> contacts = new ArrayList<Problem>();

        Problem p1 = new Problem("demand 1", "ok", new Date(), "123 000");
        Message root = new Message();
        root.setBody("Sprava demand 1");
        p1.setRoot(root);
        p1.setAnswers(this.getMessages("1"));

        Problem p2 = new Problem("Joe", "ok", new Date(), "10 000");
        root = new Message();
        root.setBody("Sprava Joe");
        p2.setRoot(root);
        p2.setAnswers(this.getMessages("2"));

        Problem p3 = new Problem("George", "not ok", new Date(), "1600");
        root = new Message();
        root.setBody("Sprava George");
        p3.setRoot(root);
        p3.setAnswers(this.getMessages("3"));

        contacts.add(p1);
        contacts.add(p2);
        contacts.add(p3);

        return contacts;
    }
}
