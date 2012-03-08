/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;

import cz.poptavka.sample.domain.message.UserMessage;
import java.util.Date;

/**
 * Used in DemandModule in client -> MyDemands section.
 * Holds information about demands created by Client.
 *
 * @author ivan.vlcek
 */
public class ClientDemandMessageDetail extends DemandMessageDetail
        implements Serializable, TableDisplay {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String clientName;
    private int clientRating;

    public static DemandMessageDetail fillMessageDetail(ClientDemandMessageDetail detail,
            UserMessage userMessage) {
        MessageDetail.fillMessageDetail(detail, userMessage);
        if (userMessage.getMessage() != null
                && userMessage.getMessage().getDemand() != null
                && userMessage.getMessage().getDemand().getClient() != null
                && userMessage.getMessage().getDemand().getClient().getBusinessUser() != null
                && userMessage.getMessage().getDemand().getClient().getBusinessUser().getBusinessUserData() != null) {
            detail.setSender(userMessage.getMessage().getDemand()
                    .getClient().getBusinessUser().getBusinessUserData().getDisplayName());
        }
        return detail;
    }

    public static ClientDemandMessageDetail createDetail(UserMessage userMessage) {
        ClientDemandMessageDetail detail = new ClientDemandMessageDetail();
        ClientDemandMessageDetail.fillMessageDetail(detail, userMessage);
        return detail;
    }

    @Override
    public String toString() {
        return "ClientDemandMessageDetail{\n" + "messageId=" + getMessageId()
                + ",\n threadRoodId=" + getThreadRootId()
                + ",\n demandId=" + getDemandId()
                + ",\n senderId=" + getSenderId()
                + ",\n unreadSubmessages=" + getUnreadSubmessages()
                + ",\n demandTitle=" + getSender()
                + ",\n demandStatus=" + getDemandStatus()
                + ",\n endDate=" + getEndDate()
                + ",\n validToDate=" + getValidToDate()
                + ",\n price=" + getPrice() + "}\n\n";
    }
    public static final ProvidesKey<ClientDemandMessageDetail> KEY_PROVIDER =
            new ProvidesKey<ClientDemandMessageDetail>() {

                @Override
                public Object getKey(ClientDemandMessageDetail item) {
                    return item == null ? null : item.getDemandId();
                }
            };

    @Override
    public String getSender() {
        return clientName;
    }

    public void setSender(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public int getRating() {
        return clientRating;
    }

    public void setRating(int clientRating) {
        this.clientRating = clientRating;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    @Override
//    public String getPrice() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}
