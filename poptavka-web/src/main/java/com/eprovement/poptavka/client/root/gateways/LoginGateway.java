/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.mvp4g.client.annotation.Event;

/**
 * Gateway interface for Address selector module.
 * Defines which methods are accessible to the world.
 *
 * @author Martin Slavkovsky
 */
public interface LoginGateway {

    @Event(forwardToParent = true)
    void login(int widgetToLoad);

    @Event(forwardToParent = true)
    void loginFromSession(int widgetToLoad);

    @Event(forwardToParent = true)
    void autoLogin(String email, String password, int widgetToLoad);
}
