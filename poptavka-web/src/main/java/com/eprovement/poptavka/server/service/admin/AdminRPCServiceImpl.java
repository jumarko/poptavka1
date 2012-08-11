/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.admin;

import com.eprovement.poptavka.client.service.demand.AdminRPCService;
import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.invoice.Invoice;
import com.eprovement.poptavka.domain.invoice.OurPaymentDetails;
import com.eprovement.poptavka.domain.invoice.PaymentMethod;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.settings.Preference;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Problem;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PreferenceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ProblemDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.MessageType;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;

/*
 * TODO Martin Vsetky count zrobit inak, ked sa bude riesit tento modul. Vsetky
 * updaty dorobit.
 */
/**
 * @author Martin Slavkovsky
 */
@Configurable
public class AdminRPCServiceImpl extends AutoinjectingRemoteService implements AdminRPCService {

    private static final long serialVersionUID = 1132667081084321575L;
    private GeneralService generalService;
    private DemandService demandService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private Converter<Demand, FullDemandDetail> fullDemandConverter;
    private Converter<Client, ClientDetail> clientConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Offer, OfferDetail> offerConverter;
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;
    private Converter<ActivationEmail, ActivationEmailDetail> activationEmailConverter;
    private Converter<Invoice, InvoiceDetail> invoiceConverter;
    private Converter<OurPaymentDetails, PaymentDetail> paymentConverter;
    private Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter;
    private Converter<Permission, PermissionDetail> permissionConverter;
    private Converter<Preference, PreferenceDetail> preferenceConverter;
    private Converter<Problem, ProblemDetail> problemConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<Locality, LocalityDetail> localityConverter;
    private Converter<Category, CategoryDetail> categoryConverter;

