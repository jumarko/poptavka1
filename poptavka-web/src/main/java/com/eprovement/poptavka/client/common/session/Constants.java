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
    /* Action box Contants                                                    */
    /**************************************************************************/
    public static final int READ = -20;
    public static final int UNREAD = -21;
    public static final int STARED = -22;
    public static final int UNSTARED = -23;

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
    /* Home demands module - view types */
    public static final int HOME_DEMANDS_BY_DEFAULT = 80;
    public static final int HOME_DEMANDS_BY_WELCOME = 81;
    public static final int HOME_DEMANDS_BY_SEARCH = 82;
    /* Home suppliers module - view types */
    public static final int HOME_SUPPLIERS_BY_DEFAULT = 90;
    public static final int HOME_SUPPLIERS_BY_SEARCH = 91;
    public static final int HOME_SUPPLIERS_BY_HISTORY = 92;
    //
    public static final int CREATE_SUPPLIERS = 1;
    public static final int CREATE_DEMAND = 2;

    /* Demands module */
    // Client
    public static final int CLIENT_DEMANDS_WELCOME = 9;
    public static final int CLIENT_DEMANDS = 10;
    public static final int CLIENT_DEMAND_DISCUSSIONS = 18;
    public static final int CLIENT_OFFERED_DEMANDS = 11;
    public static final int CLIENT_OFFERED_DEMAND_OFFERS = 19;
    public static final int CLIENT_ASSIGNED_DEMANDS = 12;
    // Supplier
    public static final int SUPPLIER_DEMANDS_WELCOME = 14;
    public static final int SUPPLIER_POTENTIAL_DEMANDS = 15;
    public static final int SUPPLIER_OFFERS = 16;
    public static final int SUPPLIER_ASSIGNED_DEMANDS = 17;

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
        admins.add(HOME_DEMANDS_BY_DEFAULT);
        admins.add(HOME_SUPPLIERS_BY_DEFAULT);
        return admins;
    }

    public static ArrayList<Integer> getClientDemandsConstants() {
        ArrayList<Integer> clients = new ArrayList<Integer>();
        clients.add(CLIENT_DEMANDS);
        clients.add(CLIENT_DEMAND_DISCUSSIONS);
        clients.add(CLIENT_OFFERED_DEMANDS);
        clients.add(CLIENT_OFFERED_DEMAND_OFFERS);
        clients.add(CLIENT_ASSIGNED_DEMANDS);
        return clients;
    }

    public static ArrayList<Integer> getSupplierDemandsConstants() {
        ArrayList<Integer> suppliers = new ArrayList<Integer>();
        suppliers.add(SUPPLIER_POTENTIAL_DEMANDS);
        suppliers.add(SUPPLIER_OFFERS);
        suppliers.add(SUPPLIER_ASSIGNED_DEMANDS);
        return suppliers;
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

    /**************************************************************************/
    /* Constants for subject name in Send us an email widget                  */
    /**************************************************************************/
    public static final int SUBJECT_REPORT_USER = 4;
    public static final int SUBJECT_REPORT_ISSUE = 3;
    public static final int SUBJECT_PARTNERSHIP = 2;
    public static final int SUBJECT_HELP = 1;
    public static final int SUBJECT_GENERAL_QUESTION = 0;

}
