package com.eprovement.poptavka.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RemoteService {
    void doPost(RemoteService service, HttpServletRequest request, HttpServletResponse response);

}
