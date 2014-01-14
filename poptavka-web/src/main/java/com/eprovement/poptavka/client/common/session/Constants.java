/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.session;

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
    public static final int READ = 0;
    public static final int UNREAD = 1;
    public static final int STARED = 2;
    public static final int UNSTARED = 3;

    /**************************************************************************/
    /* Common Contants                                                        */
    /**************************************************************************/
    public static final int NONE = 4;
    public static final int SKIP = 5;

    /**************************************************************************/
    /* Module Views Contants                                                  */
    /**************************************************************************/
    public static final int HOME_WELCOME_MODULE = 6;
    public static final int HOME_DEMANDS_MODULE = 7;
    public static final int HOME_SUPPLIERS_MODULE = 8;
    public static final int HOME_SEARCH_MODULE = 9;

    public static final int USER_CLIENT_MODULE = 10;
    public static final int USER_SUPPLIER_MODULE = 11;
    public static final int USER_DEMANDS_MODULE = 12;
    public static final int USER_MESSAGES_MODULE = 13;
    public static final int USER_SETTINGS_MODULE = 14;
    public static final int USER_ADMININSTRATION_MODULE = 15;

    public static final int CAT_LOC_SELECTOR_MODULE = 70;

    /**************************************************************************/
    /* Widget Views Contants                                                  */
    /**************************************************************************/
    /* Home demands module - view types */
    public static final int HOME_DEMANDS_BY_DEFAULT = 16;
    public static final int HOME_DEMANDS_BY_WELCOME = 17;
    public static final int HOME_DEMANDS_BY_SEARCH = 18;
    /* Home suppliers module - view types */
    public static final int HOME_SUPPLIERS_BY_DEFAULT = 19;
    public static final int HOME_SUPPLIERS_BY_SEARCH = 20;

    public static final int CREATE_SUPPLIER = 22;
    public static final int CREATE_DEMAND = 23;

    /* Demands module */
    // Client
    public static final int CLIENT_DEMANDS_WELCOME = 24;
    public static final int CLIENT_DEMANDS = 25;
    public static final int CLIENT_DEMAND_DISCUSSIONS = 26;
    public static final int CLIENT_OFFERED_DEMANDS = 27;
    public static final int CLIENT_OFFERED_DEMAND_OFFERS = 28;
    public static final int CLIENT_ASSIGNED_DEMANDS = 29;
    public static final int CLIENT_CLOSED_DEMANDS = 30;
    public static final int CLIENT_RATINGS = 31;
    // Supplier
    public static final int SUPPLIER_DEMANDS_WELCOME = 32;
    public static final int SUPPLIER_POTENTIAL_DEMANDS = 33;
    public static final int SUPPLIER_OFFERS = 34;
    public static final int SUPPLIER_ASSIGNED_DEMANDS = 35;
    public static final int SUPPLIER_CLOSED_DEMANDS = 36;
    public static final int SUPPLIER_RATINGS = 37;

    /* Messages module constants */
    public static final int MESSAGES_INBOX = 38;
    public static final int MESSAGES_SENT = 39;
    public static final int MESSAGES_DRAFT = 40;
    public static final int MESSAGES_TRASH = 41;
    //
    public static final int MESSAGES_COMPOSE_NEW = 42;
    public static final int MESSAGES_COMPOSE_REPLY = 43;

    /* Administration module contants */
    public static final int ADMIN_ACCESS_ROLE = 44;
    public static final int ADMIN_ACTIVE_DEMANDS = 45;
    public static final int ADMIN_CLIENTS = 46;
    public static final int ADMIN_DEMANDS = 47;
    public static final int ADMIN_EMAILS_ACTIVATION = 48;
    public static final int ADMIN_INVOICES = 49;
    public static final int ADMIN_MESSAGES = 50;
    public static final int ADMIN_NEW_DEMANDS = 51;
    public static final int ADMIN_OFFERS = 52;
    public static final int ADMIN_OUR_PAYMENT_DETAILS = 53;
    public static final int ADMIN_PAYMENT_METHODS = 54;
    public static final int ADMIN_PERMISSIONS = 55;
    public static final int ADMIN_PREFERENCES = 56;
    public static final int ADMIN_PROBLEMS = 57;
    public static final int ADMIN_SUPPLIERS = 58;

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
    public static final int WITHOUT_CHECK_BOXES = 59;
    public static final int WITH_CHECK_BOXES = 60;
    public static final int WITH_CHECK_BOXES_ONLY_ON_LEAFS = 61;
    // DisplayCountOfWhat
    public static final int DISPLAY_COUNT_OF_DEMANDS = 62;
    public static final int DISPLAY_COUNT_OF_SUPPLIERS = 63;
    public static final int DISPLAY_COUNT_DISABLED = 64;

    /**************************************************************************/
    /* Constants for subject name in Send us an email widget                  */
    /**************************************************************************/
    public static final int SUBJECT_REPORT_USER = 65;
    public static final int SUBJECT_REPORT_ISSUE = 66;
    public static final int SUBJECT_PARTNERSHIP = 67;
    public static final int SUBJECT_HELP = 68;
    public static final int SUBJECT_GENERAL_QUESTION = 69;

    /**************************************************************************/
    /* Address selector                                                       */
    /**************************************************************************/
    public static final int MIN_CHARS_TO_SEARCH = 3;
    public static final String COUNTRY = "United States";
    public static final String DISTRICT = "";

    /**************************************************************************/
    /* Other Constants                                                        */
    /**************************************************************************/
    public static final int RATE_1 = 100;
    public static final int RATE_2 = 80;
    public static final int RATE_3 = 50;
    public static final int RATE_4 = 10;
    public static final int RATE_5 = 0;

    public static final int DAYS_URGENCY_HIGH = 5;
    public static final int DAYS_URGENCY_HIGHER = 14;
    public static final int MONTHS_URGENCY_NORMAL = 1;

    public static final String ACT = "act";
    public static final int ENTER_KEY_CODE = 13;
    public static final int THANK_YOU_POPUP_DISPLAY_TIME = 3000;
    public static final int VALIDATION_TOOLTIP_DISPLAY_TIME = 5000;
    public static final int SUGGESTBOX_DELAY = 600;
    public static final int REGISTER_MAX_CATEGORIES = 5;
    public static final int REGISTER_MAX_LOCALITIES = 5;
    public static final String PATH_TO_BUSINESS_DATA = "businessUser.businessUserData.";
    public static final String PATH_TO_TOKEN_FOR_VIEWS = "homeWelcome/view?";

    public static final int SHORT_PASSWORD = 5;
    public static final int LONG_PASSWORD = 8;
    public static final int FEEDBACK_COMMENT_MAX_LENGTH = 255;

    public static final String[] PAGER_SIZE_ITEMS = {"10", "20", "30"};
    public static final int PAGER_SIZE_DEFAULT = 10;

    public static final String SLIDE_PX_SUBMENU = "250px";
    public static final String SLIDE_PX_DETAIL = "350px";

    public static final int TABLE_MARGINS_LARGE = 60; //20top_margin+20top_padding+20bottom_padding
    public static final int TABLE_MARGINS_SMALL = 30; //10top_margin+10top_padding+10bottom_padding
}