    @Autowired
    public void setGeneralService(GeneralService generalService) throws RPCException {
        this.generalService = generalService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) throws RPCException {
        this.demandService = demandService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setFullDemandConverter(
            @Qualifier("fullDemandConverter") Converter<Demand, FullDemandDetail> fullDemandConverter) {
        this.fullDemandConverter = fullDemandConverter;
    }

    @Autowired
    public void setClientConverter(@Qualifier("clientConverter") Converter<Client, ClientDetail> clientConverter) {
        this.clientConverter = clientConverter;
    }

    @Autowired
    public void setSupplierConverter(
            @Qualifier("supplierConverter") Converter<Supplier, FullSupplierDetail> supplierConverter) {
        this.supplierConverter = supplierConverter;
    }

    @Autowired
    public void setOfferConverter(@Qualifier("offerConverter") Converter<Offer, OfferDetail> offerConverter) {
        this.offerConverter = offerConverter;
    }

    @Autowired
    public void setAccessRoleConverter(
            @Qualifier("accessRoleConverter") Converter<AccessRole, AccessRoleDetail> accessRoleConverter) {
        this.accessRoleConverter = accessRoleConverter;
    }

    @Autowired
    public void setActivationEmailConverter(@Qualifier("activationEmailConverter") Converter<ActivationEmail,
            ActivationEmailDetail> activationEmailConverter) {
        this.activationEmailConverter = activationEmailConverter;
    }

    @Autowired
    public void setInvoiceConverter(@Qualifier("invoiceConverter") Converter<Invoice, InvoiceDetail> invoiceConverter) {
        this.invoiceConverter = invoiceConverter;
    }

    @Autowired
    public void setPaymentConverter(
            @Qualifier("paymentConverter") Converter<OurPaymentDetails, PaymentDetail> paymentConverter) {
        this.paymentConverter = paymentConverter;
    }

    @Autowired
    public void setPaymentMethodConverter(
            @Qualifier("paymentMethodConverter") Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter) {
        this.paymentMethodConverter = paymentMethodConverter;
    }

    @Autowired
    public void setPermissionConverter(
            @Qualifier("permissionConverter") Converter<Permission, PermissionDetail> permissionConverter) {
        this.permissionConverter = permissionConverter;
    }

    @Autowired
    public void setPreferenceConverter(
            @Qualifier("preferenceConverter") Converter<Preference, PreferenceDetail> preferenceConverter) {
        this.preferenceConverter = preferenceConverter;
    }

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, LocalityDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setProblemConverter(@Qualifier("problemConverter") Converter<Problem, ProblemDetail> problemConverter) {
        this.problemConverter = problemConverter;
    }

    @Autowired
    public void setMessageConverter(@Qualifier("messageConverter") Converter<Message, MessageDetail> messageConverter) {
        this.messageConverter = messageConverter;
    }

    /**
     * ********************************************************************************************
     *********************** DEMAND SECTION.
     * ************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminDemandsCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Demand.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Demand.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<FullDemandDetail> getAdminDemands(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Demand.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return fullDemandConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateDemand(FullDemandDetail fullDemandDetail) throws RPCException {
        Demand demand = demandService.getById(fullDemandDetail.getDemandId());
        if (!demand.getMaxSuppliers().equals(fullDemandDetail.getMaxOffers())) {
            demand.setMaxSuppliers(fullDemandDetail.getMaxOffers());
        }
        if (!demand.getMinRating().equals(fullDemandDetail.getMinRating())) {
            demand.setMinRating(fullDemandDetail.getMinRating());
        }
        if (!demand.getStatus().getValue().equals(fullDemandDetail.getDemandStatus())) {
            demand.setStatus(fullDemandDetail.getDemandStatus());
        }
        if (!demand.getCreatedDate().equals(fullDemandDetail.getCreated())) {
            demand.setCreatedDate(fullDemandDetail.getCreated());
        }
        if (!demand.getEndDate().equals(fullDemandDetail.getEndDate())) {
            demand.setEndDate(fullDemandDetail.getEndDate());
        }
        if (!demand.getValidTo().equals(fullDemandDetail.getValidToDate())) {
            demand.setValidTo(fullDemandDetail.getValidToDate());
        }
        if (!demand.getTitle().equals(fullDemandDetail.getTitle())) {
            demand.setTitle(fullDemandDetail.getTitle());
        }
        if (!demand.getDescription().equals(fullDemandDetail.getDescription())) {
            demand.setDescription(fullDemandDetail.getDescription());
        }
        if (!demand.getPrice().equals(fullDemandDetail.getPrice())) {
            demand.setPrice(fullDemandDetail.getPrice());
        }
        if (!demand.getType().getDescription().equals(fullDemandDetail.getDemandType())) {
            demand.setType(demandService.getDemandType(fullDemandDetail.getDemandType()));
        }
        //Treba zistovat ci sa kategorie zmenili? Ak ano, ako aby to nebolo narocne?
        demand.setCategories(categoryConverter.convertToSourceList(fullDemandDetail.getCategories()));
        //Treba zistovat ci sa lokality zmenili? Ak ano, ako aby to nebolo narocne?
        demand.setLocalities(localityConverter.convertToSourceList(fullDemandDetail.getLocalities()));

        demandService.update(demand);
    }

    /**
     * ********************************************************************************************
     *********************** CLIENT SECTION.
     * ************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminClientsCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Client.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Client.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ClientDetail> getAdminClients(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Client.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return clientConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateClient(ClientDetail clientDetail) throws RPCException {
        Client client = generalService.find(Client.class, clientDetail.getId());
        if (client.getOveralRating() != clientDetail.getOveralRating()) {
            client.setOveralRating(clientDetail.getOveralRating());
        }
        client.setVerification(Verification.valueOf(clientDetail.getVerification()));
        //TODO Martin - how to update businessUserData, supplierBlackList, demandsIds???
        generalService.merge(client);
    }

    /**
     * ********************************************************************************************
     *********************** SUPPLIER SECTION.
     * ************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Supplier.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Supplier.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<FullSupplierDetail> getAdminSuppliers(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Supplier.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return supplierConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateSupplier(FullSupplierDetail supplierDetail) throws RPCException {
        Supplier supplier = generalService.find(Supplier.class, supplierDetail.getSupplierId());
        generalService.merge(supplier);
    }

    /**
     * ********************************************************************************************
     *********************** OFFER SECTION.
     * ************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminOffersCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Offer.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Offer.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<OfferDetail> getAdminOffers(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Offer.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return offerConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateOffer(OfferDetail offerDetail) throws RPCException {
        Offer offer = generalService.find(Offer.class, offerDetail.getId());
        if (!offer.getPrice().equals(offerDetail.getPrice())) {
            offer.setPrice((BigDecimal) offerDetail.getPrice());
        }
        if (!offer.getCreated().equals(offerDetail.getCreatedDate())) {
            offer.setCreated(offerDetail.getCreatedDate());
        }
        if (!offer.getFinishDate().equals(offerDetail.getFinishDate())) {
            offer.setFinishDate(offerDetail.getFinishDate());
        }
        if (!offer.getSupplier().getBusinessUser().getBusinessUserData().getCompanyName().equals(
                offerDetail.getSupplierName())) {
            offer.getSupplier().getBusinessUser().getBusinessUserData().setCompanyName(offerDetail.getSupplierName());
        }
        //TODO Martin - how to update OfferState??
        if (!offer.getPrice().equals(offerDetail.getPrice())) {
            offer.setPrice((BigDecimal) offerDetail.getPrice());
        }
        generalService.merge(offer);
    }

    /**
     * ********************************************************************************************
     *********************** ACCESS ROLE SECTION.
     * ************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(AccessRole.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(AccessRole.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<AccessRoleDetail> getAdminAccessRoles(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(AccessRole.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return accessRoleConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateAccessRole(AccessRoleDetail accessRoleDetail) throws RPCException {
        AccessRole accessRole = generalService.find(AccessRole.class, accessRoleDetail.getId());
        if (!accessRole.getName().equals(accessRoleDetail.getName())) {
            accessRole.setName(accessRoleDetail.getName());
        }
        if (!accessRole.getDescription().equals(accessRoleDetail.getDescription())) {
            accessRole.setDescription(accessRoleDetail.getDescription());
        }
        if (!accessRole.getCode().equalsIgnoreCase(accessRoleDetail.getCode())) {
            accessRole.setCode(accessRoleDetail.getCode());
        }
        //TODO Martin - update permissions
        generalService.merge(accessRole);
    }

    /**
     * ********************************************************************************************
     *********************** EMAIL ACTIVATION SECTION.
     * *******************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(ActivationEmail.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(ActivationEmail.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ActivationEmailDetail> getAdminEmailsActivation(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(ActivationEmail.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return activationEmailConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateEmailActivation(ActivationEmailDetail emailActivationDetail) throws RPCException {
        ActivationEmail emailActivation = generalService.find(ActivationEmail.class, emailActivationDetail.getId());
        if (!emailActivation.getActivationLink().equals(emailActivationDetail.getActivationLink())) {
            emailActivation.setActivationLink(emailActivationDetail.getActivationLink());
        }
        if (!emailActivation.getValidTo().equals(emailActivationDetail.getTimeout())) {
            emailActivation.setValidTo(emailActivationDetail.getTimeout());
        }
        generalService.merge(emailActivation);
    }

    /**
     * ********************************************************************************************
     *********************** INVOICE SECTION.
     * ****************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Invoice.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Invoice.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<InvoiceDetail> getAdminInvoices(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Invoice.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return invoiceConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateInvoice(InvoiceDetail invoiceDetail) throws RPCException {
        Invoice invoice = generalService.find(Invoice.class, invoiceDetail.getId());
        if (!invoice.getInvoiceNumber().equals(invoiceDetail.getInvoiceNumber())) {
            invoice.setInvoiceNumber(invoiceDetail.getInvoiceNumber());
        }
        //------------------------------ Dates ---------------------------------
        if (!invoice.getIssueDate().equals(invoiceDetail.getIssueDate())) {
            invoice.setIssueDate(invoiceDetail.getIssueDate());
        }
        if (!invoice.getShipmentDate().equals(invoiceDetail.getShipmentDate())) {
            invoice.setShipmentDate(invoiceDetail.getShipmentDate());
        }
        if (!invoice.getDueDate().equals(invoiceDetail.getDueDate())) {
            invoice.setDueDate(invoiceDetail.getDueDate());
        }
        //------------------------ Bank information ----------------------------
        if (!invoice.getBankAccountNumber().equals(invoiceDetail.getBankAccountNumber())) {
            invoice.setBankAccountNumber(invoiceDetail.getBankAccountNumber());
        }
        if (!invoice.getBankCode().equals(invoiceDetail.getBankCode())) {
            invoice.setBankCode(invoiceDetail.getBankCode());
        }
        if (!invoice.getVariableSymbol().equals(invoiceDetail.getVariableSymbol())) {
            invoice.setVariableSymbol(invoiceDetail.getVariableSymbol());
        }
        if (!invoice.getConstSymbol().equals(invoiceDetail.getConstSymbol())) {
            invoice.setConstSymbol(invoiceDetail.getConstSymbol());
        }
        //------------------------------ Price ---------------------------------
        if (!invoice.getTaxBasis().equals(invoiceDetail.getTaxBasis())) {
            invoice.setTaxBasis(invoiceDetail.getTaxBasis());
        }
        if (invoice.getVatRate() != invoiceDetail.getVatRate()) {
            invoice.setVatRate(invoiceDetail.getVatRate());
        }
        if (!invoice.getVat().equals(invoiceDetail.getVat())) {
            invoice.setVat(invoiceDetail.getVat());
        }
        if (!invoice.getTotalPrice().equals(invoiceDetail.getTotalPrice())) {
            invoice.setTotalPrice(invoiceDetail.getTotalPrice());
        }
        if (!invoice.getConstSymbol().equals(invoiceDetail.getConstSymbol())) {
            invoice.setConstSymbol(invoiceDetail.getConstSymbol());
        }
        //TODO Martin - how to update userServices, paymentMethods
        generalService.merge(invoice);
    }

    /**
     * ********************************************************************************************
     *********************** MESSAGE SECTION.
     * ****************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminMessagesCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Message.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Message.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<MessageDetail> getAdminMessages(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Message.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
//        search.setPage(count);
        return messageConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateMessage(MessageDetail messageDetail) throws RPCException {
        Message message = generalService.find(Message.class, messageDetail.getMessageId());
        try {
            //TODO Martin - how to update missing ones
            if (!message.getSubject().equals(messageDetail.getSubject())) {
                message.setSubject(messageDetail.getSubject());
            }
            if (!message.getBody().equals(messageDetail.getBody())) {
                message.setBody(messageDetail.getBody());
            }
            if (!message.getCreated().equals(messageDetail.getCreated())) {
                message.setCreated(messageDetail.getCreated());
            }
            if (!message.getSent().equals(messageDetail.getSent())) {
                message.setSent(messageDetail.getSent());
            }
            if (!message.getMessageState().equals(MessageState.valueOf(messageDetail.getMessageState()))) {
                message.setMessageState(MessageState.valueOf(messageDetail.getMessageState()));
            }

            generalService.merge(message);
        } catch (MessageException ex) {
            Logger.getLogger(AdminRPCServiceImpl.class.getName()).log(Level.SEVERE, "Coudn't update message.", ex);
        }
    }

    /**
     * ********************************************************************************************
     *********************** OUR PAYMENT DETAIL SECTION.
     * *****************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(OurPaymentDetails.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(OurPaymentDetails.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentDetail> getAdminOurPaymentDetails(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(OurPaymentDetails.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return paymentConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateOurPaymentDetail(PaymentDetail paymentDetail) throws RPCException {
        OurPaymentDetails ourPaymentDetails = generalService.find(OurPaymentDetails.class, paymentDetail.getId());
        if (!ourPaymentDetails.getBankAccount().equals(paymentDetail.getBankAccount())) {
            ourPaymentDetails.setBankAccount(paymentDetail.getBankAccount());
        }
        if (!ourPaymentDetails.getBankCode().equals(paymentDetail.getBankCode())) {
            ourPaymentDetails.setBankCode(paymentDetail.getBankCode());
        }
        if (!ourPaymentDetails.getCity().equals(paymentDetail.getCity())) {
            ourPaymentDetails.setCity(paymentDetail.getCity());
        }
        if (!ourPaymentDetails.getCountryVat().equals(paymentDetail.getCountryVat())) {
            ourPaymentDetails.setCountryVat(paymentDetail.getCountryVat());
        }
        if (!ourPaymentDetails.getEmail().equals(paymentDetail.getEmail())) {
            ourPaymentDetails.setEmail(paymentDetail.getEmail());
        }
        if (!ourPaymentDetails.getIban().equals(paymentDetail.getIban())) {
            ourPaymentDetails.setIban(paymentDetail.getIban());
        }
        if (!ourPaymentDetails.getIdentificationNumber().equals(paymentDetail.getIdentificationNumber())) {
            ourPaymentDetails.setIdentificationNumber(paymentDetail.getIdentificationNumber());
        }
        if (!ourPaymentDetails.getPhone().equals(paymentDetail.getPhone())) {
            ourPaymentDetails.setPhone(paymentDetail.getPhone());
        }
        if (!ourPaymentDetails.getStreet().equals(paymentDetail.getStreet())) {
            ourPaymentDetails.setStreet(paymentDetail.getStreet());
        }
        if (!ourPaymentDetails.getSwiftCode().equals(paymentDetail.getSwiftCode())) {
            ourPaymentDetails.setSwiftCode(paymentDetail.getSwiftCode());
        }
        if (!ourPaymentDetails.getTaxId().equals(paymentDetail.getTaxId())) {
            ourPaymentDetails.setTaxId(paymentDetail.getTaxId());
        }
        if (!ourPaymentDetails.getTitle().equals(paymentDetail.getTitle())) {
            ourPaymentDetails.setTitle(paymentDetail.getTitle());
        }
        if (!ourPaymentDetails.getZipCode().equals(paymentDetail.getZipCode())) {
            ourPaymentDetails.setZipCode(paymentDetail.getZipCode());
        }
        generalService.merge(ourPaymentDetails);
    }

    /**
     * ********************************************************************************************
     *********************** PAYMENT METHOD SECTION.
     * ************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(PaymentMethod.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(PaymentMethod.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentMethodDetail> getAdminPaymentMethods(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(PaymentMethod.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return paymentMethodConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentMethodDetail> getAdminPaymentMethods() throws RPCException {
        final Search search = new Search(PaymentMethod.class);
        search.addSort("id", false);
        return paymentMethodConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePaymentMethod(PaymentMethodDetail paymentMethodDetail) throws RPCException {
        PaymentMethod paymentMethod = generalService.find(PaymentMethod.class, paymentMethodDetail.getId());
        if (!paymentMethod.getName().equals(paymentMethodDetail.getName())) {
            paymentMethod.setName(paymentMethodDetail.getName());
        }
        if (!paymentMethod.getDescription().equals(paymentMethodDetail.getDescription())) {
            paymentMethod.setDescription(paymentMethodDetail.getDescription());
        }
        generalService.merge(paymentMethod);
    }

    /**
     * ********************************************************************************************
     *********************** PERMISSION SECTION.
     * *************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Permission.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Permission.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PermissionDetail> getAdminPermissions(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Permission.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return permissionConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePermission(PermissionDetail permissionDetail) throws RPCException {
        Permission permission = generalService.find(Permission.class, permissionDetail.getId());
        if (!permission.getName().equals(permissionDetail.getName())) {
            permission.setName(permissionDetail.getName());
        }
        if (!permission.getDescription().equals(permissionDetail.getDescription())) {
            permission.setDescription(permissionDetail.getDescription());
        }
        if (!permission.getCode().equals(permissionDetail.getCode())) {
            permission.setCode(permissionDetail.getCode());
        }
        generalService.merge(permission);
    }

    /**
     * ********************************************************************************************
     *********************** PREFERENCE SECTION.
     * *************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Preference.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Preference.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PreferenceDetail> getAdminPreferences(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Preference.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return preferenceConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePreference(PreferenceDetail preferenceDetail) throws RPCException {
        Preference preference = generalService.find(Preference.class, preferenceDetail.getId());
        if (!preference.getKey().equals(preferenceDetail.getKey())) {
            preference.setKey(preferenceDetail.getKey());
        }
        if (!preference.getValue().equals(preferenceDetail.getValue())) {
            preference.setValue(preferenceDetail.getValue());
        }
        if (!preference.getDescription().equals(preferenceDetail.getDescription())) {
            preference.setDescription(preferenceDetail.getDescription());
        }
        generalService.merge(preference);
    }

    /**
     * ********************************************************************************************
     *********************** PROBLEM SECTION.
     * ************************************************
     *********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminProblemsCount(SearchModuleDataHolder searchDataHolder) throws RPCException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Problem.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Problem.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ProblemDetail> getAdminProblems(SearchDefinition searchDefinition) throws RPCException {
        Search search = new Search(Problem.class);
        if (searchDefinition.getFilter() != null) {
            search = this.setFilters(searchDefinition.getFilter(), search);
        }
        search = this.setSortSearch(searchDefinition.getOrderColumns(), search);
        search.setFirstResult(searchDefinition.getStart());
        search.setMaxResults(searchDefinition.getMaxResult());
        return problemConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateProblem(ProblemDetail problemDetail) throws RPCException {
        Problem problem = generalService.find(Problem.class, problemDetail.getId());
        if (!problem.getText().equals(problemDetail.getText())) {
            problem.setText(problemDetail.getText());
        }
        generalService.merge(problem);
    }

    /**
     * ********************************************************************************************
     *********************** COMMON METHODS.
     * *************************************************
     *********************************************************************************************
     */
    private Search setSortSearch(Map<String, OrderType> orderColumns, Search search) {
        List<Sort> sorts = new ArrayList<Sort>();
        for (String str : orderColumns.keySet()) {
            if (orderColumns.get(str) == OrderType.ASC) {
                sorts.add(new Sort(str, false));
            } else {
                sorts.add(new Sort(str, true));
            }
        }
        return search.addSorts(sorts.toArray(new Sort[sorts.size()]));
    }

    private Search filter(Search search, FilterItem item) {
        switch (item.getOperation()) {
            case FilterItem.OPERATION_EQUALS:
                search.addFilterEqual(item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_LIKE:
                search.addFilterLike(item.getItem(), "%" + item.getValue().toString() + "%");
                break;
            case FilterItem.OPERATION_IN:
                search.addFilterIn(item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_FROM:
                search.addFilterGreaterOrEqual(item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_TO:
                search.addFilterLessOrEqual(item.getItem(), item.getValue());
                break;
            default:
                break;
        }
        return search;
    }

    private Search setFilters(SearchModuleDataHolder searchDataHolder, Search search) {
        for (FilterItem item : searchDataHolder.getAttributes()) {
            if (item.getItem().equals("type")) {
                search.addFilterEqual("type", demandService.getDemandType(item.getValue().toString()));
                //Da sa aj takto? AK ano, mozem odstanit demandService cim zmensim opat download fragment
//                search.addFilterEqual("type", generalService.find(DemandType.class, item.getValue().toString()));
            } else if (item.getItem().equals("companyName")) {
                Collection<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getItem().equals("personFirstName")) {
                Collection<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getItem().equals("personLastName")) {
                List<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getItem().equals("description")) {
                Collection<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getItem().equals("state")) {
                //TODO skontrolovat, v message totiz list roli + ci je to rovnaky typ triedy
                search.addFilterIn("roles.MessageUserRole.MessageUserRoleType",
                        MessageType.valueOf(item.getValue().toString()));
            } else {
                this.filter(search, item);
            }
        }
        return search;
    }
}