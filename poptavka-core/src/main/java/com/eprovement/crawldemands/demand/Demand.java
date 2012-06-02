package com.eprovement.crawldemands.demand;

import java.io.Serializable;

/**
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
public class Demand implements Serializable {

    private String name;
    private String link;
    private String category;
    private String locality;
    private String description;
    private String originalHtml;
    private String attractive;
    private String dateOfCreation;
    private String company;
    private String contactPerson;
    private String street;
    private String city;
    private String email;
    private String phone;
    private String ico;
    private String linkToCommercialRegister;
    private String www;
    private String clientDescription;
    private String dic;

    //polished attributes
    private String localityId;

    private String endDate;
    private String validTo;
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the locality
     */
    public String getLocality() {
        return locality;
    }

    /**
     * @param locality the locality to set
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the originalHtml
     */
    public String getOriginalHtml() {
        return originalHtml;
    }

    /**
     * @param originalHtml the originalHtml to set
     */
    public void setOriginalHtml(String originalHtml) {
        this.originalHtml = originalHtml;
    }

    /**
     * @return the attractive
     */
    public String getAttractive() {
        return attractive;
    }

    /**
     * @param attractive the attractive to set
     */
    public void setAttractive(String attractive) {
        this.attractive = attractive;
    }

    /**
     * @return the dateOfCreation
     */
    public String getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * @param dateOfCreation the dateOfCreation to set
     */
    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the contactPerson
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * @param contactPerson the contactPerson to set
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the ico
     */
    public String getIco() {
        return ico;
    }

    /**
     * @param ico the ico to set
     */
    public void setIco(String ico) {
        this.ico = ico;
    }

    /**
     * @return the linkToCommercialRegister
     */
    public String getLinkToCommercialRegister() {
        return linkToCommercialRegister;
    }

    /**
     * @param linkToCommercialRegister the linkToCommercialRegister to set
     */
    public void setLinkToCommercialRegister(String linkToCommercialRegister) {
        this.linkToCommercialRegister = linkToCommercialRegister;
    }

    /**
     * @return the www
     */
    public String getWww() {
        return www;
    }

    /**
     * @param www the www to set
     */
    public void setWww(String www) {
        this.www = www;
    }

    /**
     * @return the clientDescription
     */
    public String getClientDescription() {
        return clientDescription;
    }

    /**
     * @param clientDescription the clientDescription to set
     */
    public void setClientDescription(String clientDescription) {
        this.clientDescription = clientDescription;
    }

    /**
     * @return the dic
     */
    public String getDic() {
        return dic;
    }

    /**
     * @param dic the dic to set
     */
    public void setDic(String dic) {
        this.dic = dic;
    }

    /**
     * @return the localityId
     */
    public String getLocalityId() {
        return localityId;
    }

    /**
     * @param localityId the localityId to set
     */
    public void setLocalityId(String localityId) {
        this.localityId = localityId;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the validTo
     */
    public String getValidTo() {
        return validTo;
    }

    /**
     * @param validTo the validTo to set
     */
    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Demand");
        sb.append("{name='").append(name).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", locality='").append(locality).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", originalHtml='").append(originalHtml).append('\'');
        sb.append(", attractive='").append(attractive).append('\'');
        sb.append(", dateOfCreation='").append(dateOfCreation).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", contactPerson='").append(contactPerson).append('\'');
        sb.append(", street='").append(street).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", ico='").append(ico).append('\'');
        sb.append(", linkToCommercialRegister='").append(linkToCommercialRegister).append('\'');
        sb.append(", www='").append(www).append('\'');
        sb.append(", clientDescription='").append(clientDescription).append('\'');
        sb.append(", dic='").append(dic).append('\'');
        sb.append(", localityId='").append(localityId).append('\'');
        sb.append(", endDate='").append(endDate).append('\'');
        sb.append(", validTo='").append(validTo).append('\'');
        sb.append('}');
        return sb.toString();
    }


}
