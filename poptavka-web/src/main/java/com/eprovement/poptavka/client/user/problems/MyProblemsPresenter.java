package com.eprovement.poptavka.client.user.problems;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.user.UserEventBus;
import com.eprovement.poptavka.client.user.widget.unused.OldDetailWrapperPresenter;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.MessageException;

/**
 *
 * @author Martin Slavkovsky
 *
 */
@Presenter(view = MyProblemsView.class, multiple = true)
public class MyProblemsPresenter
        extends
        BasePresenter<MyProblemsPresenter.MyProblemsViewInterface, UserEventBus> {

    private static final Logger LOGGER = Logger
            .getLogger(MyProblemsPresenter.class.getName());

    public interface MyProblemsViewInterface {

        Widget getWidgetView();

        void displayProblems(List<Problem> problems);

        CellTable<Problem> getCellTable();

        SingleSelectionModel<Problem> getSelectionModel();

        Button getReplyBtn();

        Button getEditBtn();

        Button getCloseBtn();

        Button getCancelBtn();

        Button getRefuseBtn();

        SimplePanel getDetailSection();
    }

    private OldDetailWrapperPresenter detailPresenter = null;

    public void bind() {
        view.getCellTable().getSelectionModel()
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {
                        Problem selected = view.getSelectionModel()
                                .getSelectedObject();
                        if (selected != null) {
                            // TODO Martin - add funkcionality
                            // eventBus.displayMessages(selected);
                        }
                    }
                });
        // TODO Martin -- provide data from db, not fake data
    }

    public void onInvokeMyProblems() {
        //eventBus.displayContent(view.getWidgetView());

        eventBus.requestMyProblems();
    }

    public void onRequestMyProblems() {
        // Initialize Messages
        view.displayProblems(this.getProblems());
    }

    public void onDisplayMessages(Problem problem) {
        LOGGER.info("onDisplayMessages in MessagesPresenter");
    }

    public List<Message> getMessages(String sufix) {
        try {
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
            m4.setBody("It is Japanese you moron. "
                    + "Don't write here bulshits."
                    + "By the way. Those are lyrics to song of may alarm clock.");
            list.add(m1);
            list.add(m2);
            list.add(m3);
            list.add(m4);
            return list;
        } catch (MessageException ex) {
            Logger.getLogger(MyProblemsPresenter.class.getName()).log(
                    Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Problem> getProblems() {
        try {
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
        } catch (MessageException ex) {
            Logger.getLogger(MyProblemsPresenter.class.getName()).log(
                    Level.SEVERE, null, ex);
            return null;
        }

    }

    // TODO delete, just devel tool
    public void cleanDetailWrapperPresenterForDevelopment() {
        if (detailPresenter != null) {
            eventBus.removeHandler(detailPresenter);
        }
    }
}
