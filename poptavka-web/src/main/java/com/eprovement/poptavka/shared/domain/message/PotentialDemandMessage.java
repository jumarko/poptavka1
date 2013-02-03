package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.domain.enums.OfferStateType;
import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

public class PotentialDemandMessage extends DemandMessageDetail implements Serializable, TableDisplay {

    /**
     *
     */
    private static final long serialVersionUID = -6105359783491407143L;
    private String clientName;
    private Integer clientRating;

    public static final ProvidesKey<PotentialDemandMessage> KEY_PROVIDER =
            new ProvidesKey<PotentialDemandMessage>() {

                @Override
                public Object getKey(PotentialDemandMessage item) {
                    return item == null ? null : item.getDemandId();
                }
            };


//    @Override
    public int getClientRating() {
        return (clientRating == null ? 0 : clientRating.intValue());
    }

    public void setClientRating(Integer clientRating) {
        this.clientRating = clientRating;
    }

//    @Override
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public Date getExpireDate() {
        return null;
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
