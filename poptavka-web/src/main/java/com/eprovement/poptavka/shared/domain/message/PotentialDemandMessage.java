package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.client.user.widget.grid.TableDisplayDisplayName;
import com.eprovement.poptavka.domain.enums.OfferStateType;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

public class PotentialDemandMessage extends DemandMessageDetail
    implements IsSerializable, TableDisplay, TableDisplayDisplayName {

    private String displayName;
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

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
