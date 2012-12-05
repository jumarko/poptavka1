/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import java.io.Serializable;

/**
 *
 * @author Martin Slavkovsky
 */
public class LocalityDetailSuggestion //MultiWordSuggestOracle.MultiWordSuggestion
        implements Serializable, Suggestion {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static final long serialVersionUID = -6029478783391927533L;
    /** Suggestion. **/
    private String displayString;
    private String replacementString;
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
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /** Suggestion. **/
    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    public void setReplacementString(String replacementString) {
        this.replacementString = replacementString;
    }

    public void setSuggestion(String replacementString, String displayString) {
        this.replacementString = replacementString;
        this.displayString = displayString;
    }

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
    /** Suggestion. **/
    @Override
    public String getDisplayString() {
        return displayString;
    }

    @Override
    public String getReplacementString() {
        return replacementString;
    }

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
