package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;

/** ADMINCLIENT **/
public class AdminClients implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private String companyName = null;
    private String firstName = null;
    private String lastName = null;
    private Integer ratingFrom = null;
    private Integer ratingTo = null;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getRatingFrom() {
        return ratingFrom;
    }

    public void setRatingFrom(Integer ratingFrom) {
        this.ratingFrom = ratingFrom;
    }

    public Integer getRatingTo() {
        return ratingTo;
    }

    public void setRatingTo(Integer ratingTo) {
        this.ratingTo = ratingTo;
    }
}