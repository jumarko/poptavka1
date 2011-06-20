/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.message;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ivan.vlcek
 */
public class ClientDemandMessageDetail {

    private long messageId;
    private long threadRoodId;
    private long demandId;
    private long senderId;
    private int unreadSubmessages;
    // demand fields
    private String demandTitle;
    private Date endDate;
    private Date expiryDate;
    private BigDecimal price;
}
