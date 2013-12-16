package com.eprovement.poptavka.domain.address;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 * Entity represents mapping between region name abbreviations and regions.
 */
@Entity
@NamedQuery(name = "getRegionByAbbreviation",
            query = "select r.region from RegionAbbreviation r where r.abbreviation = upper(:abbreviation)")

public class RegionAbbreviation extends DomainObject {

    /** Typical abbrev. is a two letters long but we are prepared for another form. */
    @Column(unique = true, length = 8)
    private String abbreviation;

    @OneToOne(fetch = FetchType.LAZY)
    private Locality region;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Locality getRegion() {
        return region;
    }

    public void setRegion(Locality region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "RegionAbbreviation{"
                + "abbreviation='" + abbreviation + '\''
                + ", region=" + region
                + '}';
    }
}
