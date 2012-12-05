/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import java.io.Serializable;

/**
 *
 * @author Martin Slavkovsky
 */
public class LocalityDetailSuggestion extends MultiWordSuggestOracle.MultiWordSuggestion
        implements Serializable {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static final long serialVersionUID = -6029478783391927533L;
    /** State. **/
    private long stateId;
    private String stateCode;
    private String stateName;
    /** City. **/
    private long cityId;
    private String cityCode;
    private String cityName;

    /**************************************************************************/
    /* INITIALIZATOIN                                                         */
    /**************************************************************************/
    public LocalityDetailSuggestion() {
        super();
    }

    public LocalityDetailSuggestion(String replacementString, String displayString) {
        super(replacementString, displayString);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /** State. **/
    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /** City. **/
    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** State. **/
    public long getStateId() {
        return stateId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    /** City. **/
    public long getCityId() {
        return cityId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return getCityName() + ", " + getStateName();
    }
}
