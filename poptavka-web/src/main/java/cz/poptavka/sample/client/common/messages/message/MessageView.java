package cz.poptavka.sample.client.common.messages.message;


import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.domain.mail.Message;

public class MessageView extends Composite
        implements MessagePresenter.MessageViewInterface {

    private static final Logger LOGGER = Logger.getLogger(MessageView.class.getName());

    private static MessagesUiBinder uiBinder = GWT
            .create(MessagesUiBinder.class);

    interface MessagesUiBinder extends UiBinder<Widget, MessageView> {
    }

    @UiField
    Image logo;
    @UiField
    Label from;
    @UiField
    Label shortText;
    @UiField
    Label date;

    @UiField
    Button buttonSend;
    @UiField
    Button buttonDiscard;

    @UiField
    MyHorizontalPanel panelHeader;
    @UiField
    DisclosurePanel panelBody;
    @UiField
    HorizontalPanel panelFooter;
    @UiField
    VerticalPanel messageText;

    //EXISTING MESSAGES
    public MessageView(Message message) {
        LOGGER.info("Creating MessageView: " + message.getBody());
        initWidget(uiBinder.createAndBindUi(this));
        this.from.setText(message.getSender().getEmail());
        this.date.setText(message.getSent().toString());
        messageText.add(this.getText(message.getBody()));
        if (message.getBody().length() > 50) {
            this.shortText.setText(message.getBody().substring(0, 50) + "...");
        } else {
            this.shortText.setText(message.getBody());
        }
        panelFooter.setVisible(false);
    }

    // NEW MESSAGE
    public MessageView() {
        initWidget(uiBinder.createAndBindUi(this));
        this.from.setText("me");
        this.date.setText(new Date().toString());

        messageText.add(this.getTextArea());

        panelFooter.setVisible(true);
        panelBody.setOpen(true);
    }

    public Grid getText(String text) {

        //RichTextArea area = new RichTextArea();
//        area.ensureDebugId("cwRichText-area");
//        area.setSize("100%", "14em");
//        area.setText(text);
        Label label = new Label();
        label.setText(text);

        Grid grid = new Grid(1, 1);
        grid.setStyleName("cw-RichText");
        grid.setWidget(0, 0, label);
        return grid;
    }

    public Grid getTextArea() {
        // Create the text area and toolbar
        //RichTextArea area = new RichTextArea();
        TextArea area = new TextArea();
//        area.ensureDebugId("cwRichText-area");
//        area.setSize("100%", "14em");
//        RichTextToolbar toolbar = new RichTextToolbar(area);
//        toolbar.ensureDebugId("cwRichText-toolbar");
//        toolbar.setWidth("100%");

        // Add the components to a panel
        Grid grid = new Grid(2, 1);
        grid.setStyleName("cw-RichText");
        //grid.setWidget(0, 0, toolbar);
        grid.setWidget(0, 0, area);
        return grid;
    }

    @Override
    public DisclosurePanel getPanelBody() {
        return panelBody;
    }

    /**
     * Displays message body. Just GUI. Not working if through eventBus and presenter.
     * @param event
     */
    @UiHandler("panelHeader")
    public void onClickHeaderPanel(ClickEvent click) {
        if (panelBody.isOpen()) {
            panelBody.setOpen(false);
        } else {
            panelBody.setOpen(true);
        }
    }


    @Override
    public HasClickHandlers getSendMessageBtn() {
        return buttonSend;
    }

    @Override
    public HasClickHandlers getDiscardMessageBtn() {
        return buttonDiscard;
    }

    @Override
    public HorizontalPanel getPanelFooter() {
        return panelFooter;
    }
    /**
     * Returns this view instance.
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
