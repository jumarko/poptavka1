package cz.poptavka.sample.client.user.messages.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.user.demands.widget.DemandDetailView;
import cz.poptavka.sample.client.user.messages.widget.UserConversationPanel;

public class MessagesDetailWrapperView extends Composite
    implements MessagesDetailWrapperPresenter.IDetailWrapper {

    private static MessagesDetailWrapperViewUiBinder uiBinder = GWT.create(MessagesDetailWrapperViewUiBinder.class);
    interface MessagesDetailWrapperViewUiBinder extends UiBinder<Widget, MessagesDetailWrapperView> {   }

    @UiField HTMLPanel container;
    @UiField SimplePanel detailHolder;
    @UiField UserConversationPanel conversationPanel;
    @UiField SimplePanel replyHolder;
    @UiField StackLayoutPanel stackConversationPanel;
    @UiField DemandDetailView demandDetail;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        GWT.log("DEMAND DETAIL view LOADED");
        GWT.log("Visible stackLayouPanel:" + stackConversationPanel.isVisible());
        stackConversationPanel.setSize("100%", "66%");
        // Add the Mail folders.
        Widget mailHeader = createHeaderWidget(
            "Pokus");
        Widget mailHeader1 = createHeaderWidget(
                "Pokus1");
        Widget mailHeader2 = createHeaderWidget(
                "Pokus2");
        stackConversationPanel.add(conversationPanel, mailHeader, 4);
        stackConversationPanel.add(demandDetail, mailHeader1, 4);
        stackConversationPanel.add(createMailItem(), mailHeader2, 4);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setDetail(Widget demandDetailWidget) {
        detailHolder.setWidget(demandDetailWidget);
    }

    @Override
    public UserConversationPanel getConversationPanel() {
        return conversationPanel;
    }

    @Override
    public SimplePanel getReplyHolder() {
        return replyHolder;
    }

    @Override
    public SimplePanel getDetailHolder() {
        return detailHolder;
    }

    public StackLayoutPanel getStackConversationPanel() {
        return stackConversationPanel;
    }

    /**
     * Create a widget to display in the header that includes an image and some
     * text.
     *
     * @param text the header text
     * @param image the {@link ImageResource} to add next to the header
     * @return the header widget
     */
    private Widget createHeaderWidget(String text) {
        // Add the image and text to a horizontal panel
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setHeight("100%");
        hPanel.setSpacing(0);
        hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        HTML headerText = new HTML(text);
        headerText.setStyleName("cw-StackPanelHeader");
        hPanel.add(headerText);
        return new SimplePanel(hPanel);
    }

    /**
     * Create the {@link Tree} of Mail options.
     *
     * @param images the {@link Images} used in the Mail options
     * @return the {@link Tree} of mail options
     */
    private Widget createMailItem() {
        Tree mailPanel = new Tree();
        TreeItem mailPanelRoot = mailPanel.addItem("foo@example.com");
        String[] mailFolders = {"inbox", "drafts", "templates", "sent", "trash"};
        addItem(mailPanelRoot, mailFolders[0]);
        addItem(mailPanelRoot, mailFolders[1]);
        addItem(mailPanelRoot, mailFolders[2]);
        addItem(mailPanelRoot, mailFolders[3]);
        addItem(mailPanelRoot, mailFolders[4]);
        mailPanelRoot.setState(true);
        return mailPanel;
    }

    /**
     * Add a {@link TreeItem} to a root item.
     *
     * @param root the root {@link TreeItem}
     * @param image the icon for the new child item
     * @param label the label for the child icon
     */
    private void addItem(TreeItem root, String label) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendEscaped(" ").appendEscaped(label);
        root.addItem(sb.toSafeHtml());
    }

    public DemandDetailView getDemandDetail() {
        return demandDetail;
    }

}
