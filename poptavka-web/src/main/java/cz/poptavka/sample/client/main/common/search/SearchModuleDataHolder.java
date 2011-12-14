package cz.poptavka.sample.client.main.common.search;

import cz.poptavka.sample.client.main.common.search.dataHolders.AdminAccessRoles;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminClients;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminDemands;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminEmailActivation;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminInvoices;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminMessages;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminOffers;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminPaymentMethods;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminPermissions;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminPreferences;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminProblems;
import cz.poptavka.sample.client.main.common.search.dataHolders.AdminSuppliers;
import cz.poptavka.sample.client.main.common.search.dataHolders.HomeDemands;
import cz.poptavka.sample.client.main.common.search.dataHolders.HomeSuppliers;
import cz.poptavka.sample.client.main.common.search.dataHolders.MessagesTab;
import cz.poptavka.sample.client.main.common.search.dataHolders.PotentialDemandMessages;
import java.io.Serializable;

/**
 * Before using classes init method must be called.
 *
 * @author Martin Slavkovsky
 */
public class SearchModuleDataHolder implements Serializable {

    private Common common = null;
    private HomeDemands homeDemands = null;
    private HomeSuppliers homeSuppliers = null;
    private PotentialDemandMessages potentialDemandMessages = null;
    private AdminAccessRoles adminAccessRoles = null;
    private AdminClients adminClients = null;
    private AdminDemands adminDemands = null;
    private AdminEmailActivation adminEmailActivation = null;
    private AdminInvoices adminInvoice = null;
    private AdminMessages adminMessages = null;
    private AdminOffers adminOffers = null;
    private AdminPaymentMethods adminPaymentMethods = null;
    private AdminPermissions adminPermissions = null;
    private AdminPreferences adminPreferences = null;
    private AdminProblems adminProblems = null;
    private AdminSuppliers adminSuppliers = null;
    private MessagesTab messagesTab = null;

    public void initCommon() {
        common = new Common();
    }

    public void initHomeDemands() {
        homeDemands = new HomeDemands();
    }

    public void initHomeSuppliers() {
        homeSuppliers = new HomeSuppliers();
    }

    public void initPotentialDemandMessages() {
        potentialDemandMessages = new PotentialDemandMessages();
    }

    public void initAdminAccessRoles() {
        adminAccessRoles = new AdminAccessRoles();
    }

    public void initAdminClients() {
        adminClients = new AdminClients();
    }

    public void initAdminDemands() {
        adminDemands = new AdminDemands();
    }

    public void initAdminEmailActivation() {
        adminEmailActivation = new AdminEmailActivation();
    }

    public void initAdminInvoices() {
        adminInvoice = new AdminInvoices();
    }

    public void initAdminMessages() {
        adminMessages = new AdminMessages();
    }

    public void initAdminOffers() {
        adminOffers = new AdminOffers();
    }

    public void initAdminPaymentMethods() {
        adminPaymentMethods = new AdminPaymentMethods();
    }

    public void initAdminPermissions() {
        adminPermissions = new AdminPermissions();
    }

    public void initAdminPreferences() {
        adminPreferences = new AdminPreferences();
    }

    public void initAdminProblems() {
        adminProblems = new AdminProblems();
    }

    public void initAdminSuppliers() {
        adminSuppliers = new AdminSuppliers();
    }

    public void initMessagesTab() {
        messagesTab = new MessagesTab();
    }

    public AdminAccessRoles getAdminAccessRoles() {
        return adminAccessRoles;
    }

    public AdminClients getAdminClients() {
        return adminClients;
    }

    public AdminDemands getAdminDemands() {
        return adminDemands;
    }

    public AdminEmailActivation getAdminEmailActivation() {
        return adminEmailActivation;
    }

    public AdminInvoices getAdminInvoice() {
        return adminInvoice;
    }

    public AdminMessages getAdminMessages() {
        return adminMessages;
    }

    public AdminOffers getAdminOffers() {
        return adminOffers;
    }

    public AdminPaymentMethods getAdminPaymentMethods() {
        return adminPaymentMethods;
    }

    public AdminPermissions getAdminPermissions() {
        return adminPermissions;
    }

    public AdminPreferences getAdminPreferences() {
        return adminPreferences;
    }

    public AdminProblems getAdminProblems() {
        return adminProblems;
    }

    public AdminSuppliers getAdminSuppliers() {
        return adminSuppliers;
    }

    public Common getCommon() {
        return common;
    }

    public HomeDemands getHomeDemands() {
        return homeDemands;
    }

    public HomeSuppliers getHomeSuppliers() {
        return homeSuppliers;
    }

    public PotentialDemandMessages getPotentialDemandMessages() {
        return potentialDemandMessages;
    }

    public MessagesTab getMessagesTab() {
        return messagesTab;
    }

    /** COMMON - full text search **/
    public static class Common implements Serializable {

        private String text = "";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
