/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.message;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;

import com.eprovement.poptavka.domain.message.UserMessage;

/**
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
            detail.setClientName(userMessage.getMessage().getDemand()
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
                + ",\n demandTitle=" + getDemandTitle()
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getClientRating() {
        return clientRating;
    }

    public void setClientRating(int clientRating) {
        this.clientRating = clientRating;
    }
}