/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.client.user.widget.grid.TableDisplayDisplayName;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 *
 * @author ivan.vlcek
 */
public class ClientDemandMessageDetail extends DemandMessageDetail
        implements IsSerializable, TableDisplay, TableDisplayDisplayName {

    private String displayName;
    private int clientRating;



    @Override
    public String toString() {
        return "ClientDemandMessageDetail{\n" + "messageId=" + getMessageId()
                + ",\n threadRoodId=" + getThreadRootId()
                + ",\n demandId=" + getDemandId()
                + ",\n senderId=" + getSenderId()
                + ",\n demandTitle=" + getDemandTitle()
                + ",\n demandStatus=" + getDemandStatus()
                + ",\n messagesCount=" + getMessageCount()
                + ",\n unreadSubMessages=" + getUnreadSubMessages()
                + ",\n endDate=" + getEndDate()
                + ",\n validToDate=" + getValidTo()
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
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getClientRating() {
        return clientRating;
    }

    public void setClientRating(int clientRating) {
        this.clientRating = clientRating;
    }

    @Override
    public OfferStateType getOfferState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setIsStarred(boolean value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getUnreadSubmessagesCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}