package cz.poptavka.sample.shared.domain.demandsModule;

import com.google.gwt.view.client.ProvidesKey;

import cz.poptavka.sample.domain.demand.DemandStatus;

import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.shared.domain.message.TableDisplay;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 */
public class ClientDemandDetail implements Serializable, TableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private long userMessageId;
    private long demandId;
    private DemandStatus demandStatus;
    private String demandTitle; //title
    private BigDecimal price;
    private Date endDate;
    private Date validToDate;
    private boolean read;
    private boolean starred;
    private int messageCount;
    private int unreadSubmessages;

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     *
     * @param demand
     * @return DemandDetail
     */
    public static ClientDemandDetail createDemandDetail(UserMessage userMessage) {
        ClientDemandDetail detail = new ClientDemandDetail();
        detail.setUserMessageId(userMessage.getId());
        detail.setDemandId(userMessage.getMessage().getDemand().getId());
        detail.setDemandStatus(userMessage.getMessage().getDemand().getStatus());
        detail.setTitle(userMessage.getMessage().getDemand().getTitle());
        detail.setPrice(userMessage.getMessage().getDemand().getPrice());
        detail.setEndDate(userMessage.getMessage().getDemand().getEndDate());
        detail.setValidToDate(userMessage.getMessage().getDemand().getValidTo());
        detail.setRead(userMessage.isRead());
        detail.setStarred(userMessage.isStarred());
        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    @Override
    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean isStarred() {
        return starred;
    }

    @Override
    public void setStarred(boolean starred) {
        this.starred = starred;
    }
    public static final ProvidesKey<ClientDemandDetail> KEY_PROVIDER = new ProvidesKey<ClientDemandDetail>() {

        @Override
        public Object getKey(ClientDemandDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    @Override
    public long getMessageId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    @Override
//    public String getTitle() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    @Override
    public Date getCreated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPrice() {
        return price.toString();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    @Override
    public String getFormattedMessageCount() {
        return "(" + getMessageCount() + "/" + getUnreadSubmessages() + ")";
    }

    @Override
    public String getSender() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRating() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getExpireDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getReceivedDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getAcceptedDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        return demandTitle;
    }

    public void setTitle(String title) {
        demandTitle = title;
    }
}
