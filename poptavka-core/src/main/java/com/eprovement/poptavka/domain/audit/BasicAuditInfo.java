package com.eprovement.poptavka.domain.audit;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Represents basic audit information that are available for all entities annotates
 * by {@link AuditedBasic}.
 * All {@link AuditedBasic} entities are stored into the common table, because the records has the same structure
 * and there are most two records for each table:
 * <ul>
 *  <li>basic info about date of creation and which user created audited entity</li>
 *  <li><basic info about date of modification (if any has happened) and user that has modified audited entity/li>
 * </ul>
 *
 *
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
@Entity
public class BasicAuditInfo extends DomainObject {

    private Date insertTime;

    private Date modificationTime;

    private Long userIdCreated;

    private Long userIdModified;

    /** Status of this entity. The default status is "a" as active. */
    private String status = "a";


    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }

    public Long getUserIdCreated() {
        return userIdCreated;
    }

    public void setUserIdCreated(Long userIdCreated) {
        this.userIdCreated = userIdCreated;
    }

    public Long getUserIdModified() {
        return userIdModified;
    }

    public void setUserIdModified(Long userIdModified) {
        this.userIdModified = userIdModified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BasicAuditInfo");
        sb.append("{insertTime=").append(insertTime);
        sb.append(", modificationTime=").append(modificationTime);
        sb.append(", userIdCreated=").append(userIdCreated);
        sb.append(", userIdModified=").append(userIdModified);
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
