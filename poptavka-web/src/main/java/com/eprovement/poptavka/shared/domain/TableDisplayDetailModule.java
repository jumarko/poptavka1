/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;

/**
 *
 * @author Mato
 */
public interface TableDisplayDetailModule {

    //Demand detail tab
    long getDemandId();

    //User detail tab
    long getUserId();

    //Conversation detail tab
    long getThreadRootId();

    long getSenderId();
}
