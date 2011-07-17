package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetailImpl;

/**
 * Rip-off widget for sending asking/replying to questions (depends on point of view).
 *
 * @author Beho
 */
public class QuestionWindow extends Composite implements QuestionPresenter.ReplyInterface {

    private static ReplyWindowUiBinder uiBinder = GWT.create(ReplyWindowUiBinder.class);
    interface ReplyWindowUiBinder extends UiBinder<Widget, QuestionWindow> {   }

    private static final StyleResource CSS = GWT.create(StyleResource.class);

    @UiField TextArea replyTextArea;
    @UiField Anchor submitBtn;

    private long demandId = 0;

    @Override
    public void createView() {
        CSS.message().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Add ClickHandler to submitButton and demandId of demand user is replying to.
     *
     * @param submitButtonHandler
     * @param selectedDemandId
     */
    public void addClickHandler(ClickHandler submitButtonHandler) {
        submitBtn.addClickHandler(submitButtonHandler);
//        demandId = selectedDemandId;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MessageDetail getCreatedMessage() {
        MessageDetail message =  new MessageDetailImpl();
        message.setBody(replyTextArea.getText());
        message.setDemandId(demandId);
        return message;
    }

    @Override
    public boolean isValid() {
        int errorCount = 0;
        errorCount += (replyTextArea.getText().equals("") ? 1 : 0);
        return errorCount == 0;
    }

    @Override
    public void setNormalStyle() {
        replyTextArea.setText("");
    }

}
