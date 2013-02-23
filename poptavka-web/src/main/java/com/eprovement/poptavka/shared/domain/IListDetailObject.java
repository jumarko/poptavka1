/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;


import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author mato
 */
public interface IListDetailObject extends IsSerializable {

    Long getId();

    String getName();
}
