package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

import cz.poptavka.sample.domain.message.UserMessage;

public class PotentialDemandMessage extends DemandMessageDetail implements Serializable, TableDisplay {

    /**
     *
     */
    private static final long serialVersionUID = -6105359783491407143L;
    private String clientName;
    private Integer clientRating;

    public static PotentialDemandMessage createMessageDetail(UserMessage message) {
        return fillMessageDetail(new PotentialDemandMessage(), message);
    }

    public static PotentialDemandMessage fillMessageDetail(PotentialDemandMessage detail,
            UserMessage userMessage) {
        DemandMessageDetail.fillMessageDetail(detail, userMessage);
        detail.setSender(userMessage.getMessage().getDemand()
                .getClient().getBusinessUser().getBusinessUserData().getDisplayName());
        detail.setClientRating(userMessage.getMessage().getDemand().getClient().getOveralRating());
        return detail;
    }
    public static final ProvidesKey<PotentialDemandMessage> KEY_PROVIDER = new ProvidesKey<PotentialDemandMessage>() {

        @Override
        public Object getKey(PotentialDemandMessage item) {
            return item == null ? null : item.getDemandId();
        }
    };

    @Override
    public Date getExpireDate() {
        return null;
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
    public String getSender() {
        return clientName;
    }

    public void setSender(String name) {
        this.clientName = name;
    }

    @Override
    public int getRating() {
        return (clientRating == null ? 0 : clientRating.intValue());
    }

    public void setClientRating(Integer clientRating) {
        this.clientRating = clientRating;
    }
}
