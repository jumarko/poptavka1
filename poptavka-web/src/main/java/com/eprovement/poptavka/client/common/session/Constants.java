package com.eprovement.poptavka.client.common.session;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 * Class holds constants used as reference between and within modules.
 *
 * @author Martin Slavkovsky
 */
public final class Constants {

    private Constants() {
    }

    private static final Constants INSTANCE = new Constants();

    public static Constants get() {
        return INSTANCE;
    }

    /**************************************************************************/
    /* Common Contants                                                        */
    /**************************************************************************/
    public static final int NONE = 0;

    /**************************************************************************/
    /* Module Views Contants                                                  */
    /**************************************************************************/
    public static final int HOME_WELCOME_MODULE = -1;
    public static final int HOME_DEMANDS_MODULE = -2;
    public static final int HOME_SUPPLIERS_MODULE = -3;
    public static final int HOME_DEMAND_CREATION_MODULE = -4;
    public static final int HOME_SUPPLIER_CREATION_MODULE = -5;

    public static final int USER_CLIENT_MODULE = -10;
    public static final int USER_SUPPLIER_MODULE = -11;
    public static final int USER_DEMANDS_MODULE = -6;
    public static final int USER_MESSAGES_MODULE = -7;
    public static final int USER_SETTINGS_MODULE = -8;
    public static final int USER_ADMININSTRATION_MODULE = -9;

    /**************************************************************************/
    /* Table Views Contants                                                   */
    /**************************************************************************/
    /* Home demands module */
    public static final int HOME_DEMANDS = 8;
    /* Home suppliers module */
    public static final int HOME_SUPPLIERS = 9;
    public static final int HOME_CREATE_SUPPLIERS = 1;
    public static final int HOME_CREATE_DEMAND = 2;

    /* Demands module */
    // Client
    public static final int CLIENT_PROJECTS = 10;
    public static final int CLIENT_PROJECT_DISCUSSIONS = 18;
    public static final int CLIENT_OFFERED_PROJECTS = 11;
    public static final int CLIENT_OFFERED_PROJECT_CONTESTANTS = 19;
    public static final int CLIENT_ASSIGNED_PROJECTS = 12;
    // Supplier
    public static final int SUPPLIER_POTENTIAL_PROJECTS = 15;
    public static final int SUPPLIER_CONTESTS = 16;
    public static final int SUPPLIER_ASSIGNED_PROJECTS = 17;

    /* Messages module constants */
    public static final int MESSAGES_INBOX = 20;
    public static final int MESSAGES_SENT = 21;
    public static final int MESSAGES_DRAFT = 22;
    public static final int MESSAGES_TRASH = 23;
    //
    public static final int MESSAGES_COMPOSE_NEW = 24;
    public static final int MESSAGES_COMPOSE_REPLY = 25;

    /* Administration module contants */
    public static final int ADMIN_ACCESS_ROLE = 100;
    public static final int ADMIN_CLIENTS = 101;
    public static final int ADMIN_DEMANDS = 102;
    public static final int ADMIN_EMAILS_ACTIVATION = 103;
    public static final int ADMIN_INVOICES = 104;
    public static final int ADMIN_MESSAGES = 105;
    public static final int ADMIN_OFFERS = 106;
    public static final int ADMIN_OUR_PAYMENT_DETAILS = 107;
    public static final int ADMIN_PAYMENT_METHODS = 108;
    public static final int ADMIN_PERMISSIONS = 109;
    public static final int ADMIN_PREFERENCES = 110;
    public static final int ADMIN_PROBLEMS = 111;
    public static final int ADMIN_SUPPLIERS = 112;

    public static ArrayList<Integer> getHomeConstants() {
        ArrayList<Integer> admins = new ArrayList<Integer>();
        admins.add(HOME_DEMANDS);
        admins.add(HOME_SUPPLIERS);
        return admins;
    }

    public static ArrayList<Integer> getClientConstants() {
        ArrayList<Integer> clients = new ArrayList<Integer>();
        clients.add(CLIENT_PROJECTS);
        clients.add(CLIENT_PROJECT_DISCUSSIONS);
        clients.add(CLIENT_OFFERED_PROJECTS);
        clients.add(CLIENT_OFFERED_PROJECT_CONTESTANTS);
        clients.add(CLIENT_ASSIGNED_PROJECTS);
        return clients;
    }

    public static ArrayList<Integer> getSupplierConstants() {
        ArrayList<Integer> suppliers = new ArrayList<Integer>();
        suppliers.add(SUPPLIER_POTENTIAL_PROJECTS);
        suppliers.add(SUPPLIER_CONTESTS);
        suppliers.add(SUPPLIER_ASSIGNED_PROJECTS);
        return suppliers;
    }

    //TODO Martin - remove and redirect usage to getClientCOnstatnts and SuppliersConstants
    public static ArrayList<Integer> getDemandsConstants() {
        ArrayList<Integer> admins = new ArrayList<Integer>();
        admins.add(CLIENT_ASSIGNED_PROJECTS);
        admins.add(CLIENT_PROJECTS);
        admins.add(SUPPLIER_ASSIGNED_PROJECTS);
        admins.add(SUPPLIER_POTENTIAL_PROJECTS);
        admins.add(SUPPLIER_CONTESTS);
        return admins;
    }

    public static ArrayList<Integer> getMessagesConstants() {
        ArrayList<Integer> admins = new ArrayList<Integer>();
        admins.add(MESSAGES_COMPOSE_NEW);
        admins.add(MESSAGES_COMPOSE_REPLY);
        admins.add(MESSAGES_DRAFT);
        admins.add(MESSAGES_INBOX);
        admins.add(MESSAGES_SENT);
        admins.add(MESSAGES_TRASH);
        return admins;
    }

    public static ArrayList<Integer> getAdminConstants() {
        ArrayList<Integer> admins = new ArrayList<Integer>();
        admins.add(ADMIN_ACCESS_ROLE);
        admins.add(ADMIN_CLIENTS);
        admins.add(ADMIN_DEMANDS);
        admins.add(ADMIN_EMAILS_ACTIVATION);
        admins.add(ADMIN_INVOICES);
        admins.add(ADMIN_MESSAGES);
        admins.add(ADMIN_OFFERS);
        admins.add(ADMIN_OUR_PAYMENT_DETAILS);
        admins.add(ADMIN_PAYMENT_METHODS);
        admins.add(ADMIN_PERMISSIONS);
        admins.add(ADMIN_PREFERENCES);
        admins.add(ADMIN_PROBLEMS);
        admins.add(ADMIN_SUPPLIERS);
        return admins;
    }

    /**************************************************************************/
    /* Category tree view Model                                               */
    /**************************************************************************/
    public static final int WITHOUT_CHECK_BOXES = 1000;
    public static final int WITH_CHECK_BOXES = 1001;
    public static final int WITH_CHECK_BOXES_ONLY_ON_LEAFS = 1002;
}
